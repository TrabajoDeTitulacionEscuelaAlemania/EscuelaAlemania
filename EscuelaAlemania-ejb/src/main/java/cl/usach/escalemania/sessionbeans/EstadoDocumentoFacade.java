/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class EstadoDocumentoFacade extends AbstractFacade<EstadoDocumento> implements EstadoDocumentoFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoDocumentoFacade() {
        super(EstadoDocumento.class);
    }

    @Override
    public EstadoDocumento obtenerEstadDocumentoPorNombre(List<EstadoDocumento> estadoDocumentos, String nombreEstado) {
        for(EstadoDocumento ed:estadoDocumentos)
            if(ed.getEstado().compareTo(nombreEstado)==0)
                return ed;
        return null;
    }

    @Override
    public List<Documento> obtenerDocumentoPorId(String idEstadoDocumento) {
        Query query=em.createNamedQuery("EstadoDocumento.findById").setParameter("id", new Integer(idEstadoDocumento));
        return ((EstadoDocumento)query.getSingleResult()).getDocumentos();
    }
    
    
}
