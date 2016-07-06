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
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    @EJB
    private DocumentoFacadeLocal documentoFacade;

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
    
    @Override
    public Simulacion crearSimulacion(String nombrePrograma, List<Programa> programas){
        Programa programa=programaFacade.obtenerProgramaPorNombre(programas, nombrePrograma);
        double nota=notaSimulacion(nombrePrograma);
        double porcentaje=porcentajeSimulacion(nombrePrograma);
        Simulacion simulacion=new Simulacion();
        simulacion.setFecha(new Date());
        simulacion.setNota(nota);
        simulacion.setPorcentajeAprobacion(porcentaje);
        simulacion.setPrograma(programa);
        List<Documento> documentos=programaFacade.DocumentosPorPrograma(nombrePrograma);
        simulacion.setDocCompletos(documentoFacade.filtrarPorEstado(documentos, "Completo").size());
        simulacion.setDocDesactualizados(documentoFacade.filtrarPorEstado(documentos, "Desactualizado").size());
        simulacion.setDocIncompletos(documentoFacade.filtrarPorEstado(documentos, "Incompleto").size());
        simulacion.setDocSinInformacion(documentoFacade.filtrarPorEstado(documentos, "Sin informacion").size());
        create(simulacion);
        return simulacion;
    }
    
    @Override
    public Simulacion ultimaSimulacion(String nombrePrograma, List<Programa> programas){
        Programa programa=programaFacade.obtenerProgramaPorNombre(programas, nombrePrograma);
        int ultimo=programa.getSimulaciones().size();
        if(ultimo==0)
            return null;
        Simulacion simulacion=programa.getSimulaciones().get(ultimo-1);
        return simulacion;
    }
    
}
