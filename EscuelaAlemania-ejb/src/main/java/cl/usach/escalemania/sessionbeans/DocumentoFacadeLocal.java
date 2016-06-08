/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Seccion;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

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


    Documento obtenerDocumentoPorId(List<Documento> documentos, String idDocumento);

    String editarDocumento(EstadoDocumento estadoDocumento, String ubicacion, Seccion seccion, String observacion, Documento documento);

    List<Documento> obtenerDocumentoPorEstado(EstadoDocumento estadoDocumento);

    List<Documento> buscarDocumento(String frase, List<Documento> documentos);

    List<Documento> alertaDocumentos();
    
}
