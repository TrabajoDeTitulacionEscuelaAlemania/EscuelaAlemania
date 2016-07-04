/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Seccion;
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
public class ManagedBeanBuscarDocumento {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    @EJB
    private SeccionFacadeLocal seccionFacade;
    
    private String usuario;
    private String rol;
    private List<Documento> documentos;
    
    private String busqueda;
    private List<Documento> resultadoDocumentos;
    private FacesContext fc;
    private Documento documentoElegido;
    private String nombreEstadoDocumento;
    private String ubicacion;
    private String nombreSeccion;
    private String observacion;
    private List<EstadoDocumento> estadoDocumentos;
    private List<Seccion> secciones;
    private String msg;
    private String nombreDocumento;


    public void cargarDatos(){
        System.out.println("INIT");
        fc=FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null)
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/home.xhtml?faces-redirect=true");
        else{
            if (!FacesContext.getCurrentInstance().isPostback()){
                documentos=documentoFacade.findAll();
                estadoDocumentos=estadoDocumentoFacade.findAll();
                secciones=seccionFacade.findAll();      
                busqueda="";
                resultadoDocumentos=null;
            }
        }
    }
    
    public void irAEditar(){
        System.out.println("Ir a editar");
        System.out.println(documentoElegido.getNombre());
        nombreDocumento=documentoElegido.getNombre();
        nombreEstadoDocumento=documentoElegido.getEstadoDocumento().getEstado();
        ubicacion=documentoElegido.getUbicacion();
        nombreSeccion=documentoElegido.getSeccion().getSeccion();
        observacion=documentoElegido.getObservacion();
        
    }
    
    public void editar(){
        System.out.println("Editar");
        fc=FacesContext.getCurrentInstance();
        msg=documentoFacade.editarDocumento(estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, nombreEstadoDocumento), 
                ubicacion, 
                seccionFacade.obtenerPorNombre(nombreSeccion, secciones), 
                observacion, 
                nombreDocumento,
                documentoElegido);
        if(msg.compareToIgnoreCase("ok")==0){
            documentos=documentoFacade.findAll();
            resultadoDocumentos=documentoFacade.buscarDocumento(busqueda, documentos);
            RequestContext.getCurrentInstance().execute("PF('docDialogo').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "El documento se ha modificado correctamente"));
        }else{
            if(msg.compareTo("Campo vacio")==0)
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "La ubicación del documento no puede estar vacia")); 
            if(msg.compareTo("Nombre existe")==0)
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "El nombre del documento ya existe")); 
            else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, "Ocurrió un error al modificar el documento. Inténtelo nuevamente")); 
        }
    }
    
    public void buscar(){
        System.out.println("Buscar");
        resultadoDocumentos=documentoFacade.buscarDocumento(busqueda, documentos);
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
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

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<Documento> getResultadoDocumentos() {
        return resultadoDocumentos;
    }

    public void setResultadoDocumentos(List<Documento> resultadoDocumentos) {
        this.resultadoDocumentos = resultadoDocumentos;
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
    
    public ManagedBeanBuscarDocumento() {
        
    }
   
}
