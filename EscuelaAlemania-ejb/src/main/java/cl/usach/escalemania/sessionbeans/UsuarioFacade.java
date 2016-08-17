/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Usuario;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @EJB
    private ValidacionLocal validacion;
    @EJB
    private RolFacadeLocal rolFacade;
    @EJB
    private AlertaFacadeLocal alertaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public String crearUsuario(String nuevoUsuario, String correoAsociado) {
        if(nuevoUsuario.isEmpty())
            return "El nombre de usuario no puede estar vacio";
        Usuario usuario=obtenerUsuario(nuevoUsuario);
        if(usuario!=null)
            return "El nombre de usuario ya existe";
        Usuario usuarioNuevo=new Usuario();
        usuarioNuevo.setUsuario(nuevoUsuario);
        usuarioNuevo.setRol(rolFacade.obtenerRolPorNombre("Administrador"));
        usuarioNuevo.setContraseña(validacion.passwordHash(nuevoUsuario));
        usuarioNuevo.setCorreo(correoAsociado);
        try {
            create(usuarioNuevo);
            return "Usuario creado existosamente";
        } catch (Exception e) {
            return "Error inesperado al crear usuario. Por favor, inténtelo mas tarde";
        }
    }
    
    @Override
    public Usuario obtenerUsuario(String nombreusuario){
        Query query=em.createNamedQuery("Usuario.findByName").setParameter("usuario", nombreusuario);
        if(query.getResultList().isEmpty())
            return null;
        return (Usuario)query.getSingleResult();
    }

    @Override
    public String cambiarContraseña(String nombreUsuario, String contraseñaActual, String nuevaContraseña1, String nuevaContraseña2) {
        if(nuevaContraseña1.isEmpty() || nuevaContraseña2.isEmpty())
            return "La nueva contraseña no puede estar vacia";
        String contraseña=validacion.passwordHash(contraseñaActual);
        String contraseña1=validacion.passwordHash(nuevaContraseña1);
        String contraseña2=validacion.passwordHash(nuevaContraseña2);
        if(contraseña1.compareTo(contraseña2)!=0)
            return "Las contraseñas nuevas no coinciden";
        Usuario usuario=obtenerUsuario(nombreUsuario);
        if(usuario.getContraseña().compareTo(contraseña)!=0)
            return "La contraseña actual no coincide";
        usuario.setContraseña(contraseña1);
        try {
            edit(usuario);
            return "Contraseña cambiada exitosamente";
        } catch (Exception e) {
            return "Error inesperado al modificar usuario. Por favor, inténtelo mas tarde";
        }
    }

    @Override
    public String reestablecerContraseña(String nombreUsuario) {
        Usuario usuario=obtenerUsuario(nombreUsuario);
        usuario.setContraseña(validacion.passwordHash(usuario.getUsuario()));
        try {
            edit(usuario);
            return "Contraseña reestablecida correctamente";
        } catch (Exception e) {
            return "Error inesperado al reestablecer contraseña del usuario. Por favor, inténtelo mas tarde";
        }
    }

    @Override
    public String eliminarUsuario(String nombreUsuario) {
        Usuario usuario=obtenerUsuario(nombreUsuario);
        try {
            if(!usuario.getAlertas().isEmpty()){
                alertaFacade.eliminarAlertasUsuario(usuario);
                usuario.setAlertas(null);
                edit(usuario);
            }
            remove(usuario);
            return "Usuario eliminado correctamente";
        } catch (Exception e) {
             return "Error inesperado al eliminar usuario. Por favor, inténtelo mas tarde";
        }
    }

    @Override
    public String cambiarContraseñaVisitante(String nuevaContraseña1, String nuevaContraseña2) {
        if(nuevaContraseña1.isEmpty() || nuevaContraseña2.isEmpty())
            return "La contraseña del Visitante no puede estar vacia";
        String contraseña1=validacion.passwordHash(nuevaContraseña1);
        String contraseña2=validacion.passwordHash(nuevaContraseña2);
        if(contraseña1.compareTo(contraseña2)!=0)
            return "La nuevas contraseñas no coinciden";
        Usuario visitante=obtenerUsuario("visitante");
        visitante.setContraseña(contraseña1);
        try {
            edit(visitante);
            return "La contraseña del usuario Visitante fue modificada correctamente";
        } catch (Exception e) {
            return "Error inesperado al modificar la clave del usuario. Por favor, inténtelo mas tarde";
        }
        
    }

    @Override
    public String asociarCorreo(String nombreUsuario, String correoUsuario) {
        Usuario usuario=obtenerUsuario(nombreUsuario);
        usuario.setCorreo(correoUsuario);
        try {
            edit(usuario);
            return "Correo asociado exitosamente a su cuenta";
        } catch (Exception e) {
            return "Error inesperado al asociar correo al usuario. Por favor, inténtelo mas tarde";
        }
    }
    
}
