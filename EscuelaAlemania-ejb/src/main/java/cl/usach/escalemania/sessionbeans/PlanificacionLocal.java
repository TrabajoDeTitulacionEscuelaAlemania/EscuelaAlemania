/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import javax.ejb.Local;
import javax.ejb.Remote;


/**
 *
 * @author Desarrollo
 */
@Remote
public interface PlanificacionLocal {

    void setearEstadoDOcumentos();
    
    void realizarSimulacion();

    String revisionEstadoDocumentos();

    void notificarEstadoDocumenos();
    
}
