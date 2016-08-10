/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.ConfiguracionMail;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Rodrigo Rivas
 */
@Local
public interface ConfiguracionMailFacadeLocal {

    void create(ConfiguracionMail configuracionMail);

    void edit(ConfiguracionMail configuracionMail);

    void remove(ConfiguracionMail configuracionMail);

    ConfiguracionMail find(Object id);

    List<ConfiguracionMail> findAll();

    List<ConfiguracionMail> findRange(int[] range);

    int count();
    
    String enviarMail(List<String> destinos, String mensaje, String asunto);

    ConfiguracionMail obtenerConfiguracionMailPorNombre(String nombreParametro);
    
}
