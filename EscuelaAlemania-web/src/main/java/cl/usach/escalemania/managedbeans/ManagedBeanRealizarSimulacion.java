/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Simulacion;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
import cl.usach.escalemania.sessionbeans.SimulacionFacadeLocal;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;



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
    
    private String usuario;
    private String rol;
    private List<Programa> programas;
    private String nombrePrograma;
    private Simulacion simulacionActual;
    private Simulacion simulacionAnterior;

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
            }
        }
    }
    
    public void realizarSimulacion(){
        simulacionAnterior=simulacionFacade.ultimaSimulacion(nombrePrograma, programas);
        simulacionActual=simulacionFacade.crearSimulacion(nombrePrograma, programas);
    }

    public Simulacion getSimulacionActual() {
        return simulacionActual;
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
