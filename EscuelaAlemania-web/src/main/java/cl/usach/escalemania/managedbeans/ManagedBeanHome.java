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
 * @author Desarrollo
 */
@ManagedBean
@SessionScoped
public class ManagedBeanHome {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    
    private String rol;
    private String usuario;
    private String cantidadDocs;
    private FacesContext fc;

    public DocumentoFacadeLocal getDocumentoFacade() {
        return documentoFacade;
    }

    public void setDocumentoFacade(DocumentoFacadeLocal documentoFacade) {
        this.documentoFacade = documentoFacade;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCantidadDocs() {
        return cantidadDocs;
    }

    public void setCantidadDocs(String cantidadDocs) {
        this.cantidadDocs = cantidadDocs;
    }

    public FacesContext getFc() {
        return fc;
    }

    public void setFc(FacesContext fc) {
        this.fc = fc;
    }
    
    
    public ManagedBeanHome() {
        
    }
    
    
    public void init(){
        fc=FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null){
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        }else{
            cantidadDocs=documentoFacade.count()+"";            
        }
    }
    
}
