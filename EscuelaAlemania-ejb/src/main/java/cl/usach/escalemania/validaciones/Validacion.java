/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.validaciones;

import cl.usach.escalemania.entities.Usuario;
import cl.usach.escalemania.sessionbeans.UsuarioFacadeLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validation;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class Validacion implements ValidacionLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    
    
    
    @Override
    public String login(String usuario, String contraseña) {
        
        Query query=em.createNamedQuery("Usuario.findByName").setParameter("usuario", usuario);
        if(query.getResultList().isEmpty())
            return "Usuario no existe";
        else{
            Usuario user=(Usuario)query.getSingleResult();
            contraseña=passwordHash(contraseña);
            //System.out.println(contraseña);
            if(user.getContraseña().compareTo(contraseña)!=0)
                return "Contraseña invalida";
            return user.getRol().getRol();
        }
            
    }
    
    @Override
    public String passwordHash(String password) {
        
    	
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            
            byte byteData[] = md.digest();
            
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
           
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    
}
