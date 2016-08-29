/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.ParametroSistema;
import cl.usach.escalemania.entities.Programa;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class Planificacion implements PlanificacionLocal {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    @EJB
    private ParametroSistemaFacadeLocal parametroSistemaFacade;
    @EJB
    private SimulacionFacadeLocal simulacionFacade;
    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private ConfiguracionMailFacadeLocal configuracionMailFacade;

    //@Schedule(second = "30",minute = "40",hour = "22",dayOfMonth = "*")
    @Schedule(second = "1", minute = "1", hour = "9", dayOfMonth = "*")
    @Override
    public void setearEstadoDOcumentos() {
        List<ParametroSistema> fechas = parametroSistemaFacade.obtenerListaParametros("Setear Estado");
        String fechaActual = new SimpleDateFormat("dd-MM").format(new Date());
        if (fechas != null) {
            for (ParametroSistema fecha : fechas) {
                if (fechaActual.compareTo(fecha.getValor()) == 0) {
                    List<Documento> documentos = documentoFacade.findAll();
                    List<EstadoDocumento> estadoDocumentos = estadoDocumentoFacade.findAll();
                    EstadoDocumento estadoDocumento = estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, "Desactualizado");
                    for (Documento documento : documentos) {
                        documento.setEstadoDocumento(estadoDocumento);
                        documentoFacade.edit(documento);
                    }
                    break;
                }
            }
        }
    }

    //@Schedule(second = "30",minute = "37",hour = "22",dayOfMonth = "*")
    @Schedule(second = "1", minute = "15", hour = "9", dayOfMonth = "*")
    @Override
    public void realizarSimulacion() {
        List<ParametroSistema> fechas = parametroSistemaFacade.obtenerListaParametros("Simulacion Mensual");
        String fechaActual = new SimpleDateFormat("dd").format(new Date());
        if (fechas != null) {
            if (fechaActual.compareTo(fechas.get(0).getValor()) == 0) {
                List<Programa> programas = programaFacade.findAll();
                for(Programa prog:programas)
                    simulacionFacade.crearSimulacion(prog.getPrograma(), programas);
                List<ParametroSistema> mailsSimulacion = parametroSistemaFacade.obtenerListaParametros("Mail Simulacion");
                if (mailsSimulacion != null) {
                    List<String> mails = new ArrayList<>();
                    for (ParametroSistema ps : mailsSimulacion) {
                        mails.add(ps.getValor());
                    }
                    String asunto = "Resultados simulacion mensual";
                    String mensaje = "Se han realizado las simulaciones mensuales a todos los programas existentes. Para obtener los resultados ingrese al sistema y consulte por los resultados de la última simulación."
                            + "\nSaludos.";
                    System.out.println(configuracionMailFacade.enviarMail(mails, mensaje, asunto));
                }
            }
        }
    }

    @Override
    public String revisionEstadoDocumentos() {
        ParametroSistema frecuenciaRevision = parametroSistemaFacade.obtenerParametroPorNombre("Frecuencia revision documentos");
        ParametroSistema mailNotificacion = parametroSistemaFacade.obtenerParametroPorNombre("Mail notificacion");
        List<String> mails = new ArrayList<>();
        mails.add(mailNotificacion.getValor());
        double resultadoRevision;
        String asunto = "Notificación sobre estado de documentos";
        String contenido = "El sistema posee actualmente sobre el 30% de sus documentos con estado incompleto, "
                + "desactualizado o sin información. Exactamente, esta cifra bordea el ";
        switch (Integer.parseInt(frecuenciaRevision.getValor())) {
            case 1:
                resultadoRevision = porcentajeDocumentosAlerta();
                if (resultadoRevision > 30) {
                    configuracionMailFacade.enviarMail(mails, contenido + resultadoRevision + "%.\n\nSaludos.", asunto);
                    return "La notificación ha sido enviada";
                }
                break;
            case 2:
                if (notificacionSemanal()) {
                    resultadoRevision = porcentajeDocumentosAlerta();
                    if (resultadoRevision > 30) {
                        configuracionMailFacade.enviarMail(mails, contenido + resultadoRevision + "%.\n\nSaludos.", asunto);
                        return "La notificación ha sido enviada";
                    }
                } else {
                    return "Ninguna notificación fue enviada";
                }
                break;
            case 3:
                if (notificacionMensual()) {
                    resultadoRevision = porcentajeDocumentosAlerta();
                    if (resultadoRevision > 30) {
                        configuracionMailFacade.enviarMail(mails, contenido + resultadoRevision + "%.\n\nSaludos.", asunto);
                        return "La notificación ha sido enviada";
                    }
                } else {
                    return "Ninguna notificación fue enviada";
                }
                break;
            default:
                break;
        }
        return "Ya se ha envaido una notificación";
    }

    //@Schedule(second = "30",minute = "",hour = "22",dayOfMonth = "*")
    @Schedule(second = "1", minute = "30", hour = "9", dayOfMonth = "*")
    @Override
    public void notificarEstadoDocumenos() {
        revisionEstadoDocumentos();
    }

    public boolean notificacionSemanal() {
        ParametroSistema realizarRevision = parametroSistemaFacade.obtenerParametroPorNombre("Revision realizada");
        int valorRealizarRevision = Integer.parseInt(realizarRevision.getValor());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        if (valorRealizarRevision == 1) {
            if (diaSemana == 1) {
                realizarRevision.setValor("0");
                parametroSistemaFacade.edit(realizarRevision);
            }
            return false;
        } else {
            if (diaSemana == 1) {
                realizarRevision.setValor("0");
            } else {
                realizarRevision.setValor("1");
            }
            parametroSistemaFacade.edit(realizarRevision);
            return true;
        }
    }

    public boolean notificacionMensual() {
        ParametroSistema realizarRevision = parametroSistemaFacade.obtenerParametroPorNombre("Revision realizada");
        int valorRealizarRevision = Integer.parseInt(realizarRevision.getValor());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaMes = calendar.get(Calendar.DAY_OF_MONTH);
        if (valorRealizarRevision == 1) {
            if (diaMes == 1) {
                realizarRevision.setValor("0");
                parametroSistemaFacade.edit(realizarRevision);
            }
            return false;
        } else {
            if (diaMes == 1) {
                realizarRevision.setValor("0");
            } else {
                realizarRevision.setValor("1");
            }
            parametroSistemaFacade.edit(realizarRevision);
            return true;
        }
    }

    public double porcentajeDocumentosAlerta() {
        List<Documento> documentos = documentoFacade.findAll();
        int documentosAlertas = documentoFacade.obtenerAlertas(documentos);
        return Math.round((documentosAlertas * 100.0 / documentos.size()) * 100.0) / 100.0;
    }
}
