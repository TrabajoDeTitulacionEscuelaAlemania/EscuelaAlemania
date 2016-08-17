/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Alerta;
import cl.usach.escalemania.entities.Usuario;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Rodrigo Rivas
 */
@Remote
public interface AlertaFacadeLocal {

    void create(Alerta alerta);

    void edit(Alerta alerta);

    void remove(Alerta alerta);

    Alerta find(Object id);

    List<Alerta> findAll();

    List<Alerta> findRange(int[] range);

    int count();

    String enviarAlerta(String mensajeAlerta, List<String> destinosAlerta);

    List<Alerta> obtenerAlertas(String nombreUsuario);

    String marcarLiedo(Alerta alerta);

    void eliminarAlertasUsuario(Usuario usuario);
    
}
