/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;


import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import java.util.List;
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
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    
    private String rol;
    private String usuario;
    private FacesContext fc;
    private List<EstadoDocumento> estadoDocumentos;
    private int alertas;
    private int completos;
    private int incompletos;
    private int desactualizados;
    private int sinInformacion;
    private int total;

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

    public FacesContext getFc() {
        return fc;
    }

    public void setFc(FacesContext fc) {
        this.fc = fc;
    }

    public int getAlertas() {
        return alertas;
    }

    public void setAlertas(int alertas) {
        this.alertas = alertas;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
            completos=estadoDocumentoFacade.obtenerDocumentoPorId("1").size();
            incompletos=estadoDocumentoFacade.obtenerDocumentoPorId("2").size();
            desactualizados=estadoDocumentoFacade.obtenerDocumentoPorId("3").size();
            sinInformacion=estadoDocumentoFacade.obtenerDocumentoPorId("4").size();
            alertas=incompletos+desactualizados+sinInformacion;
            total=alertas+completos;
        }
    }
    
    public void redirigir(String tipoDoc){
        fc=FacesContext.getCurrentInstance();
        fc.getExternalContext().getSessionMap().put("tipoDoc", tipoDoc);
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/gestion_documentos.xhtml?faces-redirect=true");
    }
}
