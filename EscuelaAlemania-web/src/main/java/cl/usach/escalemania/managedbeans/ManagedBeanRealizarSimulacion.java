/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Seccion;
import cl.usach.escalemania.entities.Simulacion;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
import cl.usach.escalemania.sessionbeans.SeccionFacadeLocal;
import cl.usach.escalemania.sessionbeans.SimulacionFacadeLocal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;



/**
 *
 * @author Rodrigo Rivas
 */
@ManagedBean
@SessionScoped
public class ManagedBeanRealizarSimulacion {

    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private SimulacionFacadeLocal simulacionFacade;
    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    @EJB
    private SeccionFacadeLocal seccionFacade;
    
    private String usuario;
    private String rol;
    private List<Programa> programas;
    private String nombrePrograma;
    private Simulacion simulacionActual;
    private Simulacion simulacionAnterior;
    private int alertas;
    private String fechaSimulacion;
    private int docCompletos;
    private int docIncompletos;
    private int docSinInformacion;
    private int docDesactualizados;
    private int docCompletosAnterior;
    private int docIncompletosAnterior;
    private int docSinInformacionAnterior;
    private int docDesactualizadosAnterior;
    private List<Documento> documentos;
    private List<Documento> documentosFiltrados;
    private Documento documentoElegido;
    private String nombreEstadoDocumento;
    private String ubicacion;
    private String nombreSeccion;
    private String observacion;
    private String nombreDocumento;
    private List<EstadoDocumento> estadoDocumentos;
    private List<Seccion> secciones;
    private int tipoDocumentos;

