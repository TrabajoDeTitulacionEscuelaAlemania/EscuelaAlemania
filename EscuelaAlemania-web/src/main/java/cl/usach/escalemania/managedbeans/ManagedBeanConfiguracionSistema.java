/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.ConfiguracionMail;
import cl.usach.escalemania.entities.ParametroSistema;
import cl.usach.escalemania.entities.Usuario;
import cl.usach.escalemania.sessionbeans.ConfiguracionMailFacadeLocal;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ParametroSistemaFacadeLocal;
import cl.usach.escalemania.sessionbeans.UsuarioFacadeLocal;
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
public class ManagedBeanConfiguracionSistema {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private ParametroSistemaFacadeLocal parametroSistemaFacade;
    @EJB
    private ConfiguracionMailFacadeLocal configuracionMailFacade;
    
    private String usuario;
    private String rol;
    private int alertas;
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    private String nombreUsuarioReestablecer;
    private String nombreUsuarioEliminar;
    private String nombreUsuarioCrear;
    private String contraseñaActual;
    private String contraseña1;
    private String contraseña2;
    private String correoUsuario;
    private String contraseñaVisitante1;
    private String contraseñaVisitante2;
    private List<ConfiguracionMail> configuracionMails;
    private List<ParametroSistema> parametrosSistema;
    private ConfiguracionMail configuracionMailSeleccionado;
    private ParametroSistema parametroSistemaSeleccionado;
    private String nombreParametroSistema;
    private String valorParametroSistema;
    private String nombreConfiguracionMail;
    private String valorConfiguracionMail;
    
    
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
                usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
                usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
                configuracionMails=configuracionMailFacade.findAll();
                parametrosSistema=parametroSistemaFacade.findAll();
        }
    }
    
    public void irReestablecer(){
        nombreUsuarioReestablecer=usuarioSeleccionado.getUsuario();
    }
    
    public void reestablecerContraseña(){
        String resultado=usuarioFacade.reestablecerContraseña(nombreUsuarioReestablecer);
        if(resultado.compareTo("Contraseña reestablecida correctamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('reestablecerPass').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }
    
    public void irEliminar(){
        nombreUsuarioEliminar=usuarioSeleccionado.getUsuario();
    }
    
    public void eliminarUsuario(){
        String resultado=usuarioFacade.eliminarUsuario(nombreUsuarioEliminar);
        if(resultado.compareTo("Usuario eliminado correctamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('eliminarUser').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }
    
    public void irCrear(){
        nombreUsuarioCrear="";
    }
    
    public void crearUsuario(){
        String resultado=usuarioFacade.crearUsuario(nombreUsuarioCrear);
        if(resultado.compareTo("Usuario creado existosamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('crearUser').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
        
    }
    
    public void irCambiarContraseña(){
        contraseñaActual="";
        contraseña1="";
        contraseña2="";
    }
    
    public void cambiarContraseña(){
        String resultado=usuarioFacade.cambiarContraseña(usuario, contraseñaActual, contraseña1, contraseña2);
        if(resultado.compareTo("Contraseña cambiada exitosamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('cambiarPass').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
        
    }

    public void irAsociarCorreo(){
        correoUsuario=usuarioFacade.obtenerUsuario(usuario).getCorreo();
    }
    
    public void asociarCorreo(){
        String resultado=usuarioFacade.asociarCorreo(usuario, correoUsuario);
        if(resultado.compareTo("Correo asociado exitosamente a su cuenta")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('asociarMail').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }
    
    public void irCambiarVisitante(){
        contraseñaVisitante1="";
        contraseñaVisitante2="";
    }
    
    public void cambiarContraseñaVisitante(){
        String resultado=usuarioFacade.cambiarContraseñaVisitante(contraseñaVisitante1, contraseñaVisitante2);
        if(resultado.compareTo("La contraseña del usuario Visitante fue modificada correctamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('cambiarPassV').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }

    public void irEditarParametro(){
        nombreParametroSistema=parametroSistemaSeleccionado.getTipoParametro();
        valorParametroSistema=parametroSistemaSeleccionado.getValor();
        System.out.println(nombreParametroSistema);
    }
    
    public void editarParametroSistema(){
        String resultado=parametroSistemaFacade.modificarParametro(nombreParametroSistema, valorParametroSistema);
        if(resultado.compareTo("Cambios realizados exitosamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('editarPS').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }
    
    public void irEditarConfiguracionMail(){
        nombreConfiguracionMail=configuracionMailSeleccionado.getTipo();
        valorConfiguracionMail=configuracionMailSeleccionado.getValor();
        System.out.println(nombreConfiguracionMail);
    }
    
    public void editarConfiguracionMail(){
        String resultado=parametroSistemaFacade.modificarParametro(nombreConfiguracionMail, valorConfiguracionMail);
        if(resultado.compareTo("Cambios realizados exitosamente")==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            parametrosSistema=parametroSistemaFacade.findAll();
            RequestContext.getCurrentInstance().execute("PF('editarCM').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
    }

    public List<ConfiguracionMail> getConfiguracionMails() {
        return configuracionMails;
    }

    public void setConfiguracionMails(List<ConfiguracionMail> configuracionMails) {
        this.configuracionMails = configuracionMails;
    }

    public List<ParametroSistema> getParametrosSistema() {
        return parametrosSistema;
    }

    public void setParametrosSistema(List<ParametroSistema> parametrosSistema) {
        this.parametrosSistema = parametrosSistema;
    }

    public ConfiguracionMail getConfiguracionMailSeleccionado() {
        return configuracionMailSeleccionado;
    }

    public void setConfiguracionMailSeleccionado(ConfiguracionMail configuracionMailSeleccionado) {
        this.configuracionMailSeleccionado = configuracionMailSeleccionado;
    }

    public ParametroSistema getParametroSistemaSeleccionado() {
        return parametroSistemaSeleccionado;
    }

    public void setParametroSistemaSeleccionado(ParametroSistema parametroSistemaSeleccionado) {
        this.parametroSistemaSeleccionado = parametroSistemaSeleccionado;
    }

    public String getNombreParametroSistema() {
        return nombreParametroSistema;
    }

    public void setNombreParametroSistema(String nombreParametroSistema) {
        this.nombreParametroSistema = nombreParametroSistema;
    }

    public String getValorParametroSistema() {
        return valorParametroSistema;
    }

    public void setValorParametroSistema(String valorParametroSistema) {
        this.valorParametroSistema = valorParametroSistema;
    }

    public String getNombreConfiguracionMail() {
        return nombreConfiguracionMail;
    }

    public void setNombreConfiguracionMail(String nombreConfiguracionMail) {
        this.nombreConfiguracionMail = nombreConfiguracionMail;
    }

    public String getValorConfiguracionMail() {
        return valorConfiguracionMail;
    }

    public void setValorConfiguracionMail(String valorConfiguracionMail) {
        this.valorConfiguracionMail = valorConfiguracionMail;
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

    public String getContraseñaVisitante1() {
        return contraseñaVisitante1;
    }

    public void setContraseñaVisitante1(String contraseñaVisitante1) {
        this.contraseñaVisitante1 = contraseñaVisitante1;
    }

    public String getContraseñaVisitante2() {
        return contraseñaVisitante2;
    }

    public void setContraseñaVisitante2(String contraseñaVisitante2) {
        this.contraseñaVisitante2 = contraseñaVisitante2;
    }
    
    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContraseñaActual() {
        return contraseñaActual;
    }

    public void setContraseñaActual(String contraseñaActual) {
        this.contraseñaActual = contraseñaActual;
    }

    public String getContraseña1() {
        return contraseña1;
    }

    public void setContraseña1(String contraseña1) {
        this.contraseña1 = contraseña1;
    }

    public String getContraseña2() {
        return contraseña2;
    }

    public void setContraseña2(String contraseña2) {
        this.contraseña2 = contraseña2;
    }

    public String getNombreUsuarioCrear() {
        return nombreUsuarioCrear;
    }

    public void setNombreUsuarioCrear(String nombreUsuarioCrear) {
        this.nombreUsuarioCrear = nombreUsuarioCrear;
    }

    public String getNombreUsuarioEliminar() {
        return nombreUsuarioEliminar;
    }

    public void setNombreUsuarioEliminar(String nombreUsuarioEliminar) {
        this.nombreUsuarioEliminar = nombreUsuarioEliminar;
    }

    public String getNombreUsuarioReestablecer() {
        return nombreUsuarioReestablecer;
    }

    public void setNombreUsuarioReestablecer(String nombreUsuarioReestablecer) {
        this.nombreUsuarioReestablecer = nombreUsuarioReestablecer;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
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
    
    public ManagedBeanConfiguracionSistema() {
    }
    
}
