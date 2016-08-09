/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
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
public class ManagedBeanConfiguracionSistema {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    
    private String usuario;
    private String rol;
    private int alertas;

    
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
        }
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }
    
    public ManagedBeanConfiguracionSistema() {
    }
    
}
