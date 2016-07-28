/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.sessionbeans.ValidacionLocal;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;




/**
 *
 * @author Desarrollo
 */
@ManagedBean
@SessionScoped
public class ManagedBeanLogin{

    @EJB
    private ValidacionLocal validacion;
    
    private String usuario;
    private String contraseña;
    private String mensaje;
    

    public ValidacionLocal getValidacion() {
        return validacion;
    }

    public void setValidacion(ValidacionLocal validacion) {
        this.validacion = validacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public void init(){
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        if(sesisonMap.get("usuario")!=null){
            fc.getExternalContext().invalidateSession();
        }
    }
    
    public void login(){
        mensaje=validacion.login(usuario, contraseña);
        FacesContext fc=FacesContext.getCurrentInstance();
        if(mensaje.compareTo("Usuario no existe")==0 || mensaje.compareTo("Contraseña invalida")==0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", mensaje));
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=false");
        }else{
            
            fc.getExternalContext().getSessionMap().put("rol", mensaje);
            fc.getExternalContext().getSessionMap().put("usuario", usuario);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "home.xhtml?faces-redirect=true");
        }
    }
    
    
    
}
