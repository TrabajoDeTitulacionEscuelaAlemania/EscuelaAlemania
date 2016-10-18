/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.Alerta;
import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Seccion;
import cl.usach.escalemania.sessionbeans.AlertaFacadeLocal;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.SeccionFacadeLocal;
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
public class ManagedBeanVerAlertas {

    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private SeccionFacadeLocal seccionFacade;
    @EJB
    private AlertaFacadeLocal alertaFacade;
            
    private String usuario;
    private String rol;
    private List<Documento> documentosAlertas;
    private Documento documentoElegido;
    private String nombreEstadoDocumento;
    private String ubicacion;
    private String nombreSeccion;
    private String observacion;
    private List<EstadoDocumento> estadoDocumentos;
    private List<Seccion> secciones;
    private String nombreDocumento;
    private int alertas;
    private int alertasUsuario;
    private int alertasTotal;
    private List<Alerta> listaAlertasUsuario;
    private Alerta alertaSeleccionada;

    public void initAlertasUsuario(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null){
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        }else{
            if (!FacesContext.getCurrentInstance().isPostback()){
                alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
                listaAlertasUsuario=alertaFacade.obtenerAlertas(usuario);
                alertasUsuario=listaAlertasUsuario.size();
                alertasTotal=alertas+alertasUsuario;
            }
        }
    }
    
    public void init(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null){
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        }else{
            if (!FacesContext.getCurrentInstance().isPostback()){
                documentosAlertas=estadoDocumentoFacade.obtenerDocumentoPorId("2");
                documentosAlertas.addAll(estadoDocumentoFacade.obtenerDocumentoPorId("3"));
                documentosAlertas.addAll(estadoDocumentoFacade.obtenerDocumentoPorId("4"));
                alertas=documentosAlertas.size();
                alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
                alertasTotal=alertas+alertasUsuario;
                estadoDocumentos=estadoDocumentoFacade.findAll();
                secciones=seccionFacade.findAll();
            }
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
        String msg = documentoFacade.editarDocumento(estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, nombreEstadoDocumento), 
                ubicacion,
                seccionFacade.obtenerPorNombre(nombreSeccion, secciones),
                observacion, 
                nombreDocumento,
                documentoElegido,
                usuario);
        if(msg.compareToIgnoreCase("Cambios realizados correctamente")==0){
            documentosAlertas=estadoDocumentoFacade.obtenerDocumentoPorId("2");
            documentosAlertas.addAll(estadoDocumentoFacade.obtenerDocumentoPorId("3"));
            documentosAlertas.addAll(estadoDocumentoFacade.obtenerDocumentoPorId("4"));
            alertas=documentosAlertas.size();
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
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
            documentosAlertas=estadoDocumentoFacade.obtenerDocumentoPorId("2");
            documentosAlertas.addAll(estadoDocumentoFacade.obtenerDocumentoPorId("3"));
            documentosAlertas.addAll(estadoDocumentoFacade.obtenerDocumentoPorId("4"));
            alertas=documentosAlertas.size();
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información", resultado));
    }
    
    public void marcarLeido(){
        String resultado=alertaFacade.marcarLiedo(alertaSeleccionada);
        if(resultado.compareTo("La alerta ha sido marcada como leida")==0){
           alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            listaAlertasUsuario = alertaFacade.obtenerAlertas(usuario);
            alertasUsuario = listaAlertasUsuario.size();
            alertasTotal = alertas + alertasUsuario;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información", resultado));
    }
     
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public int getAlertasUsuario() {
        return alertasUsuario;
    }

    public void setAlertasUsuario(int alertasUsuario) {
        this.alertasUsuario = alertasUsuario;
    }

    public int getAlertasTotal() {
        return alertasTotal;
    }

    public Alerta getAlertaSeleccionada() {
        return alertaSeleccionada;
    }

    public void setAlertaSeleccionada(Alerta alertaSeleccionada) {
        this.alertaSeleccionada = alertaSeleccionada;
    }

    public List<Alerta> getListaAlertasUsuario() {
        return listaAlertasUsuario;
    }

    public void setListaAlertasUsuario(List<Alerta> listaAlertasUsuario) {
        this.listaAlertasUsuario = listaAlertasUsuario;
    }

    public void setAlertasTotal(int alertasTotal) {
        this.alertasTotal = alertasTotal;
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

    public List<Documento> getDocumentosAlertas() {
        return documentosAlertas;
    }

    public void setDocumentosAlertas(List<Documento> documentosAlertas) {
        this.documentosAlertas = documentosAlertas;
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

    
    public ManagedBeanVerAlertas() {
    }
}
