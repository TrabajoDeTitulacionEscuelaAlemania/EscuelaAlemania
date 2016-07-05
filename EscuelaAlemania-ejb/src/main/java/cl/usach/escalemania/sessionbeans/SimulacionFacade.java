/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Simulacion;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
public class SimulacionFacade extends AbstractFacade<Simulacion> implements SimulacionFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @EJB
    private ProgramaFacadeLocal programaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SimulacionFacade() {
        super(Simulacion.class);
    }
    
    @Override
    public double notaSimulacion(String nombrePrograma){
        List<Documento> documentos=programaFacade.DocumentosPorPrograma(nombrePrograma);
        int docVital=0, docImportante=0, docNormal=0, puntajeTotal, puntajeReal=0;
        for(Documento doc:documentos){
            if(doc.getSeccion().getId()==1){
                docVital++;
                if(doc.getEstadoDocumento().getId()==1)
                    puntajeReal=puntajeReal+5;
            }else
                if(doc.getSeccion().getId()==2){
                    docImportante++;
                    if(doc.getEstadoDocumento().getId()==1)
                        puntajeReal=puntajeReal+3;
                }else
                    if(doc.getSeccion().getId()==3){
                        docNormal++;
                        if(doc.getEstadoDocumento().getId()==1)
                            puntajeReal++;
                    }
        }
        puntajeTotal=docVital*5+docImportante*3+docNormal;
        double resultado=Math.round(((puntajeReal*7.0)/puntajeTotal)*10.0)/10.0;
        if(resultado<1.0)
            return 1.0;
        return resultado;
    }
    
    @Override
    public double porcentajeSimulacion(String nombrePrograma){
        List<Documento> documentos=programaFacade.DocumentosPorPrograma(nombrePrograma);
        int docVital=0, docImportante=0, docNormal=0, puntajeTotal, puntajeReal=0;
        for(Documento doc:documentos){
            if(doc.getSeccion().getId()==1){
                docVital++;
                if(doc.getEstadoDocumento().getId()==1)
                    puntajeReal=puntajeReal+5;
            }else
                if(doc.getSeccion().getId()==2){
                    docImportante++;
                    if(doc.getEstadoDocumento().getId()==1)
                        puntajeReal=puntajeReal+3;
                }else
                    if(doc.getSeccion().getId()==3){
                        docNormal++;
                        if(doc.getEstadoDocumento().getId()==1)
                            puntajeReal++;
                    }
        }
        puntajeTotal=docVital*5+docImportante*3+docNormal;
        double resultado=Math.round(((puntajeReal*100.0)/puntajeTotal)*100.0)/100.0;
        return resultado;
    }
    
}
