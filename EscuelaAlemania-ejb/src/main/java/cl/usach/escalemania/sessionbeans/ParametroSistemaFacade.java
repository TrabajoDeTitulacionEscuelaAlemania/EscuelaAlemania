/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.ParametroSistema;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Rodrigo Rivas
 */
@Stateless
public class ParametroSistemaFacade extends AbstractFacade<ParametroSistema> implements ParametroSistemaFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametroSistemaFacade() {
        super(ParametroSistema.class);
    }
    
    @Override
    public List<ParametroSistema> obtenerListaParametros(String tipoParametro){
        Query query=em.createNamedQuery("ParametroSistema.findByTipoParametro").setParameter("tipoParametro", tipoParametro);
        List<ParametroSistema> resultado=(List<ParametroSistema>)query.getResultList();
        return resultado;
    }
}
