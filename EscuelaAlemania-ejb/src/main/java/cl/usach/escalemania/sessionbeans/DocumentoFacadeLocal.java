/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Desarrollo
 */
@Local
public interface DocumentoFacadeLocal {

    void create(Documento documento);

    void edit(Documento documento);

    void remove(Documento documento);

    Documento find(Object id);

    List<Documento> findAll();

    List<Documento> findRange(int[] range);

    int count();

    boolean editarDocumento(EstadoDocumento estado, Documento documento);

    Documento obtenerDocumentoPorId(List<Documento> documentos, String idDocumento);
    
}
