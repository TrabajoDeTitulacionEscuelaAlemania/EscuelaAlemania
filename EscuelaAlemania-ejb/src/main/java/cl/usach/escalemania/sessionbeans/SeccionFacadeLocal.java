/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Seccion;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Desarrollo
 */
@Remote
public interface SeccionFacadeLocal {

    void create(Seccion seccion);

    void edit(Seccion seccion);

    void remove(Seccion seccion);

    Seccion find(Object id);

    List<Seccion> findAll();

    List<Seccion> findRange(int[] range);

    int count();

    Seccion obtenerPorNombre(String nombreSeccion, List<Seccion> secciones);
    
}
