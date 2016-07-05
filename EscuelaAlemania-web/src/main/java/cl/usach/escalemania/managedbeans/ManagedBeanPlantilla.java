/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import javax.ejb.EJB;
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
public class ManagedBeanPlantilla {

    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    
    private String usuario;
    private String rol;
    private int alertas;

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

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }

    
    public void init(){
            int incompletos = estadoDocumentoFacade.obtenerDocumentoPorId("2").size();
            int desactualizados = estadoDocumentoFacade.obtenerDocumentoPorId("3").size();
            int sinInformacion = estadoDocumentoFacade.obtenerDocumentoPorId("4").size();
            alertas=incompletos+desactualizados+sinInformacion;
            RequestContext.getCurrentInstance().update("plantilla:alert");
            System.out.println("Plantilla alertas: "+alertas);
        
    }
    
    public void redirigir(String tipoDoc){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().getSessionMap().put("tipoDoc", tipoDoc);
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/gestion_documentos.xhtml?faces-redirect=true");
    }
    
    public ManagedBeanPlantilla() {
    }
    
}
