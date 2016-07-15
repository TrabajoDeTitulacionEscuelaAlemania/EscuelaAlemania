/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Simulacion;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
import cl.usach.escalemania.sessionbeans.SimulacionFacadeLocal;
import java.text.SimpleDateFormat;
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
    @EJB
    private DocumentoFacadeLocal documentoFacade;
    
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
    
    public void ultimaSimulacion(){
        simulacionAnterior=simulacionFacade.ultimaSimulacion(nombrePrograma, programas);
        if(simulacionAnterior!=null)
            fechaSimulacion=new SimpleDateFormat("dd-MM-yyyy").format(simulacionAnterior.getFecha());
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
