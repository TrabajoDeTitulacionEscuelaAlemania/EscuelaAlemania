/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import javax.ejb.Local;

/**
 *
 * @author Desarrollo
 */
@Local
public interface ValidacionLocal {

    String login(String usuario, String contraseña);
    
    String passwordHash(String password);
    
}
