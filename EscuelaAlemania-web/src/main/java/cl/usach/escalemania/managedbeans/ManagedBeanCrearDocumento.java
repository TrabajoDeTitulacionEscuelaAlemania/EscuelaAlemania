/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Seccion;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
import cl.usach.escalemania.sessionbeans.SeccionFacadeLocal;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Rodrigo Rivas
 */
@ManagedBean
@SessionScoped
public class ManagedBeanCrearDocumento {

    @EJB
    private SeccionFacadeLocal seccionFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private DocumentoFacadeLocal documentoFacade;
    
    private String usuario;
    private String rol;
    private List<Programa> programas;
    private List<EstadoDocumento> estadoDocumentos;
    private List<Seccion> secciones;
    private String nombre;
    private String observacion;
    private String ubicación;
    private String estadoDocumento;
    private String seccion;
    private List<String> programa;

    
    public ManagedBeanCrearDocumento() {
    }

    public List<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(List<Programa> programas) {
        this.programas = programas;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUbicación() {
        return ubicación;
    }

    public void setUbicación(String ubicación) {
        this.ubicación = ubicación;
    }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public List<String> getPrograma() {
        return programa;
    }

    public void setPrograma(List<String> programa) {
        this.programa = programa;
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
                estadoDocumentos=estadoDocumentoFacade.findAll();
                secciones=seccionFacade.findAll();
                programas=programaFacade.findAll();
                estadoDocumento="Sin informacion";
                seccion="Vital";
            }
        }
    }
    
    public void crearDocumento(){
        String resultado=documentoFacade.crearDocumento(nombre,
                ubicación, 
                observacion, 
                estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, estadoDocumento), 
                seccionFacade.obtenerPorNombre(seccion, secciones), 
                programaFacade.obtenerListaDeProgramas(programa, programas));
        if(resultado.compareTo("Documento creado existosamente")==0){
            nombre="";
            ubicación="";
            observacion="";
            estadoDocumento="";
            seccion="";
            programa=null;
            estadoDocumento="Sin informacion";
            seccion="Vital";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            if(resultado.compareTo("Error")==0)
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                        resultado, "Ocurrió un error al modificar el documento. Inténtelo nuevamente"));
            else
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }
}
