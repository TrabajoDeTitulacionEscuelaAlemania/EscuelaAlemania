/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.Documento;
import cl.usach.escalemania.entities.Programa;
import cl.usach.escalemania.entities.Simulacion;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Desarrollo
 */
@Stateless
public class ProgramaFacade extends AbstractFacade<Programa> implements ProgramaFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private DocumentoFacadeLocal documentoFacade;
    @EJB
    private SimulacionFacadeLocal simulacionFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramaFacade() {
        super(Programa.class);
    }

    @Override
    public List<Documento> DocumentosPorPrograma(String programa) {
        Query query = em.createNamedQuery("Programa.findByName").setParameter("programa", programa);
        Programa resultado = (Programa) query.getSingleResult();
        return resultado.getDocumentos();
    }

    @Override
    public Programa obtenerProgramaPorNombre(List<Programa> programas, String nombrePrograma) {
        for (Programa prog : programas) {
            if (prog.getPrograma().compareToIgnoreCase(nombrePrograma) == 0) {
                return prog;
            }
        }
        return null;
    }

    @Override
    public List<Programa> obtenerListaDeProgramas(List<String> programa, List<Programa> programas) {
        List<Programa> resultado = new ArrayList<>();
        for (String p : programa) {
            resultado.add(obtenerProgramaPorNombre(programas, p));
        }
        return resultado;
    }

    @Override
    public String crearPrograma(String nombrePrograma) {
        if(nombrePrograma.isEmpty())
            return "El nombre del programa no puede ser vacío";
        Programa programa = obtenerProgramaPorNombre(findAll(), nombrePrograma);
        if (programa != null) {
            return "El programa que intenta crear ya existe";
        }
        Programa nuevoPrograma = new Programa();
        nuevoPrograma.setPrograma(nombrePrograma);
        try {
            create(nuevoPrograma);
            return "Programa creado existosamente";
        } catch (Exception e) {
            return "Error inesperado al crear el programa. Por favor, inténtelo nuevamente";
        }
    }

    @Override
    public String editarPrograma(String nombrePrograma, String nuevoNombrePrograma) {
        if(nuevoNombrePrograma.isEmpty())
            return "El nombre del programa no puede ser vacío";
        Programa programa = obtenerProgramaPorNombre(findAll(), nuevoNombrePrograma);
        if (programa != null) {
            return "El nombre del programa ya está en uso";
        }
        Programa programaModificado = obtenerProgramaPorNombre(findAll(), nombrePrograma);
        programaModificado.setPrograma(nuevoNombrePrograma);
        try {
            edit(programaModificado);
            return "Nombre del programa modificado existosamente";
        } catch (Exception e) {
            return "Error inesperado al modificar el programa. Por favor, inténtelo nuevamente";
        }
    }

    @Override
    public String eliminarPrograma(String nombrePrograma, boolean moverDocumentos, String programaDestino) {
        Programa programaEliminar = obtenerProgramaPorNombre(findAll(), nombrePrograma);
        if (moverDocumentos) {
            Programa programaGuardar = obtenerProgramaPorNombre(findAll(), programaDestino);
            try {
                guardarDocumentos(programaEliminar, programaGuardar);
                eliminarSimulacionPrograma(programaEliminar);
                remove(programaEliminar);
                return "El programa fue elimando existosamente y sus documentos fueron movidos al programa " + programaDestino;
            } catch (Exception e) {
                System.out.println("Error al eliminar programa: "+e.getMessage());
                return "Error inesperado al eliminar el programa. Por favor, inténtelo nuevamente";
            }
        }
        try {
            eliminarDocumentosPrograma(programaEliminar);
            eliminarSimulacionPrograma(programaEliminar);
            remove(programaEliminar);
            return "El programa fue elimando existosamente junto a sus documentos";
        } catch (Exception e) {
            return "Error inesperado al eliminar el programa. Por favor, inténtelo nuevamente";
        }
    }

    public void guardarDocumentos(Programa programaOrigen, Programa programaDestino) {
        List<Programa> programasAux;
        List<Documento> documentosOrigen = programaOrigen.getDocumentos();
        boolean existe = false;
        for (Documento doc : documentosOrigen) {
            for (Programa prog : doc.getProgramas()) {
                if (prog.getPrograma().compareToIgnoreCase(programaDestino.getPrograma()) == 0) {
                    existe = true;
                    break;
                }
            }
            programasAux = doc.getProgramas();
            programasAux.remove(programaOrigen);
            if (!existe) {
                programasAux.add(programaDestino);
            }
            doc.setProgramas(programasAux);
            documentoFacade.edit(doc);
            existe = false;
        }
    }

    public void eliminarSimulacionPrograma(Programa programa) {
        for (Simulacion simulacion : programa.getSimulaciones()) {
            simulacionFacade.remove(simulacion);
        }
        programa.setSimulaciones(null);
        edit(programa);
    }

    public void eliminarDocumentosPrograma(Programa programa) {
        List<Programa> programasAux;
        for (Documento doc : programa.getDocumentos()) {
            if (doc.getProgramas().size() == 1) {
                documentoFacade.eliminarDocumento(doc);
            } else {
                programasAux = doc.getProgramas();
                programasAux.remove(programa);
                doc.setProgramas(programasAux);
                documentoFacade.edit(doc);
            }
        }
        programa.setDocumentos(null);
        edit(programa);
    }
}
