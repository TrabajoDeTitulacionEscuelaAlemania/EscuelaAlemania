/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class Planificacion implements PlanificacionLocal {

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;
    
    @Schedule(second = "1",minute = "1",hour = "11",dayOfMonth = "15",month = "2,7", year = "*")
    @Override
    public void setearEstadoDOcumentos() {
        List<Documento> documentos=documentoFacade.findAll();
        List<EstadoDocumento> estadoDocumentos=estadoDocumentoFacade.findAll();
        EstadoDocumento estadoDocumento=estadoDocumentoFacade.obtenerEstadDocumentoPorNombre(estadoDocumentos, "Sin informacion");
        for(Documento documento: documentos){
            documento.setEstadoDocumento(estadoDocumento);
            documentoFacade.edit(documento);
        }
        
    }

    
}
