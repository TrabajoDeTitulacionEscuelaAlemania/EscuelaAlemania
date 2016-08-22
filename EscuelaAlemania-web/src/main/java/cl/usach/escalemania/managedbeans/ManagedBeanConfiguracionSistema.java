/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;

import cl.usach.escalemania.entities.ConfiguracionMail;
import cl.usach.escalemania.entities.ParametroSistema;
import cl.usach.escalemania.entities.Usuario;
import cl.usach.escalemania.sessionbeans.AlertaFacadeLocal;
import cl.usach.escalemania.sessionbeans.ConfiguracionMailFacadeLocal;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ParametroSistemaFacadeLocal;
import cl.usach.escalemania.sessionbeans.UsuarioFacadeLocal;
import java.util.ArrayList;
import java.util.Arrays;
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
    @EJB
    private AlertaFacadeLocal alertaFacade;
    
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
    private ConfiguracionMail configuracionMailSeleccionado;
    private String nombreConfiguracionMail;
    private String valorConfiguracionMail;
    private int alertasUsuario;
    private int alertasTotal;
    private String correoAsociado;
    private ParametroSistema setearEstado;
    private ParametroSistema simulacionMenusal;
    private ParametroSistema mailSimulacion;
    private ParametroSistema mailInstitución;
    private ParametroSistema contraseñaInstitucion;
    private ParametroSistema frecuanciaRevisionDocumentos;
    private ParametroSistema mailNotificacion;
    private String mes;
    private String dia;
    private List<String> dias;
    private List<String> diasSimulacion;
    private String nuevoMailInstitucion;
    private String nuevoPassIntitucion;
    private String nuevoMailNotificaion;
    
    
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
                usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
                usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
                configuracionMails=configuracionMailFacade.findAll();
                cargarParametros();
        }
    }
    
    public void cargarParametros(){
        setearEstado=parametroSistemaFacade.obtenerParametroPorNombre("Setear Estado");
        dia=setearEstado.getValor().split("-")[0];
        mes=setearEstado.getValor().split("-")[1];
        cargarDias();
        simulacionMenusal=parametroSistemaFacade.obtenerParametroPorNombre("Simulacion Mensual");
        mailSimulacion=parametroSistemaFacade.obtenerParametroPorNombre("Mail Simulacion");
        mailInstitución=parametroSistemaFacade.obtenerParametroPorNombre("mail Institucion");
        contraseñaInstitucion=parametroSistemaFacade.obtenerParametroPorNombre("Contraseña Institucion");
        frecuanciaRevisionDocumentos=parametroSistemaFacade.obtenerParametroPorNombre("Frecuencia revision documentos");
        mailNotificacion=parametroSistemaFacade.obtenerParametroPorNombre("Mail notificacion");
        nuevoMailInstitucion="";
        nuevoPassIntitucion="";
        nuevoMailNotificaion="";
        diasSimulacion=new ArrayList<>(Arrays.asList("01","02","03","04", "05", "06","07","08", "09","10","11","12","13","14","15","16","17","18","19","19","20","21","22","23","24","25","26","27","28"));
    }
    
    public void cargarDias(){
        dias=new ArrayList<>(Arrays.asList("01","02","03","04", "05", "06","07","08", "09","10","11","12","13","14","15","16","17","18","19","19","20","21","22","23","24","25","26","27","28"));
        if(mes.compareTo("02")!=0){
            dias.add("29");
            dias.add("30");
            if(mes.compareTo("04")!=0 && mes.compareTo("06")!=0 && mes.compareTo("09")!=0 && mes.compareTo("11")!=0)
                dias.add("31");
        }
        System.out.println("Cargue diassssss");
    }
    
    public void irReestablecer(){
        nombreUsuarioReestablecer=usuarioSeleccionado.getUsuario();
    }
    
    public void reestablecerContraseña(){
        String resultado=usuarioFacade.reestablecerContraseña(nombreUsuarioReestablecer);
        validarActualizar(resultado, "Contraseña reestablecida correctamente", "reestablecerPass");
    }
    
    public void irEliminar(){
        nombreUsuarioEliminar=usuarioSeleccionado.getUsuario();
    }
    
    public void eliminarUsuario(){
        String resultado=usuarioFacade.eliminarUsuario(nombreUsuarioEliminar);
        validarActualizar(resultado, "Usuario eliminado correctamente", "eliminarUser");
    }
    
    public void irCrear(){
        nombreUsuarioCrear="";
        correoAsociado="";
    }
    
    public void crearUsuario(){
        String resultado=usuarioFacade.crearUsuario(nombreUsuarioCrear,correoAsociado);
        validarActualizar(resultado, "Usuario creado existosamente", "crearUser");
    }
    
    public void irCambiarContraseña(){
        contraseñaActual="";
        contraseña1="";
        contraseña2="";
    }
    
    public void cambiarContraseña(){
        String resultado=usuarioFacade.cambiarContraseña(usuario, contraseñaActual, contraseña1, contraseña2);
        validarActualizar(resultado, "Contraseña cambiada exitosamente", "cambiarPass");
    }

    public void irAsociarCorreo(){
        correoUsuario=usuarioFacade.obtenerUsuario(usuario).getCorreo();
    }
    
    public void asociarCorreo(){
        String resultado=usuarioFacade.asociarCorreo(usuario, correoUsuario);
        validarActualizar(resultado, "Correo asociado exitosamente a su cuenta", "asociarMail");
    }
    
    public void irCambiarVisitante(){
        contraseñaVisitante1="";
        contraseñaVisitante2="";
    }
    
    public void cambiarContraseñaVisitante(){
        String resultado=usuarioFacade.cambiarContraseñaVisitante(contraseñaVisitante1, contraseñaVisitante2);
        validarActualizar(resultado, "La contraseña del usuario Visitante fue modificada correctamente", "cambiarPassV");
    }
    
    public void editarParametroSetearEstado(){
        String resultado=parametroSistemaFacade.modificarParametro(setearEstado.getTipoParametro(), dia+"-"+mes);
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSSE");
    }
    
    public void editarParametroSimulacionMensual(){
        String resultado=parametroSistemaFacade.modificarParametro(simulacionMenusal.getTipoParametro(), simulacionMenusal.getValor());
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSSM");
    }
    
    public void editarParametroMailSimulacion(){
        String resultado=parametroSistemaFacade.modificarParametro(mailSimulacion.getTipoParametro(), mailSimulacion.getValor());
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSMS");
    }
    
    public void editarParametroMailInstitucion(){
        String resultado=parametroSistemaFacade.modificarParametro(mailInstitución.getTipoParametro(), nuevoMailInstitucion);
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSMI");
    }
    
    public void editarParametroPassInstitucion(){
        String resultado=parametroSistemaFacade.modificarParametro(contraseñaInstitucion.getTipoParametro(), nuevoPassIntitucion);
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSPI");
    }
    
    public void editarParametroFrecuanciaRevision(){
        String resultado=parametroSistemaFacade.modificarParametro(frecuanciaRevisionDocumentos.getTipoParametro(), frecuanciaRevisionDocumentos.getValor());
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSFRD");
    }
    
    public void editarParametroMailNotificacion(){
        String resultado=parametroSistemaFacade.modificarParametro(mailNotificacion.getTipoParametro(), nuevoMailNotificaion);
        validarActualizar(resultado, "Cambios realizados exitosamente", "PSMN");
    }
    
    public void irEditarConfiguracionMail(){
        nombreConfiguracionMail=configuracionMailSeleccionado.getTipo();
        valorConfiguracionMail=configuracionMailSeleccionado.getValor();
    }
    
    public void editarConfiguracionMail(){
        String resultado=parametroSistemaFacade.modificarParametro(nombreConfiguracionMail,valorConfiguracionMail);
        validarActualizar(resultado, "Cambios realizados exitosamente", "editarCM");
    }
    
        
    public void validarActualizar(String resultado, String msgAprobacion, String dialog){
        if(resultado.compareTo(msgAprobacion)==0){
            alertas = documentoFacade.obtenerAlertas(documentoFacade.findAll());
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
            usuarios=usuarioFacade.findAll();
            usuarios.remove(usuarioFacade.obtenerUsuario("admin"));
            usuarios.remove(usuarioFacade.obtenerUsuario("Visitante"));
            configuracionMails=configuracionMailFacade.findAll();
            cargarParametros();
            RequestContext.getCurrentInstance().execute("PF('"+dialog+"').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
        }else{
            cargarParametros();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", resultado));
        }
            
    }

    public void irModificarMailInstitucion(){
        nuevoMailInstitucion=mailInstitución.getValor();
    }
    
    public void irModificarPassInstitucion(){
        nuevoPassIntitucion=contraseñaInstitucion.getValor();
    }
    
    public void irModificarMailNotificaicon(){
        nuevoMailNotificaion=mailNotificacion.getValor();
    }

    public String getNuevoMailInstitucion() {
        return nuevoMailInstitucion;
    }

    public String getNuevoMailNotificaion() {
        return nuevoMailNotificaion;
    }

    public void setNuevoMailNotificaion(String nuevoMailNotificaion) {
        this.nuevoMailNotificaion = nuevoMailNotificaion;
    }

    public void setNuevoMailInstitucion(String nuevoMailInstitucion) {
        this.nuevoMailInstitucion = nuevoMailInstitucion;
    }

    public String getNuevoPassIntitucion() {
        return nuevoPassIntitucion;
    }

    public void setNuevoPassIntitucion(String nuevoPassIntitucion) {
        this.nuevoPassIntitucion = nuevoPassIntitucion;
    }
    
    public ParametroSistema getSetearEstado() {
        return setearEstado;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List<String> getDiasSimulacion() {
        return diasSimulacion;
    }

    public void setDiasSimulacion(List<String> diasSimulacion) {
        this.diasSimulacion = diasSimulacion;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setSetearEstado(ParametroSistema setearEstado) {
        this.setearEstado = setearEstado;
    }

    public ParametroSistema getSimulacionMenusal() {
        return simulacionMenusal;
    }

    public void setSimulacionMenusal(ParametroSistema simulacionMenusal) {
        this.simulacionMenusal = simulacionMenusal;
    }

    public ParametroSistema getMailSimulacion() {
        return mailSimulacion;
    }

    public void setMailSimulacion(ParametroSistema mailSimulacion) {
        this.mailSimulacion = mailSimulacion;
    }

    public ParametroSistema getMailInstitución() {
        return mailInstitución;
    }

    public void setMailInstitución(ParametroSistema mailInstitución) {
        this.mailInstitución = mailInstitución;
    }

    public ParametroSistema getContraseñaInstitucion() {
        return contraseñaInstitucion;
    }

    public void setContraseñaInstitucion(ParametroSistema contraseñaInstitucion) {
        this.contraseñaInstitucion = contraseñaInstitucion;
    }

    public ParametroSistema getMailNotificacion() {
        return mailNotificacion;
    }

    public ParametroSistema getFrecuanciaRevisionDocumentos() {
        return frecuanciaRevisionDocumentos;
    }

    public void setFrecuanciaRevisionDocumentos(ParametroSistema frecuanciaRevisionDocumentos) {
        this.frecuanciaRevisionDocumentos = frecuanciaRevisionDocumentos;
    }

    public void setMailNotificacion(ParametroSistema mailNotificacion) {
        this.mailNotificacion = mailNotificacion;
    }

    public List<ConfiguracionMail> getConfiguracionMails() {
        return configuracionMails;
    }

    public void setConfiguracionMails(List<ConfiguracionMail> configuracionMails) {
        this.configuracionMails = configuracionMails;
    }

    public String getCorreoAsociado() {
        return correoAsociado;
    }

    public void setCorreoAsociado(String correoAsociado) {
        this.correoAsociado = correoAsociado;
    }

    public int getAlertasUsuario() {
        return alertasUsuario;
    }

    public void setAlertasUsuario(int alertasUsuario) {
        this.alertasUsuario = alertasUsuario;
    }

    public int getAlertasTotal() {
        return alertasTotal;
    }

    public void setAlertasTotal(int alertasTotal) {
        this.alertasTotal = alertasTotal;
    }

    public ConfiguracionMail getConfiguracionMailSeleccionado() {
        return configuracionMailSeleccionado;
    }

    public void setConfiguracionMailSeleccionado(ConfiguracionMail configuracionMailSeleccionado) {
        this.configuracionMailSeleccionado = configuracionMailSeleccionado;
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
