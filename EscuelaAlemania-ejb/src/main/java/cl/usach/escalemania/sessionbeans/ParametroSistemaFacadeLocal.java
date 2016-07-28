/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.ParametroSistema;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Rodrigo Rivas
 */
@Local
public interface ParametroSistemaFacadeLocal {

    void create(ParametroSistema parametroSistema);

    void edit(ParametroSistema parametroSistema);

    void remove(ParametroSistema parametroSistema);

    ParametroSistema find(Object id);

    List<ParametroSistema> findAll();

    List<ParametroSistema> findRange(int[] range);

    int count();
    
    List<ParametroSistema> obtenerListaParametros(String tipoParametro);
    
}
