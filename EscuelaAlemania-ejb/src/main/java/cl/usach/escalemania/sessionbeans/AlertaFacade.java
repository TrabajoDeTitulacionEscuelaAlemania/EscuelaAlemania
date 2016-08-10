/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Alerta;
import cl.usach.escalemania.entities.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rodrigo Rivas
 */
@Stateless
public class AlertaFacade extends AbstractFacade<Alerta> implements AlertaFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private ConfiguracionMailFacadeLocal configuracionMailFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertaFacade() {
        super(Alerta.class);
    }

    @Override
    public String enviarAlerta(String mensajeAlerta, List<String> destinosAlerta) {
        if(mensajeAlerta.isEmpty())
            return "El mensaje de la alerta no puede estar vacio";
        if(destinosAlerta.isEmpty())
            return "La alerta se debe enviar a al menos un usuario";
        List<Usuario> usuarios=usuarioFacade.findAll();
        List<Usuario> usuariosDestino=new ArrayList<>();
        for(Usuario user:usuarios)
            for(String destino:destinosAlerta)
                if(user.getUsuario().compareTo(destino)==0)
                    usuariosDestino.add(user);
        Alerta alerta;
        List<String> correosDestino=new ArrayList<>();
        String correo;
        for(Usuario user:usuariosDestino ){
            alerta=new Alerta();
            alerta.setLeido(false);
            alerta.setMensaje(mensajeAlerta);
            alerta.setUsuario(user);
            try {
                create(alerta);
            } catch (Exception e) {
                return "Error inseperado al crear las alertas. Por favor, inténtelo mas tarde";
            }
            correo=user.getCorreo();
            if(correo!=null)
                if(!correo.isEmpty())
                    correosDestino.add(correo);
        }
        if(correosDestino.isEmpty())
            return "El/los usuarios fueron alertados correctamente";
        configuracionMailFacade.enviarMail(correosDestino, "Se ha realizado una alerta a usted. Por favor, ingrese al sistema para obtener mas detalles de esta.\nSaludos.", "Alerta Sistema de Gestion de Documentos");
        return "El/los usuarios fueron alertados correctamente";
    }
}
