/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.Programa;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Desarrollo
 */
@Remote
public interface ProgramaFacadeLocal {

    void create(Programa programa);

    void edit(Programa programa);

    void remove(Programa programa);

    Programa find(Object id);

    List<Programa> findAll();

    List<Programa> findRange(int[] range);

    int count();

    List<Documento> DocumentosPorPrograma(String programa);

    Programa obtenerProgramaPorNombre(List<Programa> programas, String nombrePrograma);
    
    List<Programa> obtenerListaDeProgramas(List<String> programa, List<Programa> programas);

    String crearPrograma(String nombrePrograma);

    String editarPrograma(String nombrePrograma, String nuevoNombrePrograma);

    String eliminarPrograma(String nombrePrograma, boolean moverDocumentos, String programaDestino);
    
}
