/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;


import cl.usach.escalemania.entities.Usuario;
import cl.usach.escalemania.sessionbeans.AlertaFacadeLocal;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.UsuarioFacadeLocal;
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
public class ManagedBeanEnviarAlerta {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private AlertaFacadeLocal alertaFacade;
  
    private String usuario;
    private String rol;
    private List<Usuario> usuarios;
    private int alertas;
    private int alertasTotal;
    private int alertasUsuario;
    private String mensajeAlerta;
    private List<String> destinosAlerta;


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
                usuarios=usuarioFacade.findAll();
                alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
                alertasTotal=alertas+alertasUsuario;
                usuarios.remove(usuarioFacade.obtenerUsuario(usuario));
                usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
        }
    }

    public void enviarAlerta(){
        String resultado=alertaFacade.enviarAlerta(mensajeAlerta, destinosAlerta);
        if(resultado.compareTo("El/los usuarios fueron alertados correctamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios = usuarioFacade.findAll();
            alertasUsuario = alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal = alertas + alertasUsuario;
            usuarios.remove(usuarioFacade.obtenerUsuario(usuario));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            mensajeAlerta="";
            destinosAlerta=null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
    }

    public String getMensajeAlerta() {
        return mensajeAlerta;
    }

    public void setMensajeAlerta(String mensajeAlerta) {
        this.mensajeAlerta = mensajeAlerta;
    }

    public List<String> getDestinosAlerta() {
        return destinosAlerta;
    }

    public void setDestinosAlerta(List<String> destinosAlerta) {
        this.destinosAlerta = destinosAlerta;
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }

    public int getAlertasTotal() {
        return alertasTotal;
    }

    public void setAlertasTotal(int alertasTotal) {
        this.alertasTotal = alertasTotal;
    }

    public int getAlertasUsuario() {
        return alertasUsuario;
    }

    public void setAlertasUsuario(int alertasUsuario) {
        this.alertasUsuario = alertasUsuario;
    }
    
    public ManagedBeanEnviarAlerta() {
    }
    
}
