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
    
    //@Schedule(second = "*/50",minute = "*",hour = "*",dayOfMonth = "*")
    @Schedule(second = "1",minute = "1",hour = "20",dayOfMonth = "*")
    @Override
    public void setearEstadoDOcumentos() {
        List<ParametroSistema> fechas=parametroSistemaFacade.obtenerListaParametros("Setear Estado");
        String fechaActual=new SimpleDateFormat("dd-MM").format(new Date());
        if(fechas!=null)
            for(ParametroSistema fecha : fechas)
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
    
    //@Schedule(second = "40",minute = "*/1",hour = "*",dayOfMonth = "*")
    @Schedule(second = "1",minute = "1",hour = "20",dayOfMonth = "*")
    @Override
    public void realizarSimulacion(){
        List<ParametroSistema> fechas=parametroSistemaFacade.obtenerListaParametros("Simulacion Mensual");
        String fechaActual=new SimpleDateFormat("dd").format(new Date());
        if(fechas!=null)
            if(fechaActual.compareTo(fechas.get(0).getValor())==0){
                List<Programa> programas=programaFacade.findAll();
                simulacionFacade.crearSimulacion("Programa de Integracion Escolar", programas);
                simulacionFacade.crearSimulacion("Subvencion Escolar Preferencial", programas);
                simulacionFacade.crearSimulacion("Revision integral", programas);
                simulacionFacade.crearSimulacion("Procedimiento de traspaso de establecimientos educacionales a nuevos directores", programas);
                List<ParametroSistema> mailsSimulacion=parametroSistemaFacade.obtenerListaParametros("Mail Simulacion");
                if(mailsSimulacion!=null){
                    List<String> mails=new ArrayList<>();
                    for(ParametroSistema ps:mailsSimulacion)
                        mails.add(ps.getValor());
                    String asunto="Resultados simulacion mensual";
                    String mensaje="Se han realizado las simulaciones mensuales a todos los programas existentes. Para obtener los resultados ingrese al sistema y consulte por los resultados de la última simulación."
                            + "\nSaludos.";
                    System.out.println(configuracionMailFacade.enviarMail(mails, mensaje,asunto));
                }
            }
    }

    
}
