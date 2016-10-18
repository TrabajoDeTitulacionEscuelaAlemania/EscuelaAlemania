/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;



import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Seccion;
import cl.usach.escalemania.sessionbeans.AlertaFacadeLocal;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
import cl.usach.escalemania.sessionbeans.SeccionFacadeLocal;
import java.util.ArrayList;
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
 * @author Desarrollo
 */
@ManagedBean
@SessionScoped
public class ManagedBeanGestionDocs {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    @EJB
    private SeccionFacadeLocal seccionFacade;
    @EJB
    private AlertaFacadeLocal alertaFacade;
    
    
    private String rol;
    private String usuario;
    private List<String> categoriaSeleccionada;
    
   
    
    private List<Documento> documentosTotales;
    private List<Documento> documentos;
    private List<Documento> documentosFiltrados;
    
    private List<Documento> documentosPrograma;
    private List<Documento> documentosSeccion;
    private List<Documento> documentosEstado;

    private List<EstadoDocumento> estadoDocumentos;
    private List<Programa> programas;
    private List<Seccion> secciones;
    
    private Documento documentoElegido;
    private String nombreEstadoDocumento;
    private String ubicacion;
    private String nombreSeccion;
    private String observacion;
    private int completos;
    private int incompletos;
    private int desactualizados;
    private int sinInformacion;
    private int alertas;
    private int total;
    private String msg;
    private String tipoNombre;
    private String nombreDocumento;
    private int alertasUsuario;
    private int alertasTotal;

    public void init(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> sesisonMap = fc.getExternalContext().getSessionMap();
        usuario = (String) sesisonMap.get("usuario");
        rol = (String) sesisonMap.get("rol");
        if (usuario==null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        } else if (!FacesContext.getCurrentInstance().isPostback()) {
            estadoDocumentos = estadoDocumentoFacade.findAll();
            secciones = seccionFacade.findAll();
            documentos = documentoFacade.findAll();
            tipoNombre = "Todos";
            alertas = documentoFacade.obtenerAlertas(documentos);
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
            documentosFiltrados=null;
        }
    }
    
