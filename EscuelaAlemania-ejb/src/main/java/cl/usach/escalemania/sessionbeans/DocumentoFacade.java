/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> implements DocumentoFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }

    @Override
    public boolean editarDocumento(EstadoDocumento estado, Documento documento) {
        documento.setEstadoDocumento(estado);
        documento.setFechaModificacion(new Date());
        try{
            edit(documento);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public Documento obtenerDocumentoPorId(List<Documento> documentos, String idDocumento) {
        for(Documento d:documentos){
            if(d.getId().compareTo(new Long(Integer.parseInt(idDocumento)))==0)
                return d;
        }
        return null;
    }
    
    
    
}
