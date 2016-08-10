/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.ConfiguracionMail;
import cl.usach.escalemania.entities.ParametroSistema;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB
    private ConfiguracionMailFacadeLocal configuracionMailFacade;
    
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
    
     @Override
    public ParametroSistema obtenerParametroPorNombre(String nombreParametro) {
        Query query=em.createNamedQuery("ParametroSistema.findByTipoParametro").setParameter("tipoParametro", nombreParametro);
        if(query.getResultList().isEmpty())
            return null;
        else
            return (ParametroSistema)query.getSingleResult();
    }

    @Override
    public String modificarParametro(String nombreParametro, String valorParametro) {
        ParametroSistema parametroSistema=obtenerParametroPorNombre(nombreParametro);
        if(parametroSistema!=null){
            if(parametroSistema.getTipoParametro().compareTo("Mail Simulacion")==0){
                parametroSistema.setValor(valorParametro);
            }else{
                if(valorParametro.isEmpty())
                    return "El parametro seleccionado no puede tener un valor vacio";
                parametroSistema.setValor(valorParametro);
            }
            try {
                edit(parametroSistema);
                return "Cambios realizados exitosamente";
            } catch (Exception e) {
                return "Ocurrió un error inesperado al modificar el parametro. Por favor, inténtelo mas tarde";
            }
        }
        ConfiguracionMail configuracionMail=configuracionMailFacade.obtenerConfiguracionMailPorNombre(nombreParametro);
        if(valorParametro.isEmpty())
            return "El parametro seleccionado no puede tener un valor vacio";
        configuracionMail.setValor(valorParametro);
        try {
            configuracionMailFacade.edit(configuracionMail);
            return "Cambios realizados exitosamente";
        } catch (Exception e) {
            return "Ocurrió un error inesperado al modificar el parametro. Por favor, inténtelo mas tarde";
        }
    }

}
