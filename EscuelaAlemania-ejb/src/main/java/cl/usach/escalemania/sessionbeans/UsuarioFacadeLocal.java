/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Usuario;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Desarrollo
 */
@Remote
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    String crearUsuario(String nuevoUsuario, String correoAsociado);

    String cambiarContraseña(String nombreUsuario, String contraseñaActual, String nuevaContraseña1, String nuevaContraseña2);

    String reestablecerContraseña(String nombreUsuario);

    String eliminarUsuario(String nombreUsuario);

    String cambiarContraseñaVisitante(String nuevaContraseña1, String nuevaContraseña2);

    String asociarCorreo(String nombreUsuario, String correoUsuario);
    
    Usuario obtenerUsuario(String nombreusuario);
    
}