    public void initPrograma(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null)
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        else{
            if (!FacesContext.getCurrentInstance().isPostback()){
            programas=programaFacade.findAll();
            estadoDocumentos=estadoDocumentoFacade.findAll();
            secciones=seccionFacade.findAll();
                tipoNombre="Por programa";
                categoriaSeleccionada=null;
                documentos=null;
                documentosTotales=documentoFacade.findAll();
                alertas = documentoFacade.obtenerAlertas(documentosTotales);
                alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
                alertasTotal=alertas+alertasUsuario;
                documentosFiltrados=null;
                }
        }
    }
    
    public void initSeccion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> sesisonMap = fc.getExternalContext().getSessionMap();
        usuario = (String) sesisonMap.get("usuario");
        rol = (String) sesisonMap.get("rol");
        if (usuario==null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        } else if (!FacesContext.getCurrentInstance().isPostback()) {
            estadoDocumentos = estadoDocumentoFacade.findAll();
            secciones = seccionFacade.findAll();
            tipoNombre = "Por sección";
            categoriaSeleccionada = null;
            documentos = null;
            documentosTotales = documentoFacade.findAll();
            alertas = documentoFacade.obtenerAlertas(documentosTotales);
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
            documentosFiltrados=null;
        }
    }
    
    public void initEstado(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("rol");
        if(usuario==null)
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        else{
            if (!FacesContext.getCurrentInstance().isPostback()){
            estadoDocumentos=estadoDocumentoFacade.findAll();
            secciones=seccionFacade.findAll();
                tipoNombre="Por estado";
                categoriaSeleccionada=null;
                documentos=null;
                documentosTotales=documentoFacade.findAll();
                alertas = documentoFacade.obtenerAlertas(documentosTotales);
                alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
                alertasTotal=alertas+alertasUsuario;
                documentosFiltrados=null;
            }
        }
    }
    
    public void filtrarPorPrograma(){
        documentosPrograma=new ArrayList<>();
        if(!categoriaSeleccionada.isEmpty()){
            for(String programa: categoriaSeleccionada){
                documentosPrograma.addAll(documentoFacade.filtrarPorPrograma(documentosTotales, programa));
            }
            documentosPrograma=documentoFacade.eliminarDuplicados(documentosPrograma);
            documentos=documentosPrograma;
        }else
            documentos=null;
         documentosFiltrados=null;
    }
    
    public void filtrarPorEstado() {
        documentosEstado = new ArrayList<>();
        if (!categoriaSeleccionada.isEmpty()) {
            for (String estado : categoriaSeleccionada) {
                documentosEstado.addAll(documentoFacade.filtrarPorEstado(documentosTotales, estado));
            }
            documentosEstado = documentoFacade.eliminarDuplicados(documentosEstado);
            documentos=documentosEstado;
        }else
            documentos=null;
         documentosFiltrados=null;
    }

    public void filtrarPorSeccion() {
        documentosSeccion = new ArrayList<>();
        if (!categoriaSeleccionada.isEmpty()) {
            for (String seccion : categoriaSeleccionada) {
                documentosSeccion.addAll(documentoFacade.filtrarPorSeccion(documentosTotales, seccion));
            }
            documentosSeccion = documentoFacade.eliminarDuplicados(documentosSeccion);
            documentos=documentosSeccion;
        }else
            documentos=null;
         documentosFiltrados=null;
    }
  
    public void editar(){
        msg=documentoFacade.editarDocumento(estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, nombreEstadoDocumento), 
                ubicacion, 
                seccionFacade.obtenerPorNombre(nombreSeccion, secciones), 
                observacion, 
                nombreDocumento,
                documentoElegido,
                usuario);
        if(msg.compareToIgnoreCase("Cambios realizados correctamente")==0){
            
            if(tipoNombre.compareTo("Todos")==0){
                    documentos=documentoFacade.findAll();
                    documentosTotales=documentos;
            }
            if(tipoNombre.compareTo("Por programa")==0){
                documentosTotales=documentoFacade.findAll();
                filtrarPorPrograma();
            }
            if(tipoNombre.compareTo("Por estado")==0){
                documentosTotales=documentoFacade.findAll();
                filtrarPorEstado();
            }
            if(tipoNombre.compareTo("Por sección")==0){
                documentosTotales=documentoFacade.findAll();
                filtrarPorSeccion();
            }
            alertas = documentoFacade.obtenerAlertas(documentosTotales);
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
            RequestContext.getCurrentInstance().execute("PF('docDialogo').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", msg));
        }else
            if(msg.compareTo("Error al modificar")==0)
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                        msg, "Ocurrió un error al modificar el documento. Inténtelo nuevamente")); 
            else
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", msg));
    }
    
    
    public void irAEditar(){
        nombreDocumento=documentoElegido.getNombre();
        nombreEstadoDocumento=documentoElegido.getEstadoDocumento().getEstado();
        ubicacion=documentoElegido.getUbicacion();
        nombreSeccion=documentoElegido.getSeccion().getSeccion();
        observacion=documentoElegido.getObservacion();
        
    }
    
    public void eliminarDocumento(){
        String resultado=documentoFacade.eliminarDocumento(documentoElegido);
        if(resultado.compareTo("Documento eliminado exitosamente")==0){
            if(tipoNombre.compareTo("Todos")==0){
                    documentos=documentoFacade.findAll();
                    documentosTotales=documentos;
            }
            if(tipoNombre.compareTo("Por programa")==0){
                documentosTotales=documentoFacade.findAll();
                filtrarPorPrograma();
            }
            if(tipoNombre.compareTo("Por estado")==0){
                documentosTotales=documentoFacade.findAll();
                filtrarPorEstado();
            }
            if(tipoNombre.compareTo("Por sección")==0){
                documentosTotales=documentoFacade.findAll();
                filtrarPorSeccion();
            }
            alertas = documentoFacade.obtenerAlertas(documentosTotales);
            alertasUsuario=alertaFacade.obtenerAlertas(usuario).size();
            alertasTotal=alertas+alertasUsuario;
            RequestContext.getCurrentInstance().execute("PF('docDialogo').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", resultado));
            
        }else
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información", resultado));
         documentosFiltrados=null;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
    
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(List<String> categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public List<Documento> getDocumentosFiltrados() {
        return documentosFiltrados;
    }

    public void setDocumentosFiltrados(List<Documento> documentosFiltrados) {
        this.documentosFiltrados = documentosFiltrados;
    }

    public List<EstadoDocumento> getEstadoDocumentos() {
        return estadoDocumentos;
    }

    public void setEstadoDocumentos(List<EstadoDocumento> estadoDocumentos) {
        this.estadoDocumentos = estadoDocumentos;
    }

    public List<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(List<Programa> programas) {
        this.programas = programas;
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public Documento getDocumentoElegido() {
        return documentoElegido;
    }

    public void setDocumentoElegido(Documento documentoElegido) {
        this.documentoElegido = documentoElegido;
    }

    public String getNombreEstadoDocumento() {
        return nombreEstadoDocumento;
    }

    public void setNombreEstadoDocumento(String nombreEstadoDocumento) {
        this.nombreEstadoDocumento = nombreEstadoDocumento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getCompletos() {
        return completos;
    }

    public void setCompletos(int completos) {
        this.completos = completos;
    }

    public int getIncompletos() {
        return incompletos;
    }

    public void setIncompletos(int incompletos) {
        this.incompletos = incompletos;
    }

    public int getDesactualizados() {
        return desactualizados;
    }

    public void setDesactualizados(int desactualizados) {
        this.desactualizados = desactualizados;
    }

    public int getSinInformacion() {
        return sinInformacion;
    }

    public void setSinInformacion(int sinInformacion) {
        this.sinInformacion = sinInformacion;
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }

    
    public ManagedBeanGestionDocs() {
    }
   
}
