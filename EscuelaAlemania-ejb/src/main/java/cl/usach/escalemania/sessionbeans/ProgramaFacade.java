/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.Programa;
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
public class ProgramaFacade extends AbstractFacade<Programa> implements ProgramaFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramaFacade() {
        super(Programa.class);
    }

    @Override
    public List<Documento> DocumentosPorPrograma(String programa) {
        Query query=em.createNamedQuery("Programa.findByName").setParameter("programa", programa);
        Programa programaObject=(Programa) query.getSingleResult();
        return programaObject.getDocumentos();
    }

    @Override
    public Programa obtenerProgramaPorNombre(List<Programa> programas, String nombrePrograma) {
        for(Programa prog:programas)
            if(prog.getPrograma().compareToIgnoreCase(nombrePrograma)==0)
                return prog;
        return null;
    }
     
    
}
