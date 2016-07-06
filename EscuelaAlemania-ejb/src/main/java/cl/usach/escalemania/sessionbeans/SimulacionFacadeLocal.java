/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Simulacion;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Rodrigo Rivas
 */
@Remote
public interface SimulacionFacadeLocal {

    void create(Simulacion simulacion);

    void edit(Simulacion simulacion);

    void remove(Simulacion simulacion);

    Simulacion find(Object id);

    List<Simulacion> findAll();

    List<Simulacion> findRange(int[] range);

    int count();
    
    double notaSimulacion(String nombrePrograma);
    
    double porcentajeSimulacion(String nombrePrograma);
    
    Simulacion crearSimulacion(String nombrePrograma, List<Programa> programas);
    
    Simulacion ultimaSimulacion(String nombrePrograma, List<Programa> programas);
    
}
