/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 *
 * @author Desarrollo
 */

@NamedQueries({
    @NamedQuery(name="Documento.findByName",
                query="SELECT c FROM Documento c WHERE c.nombre = :nombre"),
    @NamedQuery(name="Documento.findByEstado",
                query="SELECT c FROM Documento c WHERE c.estadoDocumento = :estadoDocumento")
})
@Entity
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 300)
    private String nombre;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaModificacion;
    @Column(nullable = false, length = 100)
    private String ubicacion;
    @Column(nullable = true, length = 120)
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne
    private EstadoDocumento estadoDocumento;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Seccion seccion;
    @ManyToMany( fetch=FetchType.EAGER)
    private List<Programa> programas;
    
    private String fechaModificacionFormateada;

    public String getFechaModificacionFormateada() {
        fechaModificacionFormateada=new SimpleDateFormat("yyyy-MM-dd").format(fechaModificacion);
        return fechaModificacionFormateada;
    }

    public void setFechaModificacionFormateada(String fechaModificacionFormateada) {
        this.fechaModificacionFormateada = fechaModificacionFormateada;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }
    
 
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoDocumento getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(EstadoDocumento estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public List<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(List<Programa> programas) {
        this.programas = programas;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.usach.escalemania.entities.Documento[ id=" + id + " ]";
    }
    
}
