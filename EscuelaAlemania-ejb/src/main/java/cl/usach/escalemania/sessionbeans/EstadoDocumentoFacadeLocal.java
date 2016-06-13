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
import javax.ejb.Remote;

/**
 *
 * @author Desarrollo
 */
@Remote
public interface EstadoDocumentoFacadeLocal {

    void create(EstadoDocumento estadoDocumento);

    void edit(EstadoDocumento estadoDocumento);

    void remove(EstadoDocumento estadoDocumento);

    EstadoDocumento find(Object id);

    List<EstadoDocumento> findAll();

    List<EstadoDocumento> findRange(int[] range);

    int count();

    EstadoDocumento obtenerEstadDocumentoPorNombre(List<EstadoDocumento> estadoDocumentos, String idEstadoDocumento);

    List<Documento> obtenerDocumentoPorId(String idEstadoDocumento);
    
}
