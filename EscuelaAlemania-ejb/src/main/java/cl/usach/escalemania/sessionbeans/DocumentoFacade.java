/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.EstadoDocumento;
import cl.usach.escalemania.entities.Seccion;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }


    @Override
    public Documento obtenerDocumentoPorId(List<Documento> documentos, String idDocumento) {
        for(Documento d:documentos){
            if(d.getId().compareTo(new Long(Integer.parseInt(idDocumento)))==0)
                return d;
        }
        return null;
    }

    @Override
    public String editarDocumento(EstadoDocumento estadoDocumento, String ubicacion, Seccion seccion, String observacion, Documento documento) {
        if(ubicacion.isEmpty())
            return "Campo vacio";
        documento.setEstadoDocumento(estadoDocumento);
        documento.setObservacion(observacion);
        documento.setSeccion(seccion);
        documento.setUbicacion(ubicacion);
        documento.setFechaModificacion(new Date());
        edit(documento);
        return "OK";
    }

    @Override
    public List<Documento> obtenerDocumentoPorEstado(EstadoDocumento estadoDocumento) {
        Query query=em.createNamedQuery("Documento.findByEstado").setParameter("estadoDocumento", estadoDocumento);
        List<Documento> documentos=query.getResultList();
        if(documentos.isEmpty())
            return null;
        else
            return documentos;
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
    
}
