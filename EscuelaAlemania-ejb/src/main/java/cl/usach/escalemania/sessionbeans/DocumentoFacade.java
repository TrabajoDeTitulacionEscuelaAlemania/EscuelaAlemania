/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Seccion;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> implements DocumentoFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @EJB
    private EstadoDocumentoFacadeLocal estadoDocumentoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }

    public boolean nombreExiste(String nombreDocumento){
        List<Documento> documentos=findAll();
        for(Documento doc: documentos)
            if(doc.getNombre().compareToIgnoreCase(nombreDocumento)==0)
                return false;
        return true;
    }
    
    @Override
    public String editarDocumento(EstadoDocumento estadoDocumento, String ubicacion, Seccion seccion, String observacion, String nombreDocumento, Documento documento) {
        try{
            if(!nombreExiste(nombreDocumento))
                return "Nombre existe";
        if(ubicacion.isEmpty())
            return "Campo vacio";
        documento.setNombre(nombreDocumento);
        documento.setEstadoDocumento(estadoDocumento);
        documento.setObservacion(observacion);
        documento.setSeccion(seccion);
        documento.setUbicacion(ubicacion);
        documento.setFechaModificacion(new Date());
        edit(documento);
        return "OK";
        }catch(Exception e){
            return "Error al modificar";
        }
    }

    @Override
    public List<Documento> buscarDocumento(String frase, List<Documento> documentos) {
        
        @SuppressWarnings("deprecation")
		SpanishAnalyzer analyzer = new SpanishAnalyzer(Version.LUCENE_4_10_1);
		//System.out.print(tokenizeString(analyzer, "certificado obra municipales"));
        List<String> palabras=tokenizeString(analyzer, frase);
        int cantidadPalabras=palabras.size();
        List<List<Documento>> resultados=new ArrayList<>();
        for(int i=0;i<cantidadPalabras;i++)
            resultados.add(new ArrayList<Documento>());
        
        List<String> textoAnalizar;
        int ocurrencia=0;
        
        for(Documento doc: documentos){
            textoAnalizar=tokenizeString(analyzer, doc.getNombre());
            for(String palabra: palabras){
                if(textoAnalizar.contains(palabra))
                    ocurrencia++;
            }
            if(ocurrencia!=0)
                resultados.get(ocurrencia-1).add(doc);
            ocurrencia=0;
        }
        List<Documento> resultadoDocumentos=new ArrayList<>();
        for(int i=cantidadPalabras-1;i>=0;i--)
            resultadoDocumentos.addAll(resultados.get(i));
        return resultadoDocumentos;
    }
    
     public List<String> tokenizeString(Analyzer analyzer, String tweet) {
		
		List<String> result = new ArrayList<String>();
		try {
			TokenStream stream = analyzer.tokenStream(null, new StringReader(
					tweet));
			stream.reset();
			while (stream.incrementToken()) {
				result.add(stream.getAttribute(CharTermAttribute.class)
						.toString());
			}
			stream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

    @Override
    public List<Documento> alertaDocumentos() {
        List<Documento> sinInformacion=estadoDocumentoFacade.obtenerDocumentoPorId("4");
        List<Documento> desactualizado=estadoDocumentoFacade.obtenerDocumentoPorId("3");
        List<Documento> incompleto=estadoDocumentoFacade.obtenerDocumentoPorId("2");
        List<Documento> alerta=new ArrayList<>();
        if(!sinInformacion.isEmpty())
            alerta.addAll(sinInformacion);
        if(!desactualizado.isEmpty())
            alerta.addAll(desactualizado);
        if(!incompleto.isEmpty())
            alerta.addAll(incompleto);
        return alerta;
    }

    @Override
    public List<Documento> filtrarPorPrograma(List<Documento> documentos, String nombreProggrama) {
        List<Documento> resultado=new ArrayList<>();
        for(Documento doc:documentos)
            for(Programa prog: doc.getProgramas())
                if(prog.getPrograma().compareToIgnoreCase(nombreProggrama)==0){
                    resultado.add(doc);
                    break;
                }
        return resultado;
    }

    @Override
    public List<Documento> eliminarDuplicados(List<Documento> documentos) {
        Map<Long,Documento> filtro=new HashMap<>();
        for(Documento doc: documentos)
            filtro.put(doc.getId(), doc);
        documentos.clear();
        documentos.addAll(filtro.values());
        return documentos;
    }

    @Override
    public List<Documento> filtrarPorEstado(List<Documento> documentos, String nombreEstado) {
        List<Documento> resultado=new ArrayList<>();
        for(Documento doc:documentos)
            if(doc.getEstadoDocumento().getEstado().compareToIgnoreCase(nombreEstado)==0)
                resultado.add(doc);
        return resultado;
    }

    @Override
    public List<Documento> filtrarPorSeccion(List<Documento> documentos, String nombreSeccion) {
        List<Documento> resultado=new ArrayList<>();
        for(Documento doc:documentos)
            if(doc.getSeccion().getSeccion().compareToIgnoreCase(nombreSeccion)==0)
                resultado.add(doc);
        return resultado;
    }
     
    
}