    public void initDocSim(){
        System.out.println("Init 2");
        if (!FacesContext.getCurrentInstance().isPostback()){
            System.out.println("Me meti al primer IF");
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
            usuario=(String)sesisonMap.get("usuario");
            rol=(String)sesisonMap.get("rol");
            if(usuario==null)
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/home.xhtml?faces-redirect=true");
            else {
                System.out.println("Me meti al segundo IF");
                Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
                tipoDocumentos=Integer.parseInt(params.get("tipoDocumento"));
                switch(tipoDocumentos){
                    case 1:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Completo");
                        break;
                    case 2:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), 
                            nombrePrograma), 
                            "Incompleto");
                        break;
                    case 3:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Desactualizado");
                        break;
                    case 4:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Sin informacion");
                        break;
                    default:
                        break;
                }
                secciones = seccionFacade.findAll();
                estadoDocumentos = estadoDocumentoFacade.findAll();
                alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            }
        }
    }
    
    public void init(){
        System.out.println("INIT");
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null)
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/home.xhtml?faces-redirect=true");
        else{
            if (!FacesContext.getCurrentInstance().isPostback()){
                programas=programaFacade.findAll();
                nombrePrograma="";
                simulacionActual=null;
                simulacionAnterior=null;
                alertas=documentoFacade.obtenerAlertas(documentoFacade.findAll());
                fechaSimulacion=null;
                docCompletos=0;
                docIncompletos=0;
                docSinInformacion=0;
                docDesactualizados=0;
                docCompletosAnterior=0;
                docIncompletosAnterior=0;
                docSinInformacionAnterior=0;
                docDesactualizadosAnterior=0;
            }
        }
    }
    
    public void obtenerUltimaSimulacion(){
        ultimaSimulacion();
        if(simulacionAnterior==null)
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No existe registro de simulacion para el programa seleccionado"));
    }
    
    public void ultimaSimulacion(){
        simulacionAnterior=simulacionFacade.ultimaSimulacion(nombrePrograma, programas);
        if(simulacionAnterior!=null)
            fechaSimulacion=new SimpleDateFormat("dd-MM-yyyy").format(simulacionAnterior.getFecha());
        else
            fechaSimulacion="";
    }
    
    public void realizarSimulacion(){
        ultimaSimulacion();
        if(simulacionAnterior!=null){
            docCompletosAnterior=simulacionAnterior.getDocCompletosImportante()+simulacionAnterior.getDocCompletosNormal()+simulacionAnterior.getDocCompletosVital();
            docIncompletosAnterior = simulacionAnterior.getDocIncompletosImportante() + simulacionAnterior.getDocIncompletosNormal() + simulacionAnterior.getDocIncompletosVital();
            docDesactualizadosAnterior = simulacionAnterior.getDocDesactualizadosImportante() + simulacionAnterior.getDocDesactualizadosNormal() + simulacionAnterior.getDocDesactualizadosVital();
            docSinInformacionAnterior=simulacionAnterior.getDocSinInformacionImportante()+simulacionAnterior.getDocSinInformacionNormal()+simulacionAnterior.getDocSinInformacionVital();
        }
        simulacionActual=simulacionFacade.crearSimulacion(nombrePrograma, programas);
        docCompletos=simulacionActual.getDocCompletosImportante()+simulacionActual.getDocCompletosNormal()+simulacionActual.getDocCompletosVital();
        docIncompletos=simulacionActual.getDocIncompletosImportante()+simulacionActual.getDocIncompletosNormal()+simulacionActual.getDocIncompletosVital();
        docDesactualizados=simulacionActual.getDocDesactualizadosImportante()+simulacionActual.getDocDesactualizadosNormal()+simulacionActual.getDocDesactualizadosVital();
        docSinInformacion=simulacionActual.getDocSinInformacionImportante()+simulacionActual.getDocSinInformacionNormal()+simulacionActual.getDocSinInformacionVital();
        alertas=documentoFacade.obtenerAlertas(documentoFacade.findAll());
    }

    public void mostrarDocCompletos(){
        FacesContext fc=FacesContext.getCurrentInstance();
        if(docCompletos==0)
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Información", "No existen documentos completos para inspeccionar"));
        else{
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/documentos_simulacion.xhtml?faces-redirect=true&includeViewParams=true&tipoDocumento=1");
        }
    }
    
    public void mostrarDocIncompletos(){
        FacesContext fc=FacesContext.getCurrentInstance();
        if(docIncompletos==0)
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Información", "No existen documentos incompletos para inspeccionar"));
        else{
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/documentos_simulacion.xhtml?faces-redirect=true&includeViewParams=true&tipoDocumento=2");
        }
    }
    
    public void mostrarDocDesactualizados(){
        FacesContext fc=FacesContext.getCurrentInstance();
        if(docDesactualizados==0)
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Información", "No existen documentos desactualizados para inspeccionar"));
        else{
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/documentos_simulacion.xhtml?faces-redirect=true&includeViewParams=true&tipoDocumento=3");
        }
    }
    
        public void mostrarDocSinInformacion(){
            FacesContext fc=FacesContext.getCurrentInstance();
        if(docSinInformacion==0)
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Información", "No existen documentos sin información para inspeccionar"));
        else{
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/documentos_simulacion.xhtml?faces-redirect=true&includeViewParams=true&tipoDocumento=4");
        }
    }

    public void irAEditar(){
        nombreDocumento=documentoElegido.getNombre();
        nombreEstadoDocumento=documentoElegido.getEstadoDocumento().getEstado();
        ubicacion=documentoElegido.getUbicacion();
        nombreSeccion=documentoElegido.getSeccion().getSeccion();
        observacion=documentoElegido.getObservacion();
    }
    
    public void editar(){
        System.out.println("Editar");
        String msg = documentoFacade.editarDocumento(estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, nombreEstadoDocumento), 
                ubicacion,
                seccionFacade.obtenerPorNombre(nombreSeccion, secciones),
                observacion, 
                nombreDocumento,
                documentoElegido);
        if(msg.compareToIgnoreCase("Cambios realizados correctamente")==0){
            switch(tipoDocumentos){
                    case 1:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Completo");
                        break;
                    case 2:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), 
                            nombrePrograma), 
                            "Incompleto");
                        break;
                    case 3:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Desactualizado");
                        break;
                    case 4:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Sin informacion");
                        break;
                    default:
                        break;
                }
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            RequestContext.getCurrentInstance().execute("PF('docDialogo').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", msg));
        }else
            if(msg.compareTo("Error al modificar")==0)
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                        msg, "Ocurrió un error al modificar el documento. Inténtelo nuevamente")); 
            else
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", msg));
    }

    public void eliminarDocumento(){
        String resultado=documentoFacade.eliminarDocumento(documentoElegido);
        if(resultado.compareTo("Documento eliminado exitosamente")==0){
            switch(tipoDocumentos){
                    case 1:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Completo");
                        break;
                    case 2:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), 
                            nombrePrograma), 
                            "Incompleto");
                        break;
                    case 3:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Desactualizado");
                        break;
                    case 4:
                        documentos=documentoFacade.filtrarPorEstado(documentoFacade.filtrarPorPrograma(documentoFacade.findAll(), nombrePrograma), 
                            "Sin informacion");
                        break;
                    default:
                        break;
                }
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información", resultado));
    }

    public List<Documento> getDocumentosFiltrados() {
        return documentosFiltrados;
    }

    public void setDocumentosFiltrados(List<Documento> documentosFiltrados) {
        this.documentosFiltrados = documentosFiltrados;
    }

    public List<EstadoDocumento> getEstadoDocumentos() {
        return estadoDocumentos;
    }

    public void setEstadoDocumentos(List<EstadoDocumento> estadoDocumentos) {
        this.estadoDocumentos = estadoDocumentos;
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }
    
    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public Documento getDocumentoElegido() {
        return documentoElegido;
    }

    public void setDocumentoElegido(Documento documentoElegido) {
        this.documentoElegido = documentoElegido;
    }

    public String getNombreEstadoDocumento() {
        return nombreEstadoDocumento;
    }

    public void setNombreEstadoDocumento(String nombreEstadoDocumento) {
        this.nombreEstadoDocumento = nombreEstadoDocumento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
    
    public int getDocCompletosAnterior() {
        return docCompletosAnterior;
    }

    public void setDocCompletosAnterior(int docCompletosAnterior) {
        this.docCompletosAnterior = docCompletosAnterior;
    }

    public int getDocIncompletosAnterior() {
        return docIncompletosAnterior;
    }

    public void setDocIncompletosAnterior(int docIncompletosAnterior) {
        this.docIncompletosAnterior = docIncompletosAnterior;
    }

    public int getDocSinInformacionAnterior() {
        return docSinInformacionAnterior;
    }

    public void setDocSinInformacionAnterior(int docSinInformacionAnterior) {
        this.docSinInformacionAnterior = docSinInformacionAnterior;
    }

    public int getDocDesactualizadosAnterior() {
        return docDesactualizadosAnterior;
    }

    public void setDocDesactualizadosAnterior(int docDesactualizadosAnterior) {
        this.docDesactualizadosAnterior = docDesactualizadosAnterior;
    }

    public Simulacion getSimulacionActual() {
        return simulacionActual;
    }

    public int getDocCompletos() {
        return docCompletos;
    }

    public void setDocCompletos(int docCompletos) {
        this.docCompletos = docCompletos;
    }

    public int getDocIncompletos() {
        return docIncompletos;
    }

    public void setDocIncompletos(int docIncompletos) {
        this.docIncompletos = docIncompletos;
    }

    public int getDocSinInformacion() {
        return docSinInformacion;
    }

    public void setDocSinInformacion(int docSinInformacion) {
        this.docSinInformacion = docSinInformacion;
    }

    public int getDocDesactualizados() {
        return docDesactualizados;
    }

    public void setDocDesactualizados(int docDesactualizados) {
        this.docDesactualizados = docDesactualizados;
    }

    public String getFechaSimulacion() {
        return fechaSimulacion;
    }

    public void setFechaSimulacion(String fechaSimulacion) {
        this.fechaSimulacion = fechaSimulacion;
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }
    
    public void setSimulacionActual(Simulacion simulacionActual) {
        this.simulacionActual = simulacionActual;
    }

    public Simulacion getSimulacionAnterior() {
        return simulacionAnterior;
    }

    public void setSimulacionAnterior(Simulacion simulacionAnterior) {
        this.simulacionAnterior = simulacionAnterior;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(List<Programa> programas) {
        this.programas = programas;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }
    
    public ManagedBeanRealizarSimulacion() {
    }
    
}
