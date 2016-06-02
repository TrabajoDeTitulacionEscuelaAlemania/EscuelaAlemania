/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.test;

import cl.usach.escalemania.sessionbeans.DocumentoFacadeLocal;
import fit.ColumnFixture;
import javax.ejb.EJB;

/**
 *
 * @author Desarrollo
 */
public class PruebasFixture extends ColumnFixture{
    
    @EJB
    private DocumentoFacadeLocal documentoFacade;
    
    public int numero;
    
    public int suma(){
        return documentoFacade.count();
    }
    
}
