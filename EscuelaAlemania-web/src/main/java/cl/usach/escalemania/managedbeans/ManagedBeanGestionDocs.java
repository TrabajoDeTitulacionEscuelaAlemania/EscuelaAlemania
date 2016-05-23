/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.managedbeans;



import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.EstadoDocumentoFacadeLocal;
import cl.usach.escalemania.sessionbeans.ProgramaFacadeLocal;
import java.util.ArrayList;
import java.util.List;
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
public class ManagedBeanGestionDocs {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    
    private List<Documento> documentos;
    private List<Documento> documentosPIE;
    private List<Documento> documentosSEP;
    private List<Documento> documentosIntegral;
    private List<Documento> documentosDirector;
    private FacesContext fc;
    private String rol;
    private String usuario;
    private List<Documento> documentosFiltrados;
    private List<Documento> documentosFiltradosPIE;
    private List<Documento> documentosFiltradosSEP;
    private List<Documento> documentosFiltradosIntegral;
    private List<Documento> documentosFiltradosDirector;
    private Documento documentoElegido;
    private List<EstadoDocumento> estadoDocumentos;
    private String nombreEstado;
    

    public EstadoDocumentoFacadeLocal getEstadoDocumentoFacade() {
        return estadoDocumentoFacade;
    }

    public void setEstadoDocumentoFacade(EstadoDocumentoFacadeLocal estadoDocumentoFacade) {
        this.estadoDocumentoFacade = estadoDocumentoFacade;
    }


    public List<EstadoDocumento> getEstadoDocumentos() {
        return estadoDocumentos;
    }

    public void setEstadoDocumentos(List<EstadoDocumento> estadoDocumentos) {
        this.estadoDocumentos = estadoDocumentos;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public ProgramaFacadeLocal getProgramaFacade() {
        return programaFacade;
    }

    public void setProgramaFacade(ProgramaFacadeLocal programaFacade) {
        this.programaFacade = programaFacade;
    }

    public Documento getDocumentoElegido() {
        return documentoElegido;
    }

    public void setDocumentoElegido(Documento documentoElegido) {
        this.documentoElegido = documentoElegido;
    }

    public List<Documento> getDocumentosFiltradosPIE() {
        return documentosFiltradosPIE;
    }

    public void setDocumentosFiltradosPIE(List<Documento> documentosFiltradosPIE) {
        this.documentosFiltradosPIE = documentosFiltradosPIE;
    }

    public List<Documento> getDocumentosFiltradosSEP() {
        return documentosFiltradosSEP;
    }

    public void setDocumentosFiltradosSEP(List<Documento> documentosFiltradosSEP) {
        this.documentosFiltradosSEP = documentosFiltradosSEP;
    }

    public List<Documento> getDocumentosFiltradosIntegral() {
        return documentosFiltradosIntegral;
    }

    public void setDocumentosFiltradosIntegral(List<Documento> documentosFiltradosIntegral) {
        this.documentosFiltradosIntegral = documentosFiltradosIntegral;
    }

    public List<Documento> getDocumentosFiltradosDirector() {
        return documentosFiltradosDirector;
    }

    public void setDocumentosFiltradosDirector(List<Documento> documentosFiltradosDirector) {
        this.documentosFiltradosDirector = documentosFiltradosDirector;
    }


    
    public List<Documento> getDocumentosPIE() {
        return documentosPIE;
    }

    public void setDocumentosPIE(List<Documento> documentosPIE) {
        this.documentosPIE = documentosPIE;
    }

    public List<Documento> getDocumentosSEP() {
        return documentosSEP;
    }

    public void setDocumentosSEP(List<Documento> documentosSEP) {
        this.documentosSEP = documentosSEP;
    }

    public List<Documento> getDocumentosIntegral() {
        return documentosIntegral;
    }

    public void setDocumentosIntegral(List<Documento> documentosIntegral) {
        this.documentosIntegral = documentosIntegral;
    }

    public List<Documento> getDocumentosDirector() {
        return documentosDirector;
    }

    public void setDocumentosDirector(List<Documento> documentosDirector) {
        this.documentosDirector = documentosDirector;
    }



    public DocumentoFacadeLocal getDocumentoFacade() {
        return documentoFacade;
    }

    public void setDocumentoFacade(DocumentoFacadeLocal documentoFacade) {
        this.documentoFacade = documentoFacade;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public FacesContext getFc() {
        return fc;
    }

    public void setFc(FacesContext fc) {
        this.fc = fc;
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

    public List<Documento> getDocumentosFiltrados() {
        return documentosFiltrados;
    }

    public void setDocumentosFiltrados(List<Documento> documentosFiltrados) {
        this.documentosFiltrados = documentosFiltrados;
    }

    
    public ManagedBeanGestionDocs() {
    }
    
    public void init(){
        fc=FacesContext.getCurrentInstance();
        Map<String,Object> sesisonMap=fc.getExternalContext().getSessionMap();
        usuario=(String)sesisonMap.get("usuario");
        rol=(String)sesisonMap.get("usuario");
        if(usuario==null){
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        }else{
            documentos=documentoFacade.findAll();
            documentosDirector=programaFacade.DocumentosPorPrograma("Procedimiento de traspaso de establecimientos educacionales a nuevos directores");
            documentosIntegral=programaFacade.DocumentosPorPrograma("Revision integral");
            documentosPIE=programaFacade.DocumentosPorPrograma("Programa de Integracion Escolar");
            documentosSEP=programaFacade.DocumentosPorPrograma("Subvencion Escolar Preferencial");
            estadoDocumentos=estadoDocumentoFacade.findAll();
            
        }
    }
    
    public void editar(){
        if(documentoFacade.editarDocumento(estadoDocumentoFacade.obtenerEstadDocumentoPorId(estadoDocumentos, nombreEstado), documentoElegido))
            recargar();
            
    }
    
    public void recargar(){
        documentos=documentoFacade.findAll();
            documentosDirector=programaFacade.DocumentosPorPrograma("Procedimiento de traspaso de establecimientos educacionales a nuevos directores");
            documentosIntegral=programaFacade.DocumentosPorPrograma("Revision integral");
            documentosPIE=programaFacade.DocumentosPorPrograma("Programa de Integracion Escolar");
            documentosSEP=programaFacade.DocumentosPorPrograma("Subvencion Escolar Preferencial");
             fc=FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/gestion_documentos.xhtml?faces-redirect=true");
    }
}
