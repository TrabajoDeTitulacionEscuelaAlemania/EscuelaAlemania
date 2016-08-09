/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;


import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
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
public class ManagedBeanGestionPrograma {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private ProgramaFacadeLocal programaFacade;
    
    private String usuario;
    private String rol;
    private int alertas;
    private List<Programa> programas;
    private Programa programaElegido;
    private String nombreProgramaCrear;
    private String nombreProgramaEditar;
    private String nombreProgramaEliminar;
    private String programaDestino;
    private List<Programa> listaProgramas;
    private boolean guardarDocumentos;

   
    public void init(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null){
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        }else
            if (!fc.isPostback()){
                alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
                programas=programaFacade.findAll();
        }
    }
    
    public void irEditar(){
        nombreProgramaEditar=programaElegido.getPrograma();
    }
    
    public void editarPrograma(){
        String resultado=programaFacade.editarPrograma(programaElegido.getPrograma(), nombreProgramaEditar);
        if(resultado.compareTo("Nombre del programa modificado existosamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            programas=programaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('modificarProg').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }
    
    public void irCrear(){
        nombreProgramaCrear="";
    }
    
    public void crearPrograma(){
        String resultado=programaFacade.crearPrograma(nombreProgramaCrear);
        if(resultado.compareTo("Programa creado existosamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            programas=programaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('crearProg').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }

    public void irEliminar(){
        nombreProgramaEliminar=programaElegido.getPrograma();
        programaDestino="";
        guardarDocumentos=false;
    }
    
    public void cargarProgramas(){
        listaProgramas=programaFacade.findAll();
        listaProgramas.remove(programaElegido);
    }
    
    public void eliminarPrograma(){
        String resultado=programaFacade.eliminarPrograma(nombreProgramaEliminar, guardarDocumentos, programaDestino);
        if(resultado.compareTo("Error inesperado al eliminar el programa. Por favor, intentelo nuevamente")!=0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            programas=programaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('eliminarProg').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
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

    public String getNombreProgramaEditar() {
        return nombreProgramaEditar;
    }

    public void setNombreProgramaEditar(String nombreProgramaEditar) {
        this.nombreProgramaEditar = nombreProgramaEditar;
    }

    public String getNombreProgramaEliminar() {
        return nombreProgramaEliminar;
    }

    public void setNombreProgramaEliminar(String nombreProgramaEliminar) {
        this.nombreProgramaEliminar = nombreProgramaEliminar;
    }
    
    public boolean isGuardarDocumentos() {
        return guardarDocumentos;
    }

    public void setGuardarDocumentos(boolean guardarDocumentos) {
        this.guardarDocumentos = guardarDocumentos;
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }

    public List<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(List<Programa> programas) {
        this.programas = programas;
    }

    public Programa getProgramaElegido() {
        return programaElegido;
    }

    public void setProgramaElegido(Programa programaElegido) {
        this.programaElegido = programaElegido;
    }

    public String getNombreProgramaCrear() {
        return nombreProgramaCrear;
    }

    public void setNombreProgramaCrear(String nombreProgramaCrear) {
        this.nombreProgramaCrear = nombreProgramaCrear;
    }

    public String getProgramaDestino() {
        return programaDestino;
    }

    public void setProgramaDestino(String programaDestino) {
        this.programaDestino = programaDestino;
    }

    public List<Programa> getListaProgramas() {
        return listaProgramas;
    }

    public void setListaProgramas(List<Programa> listaProgramas) {
        this.listaProgramas = listaProgramas;
    }
    
    public ManagedBeanGestionPrograma() {
    }
}
