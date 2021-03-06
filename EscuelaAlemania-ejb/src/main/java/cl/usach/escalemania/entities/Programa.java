/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author Desarrollo
 */
@Table(name = "programa")
@NamedQueries({
    @NamedQuery(name="Programa.findByid",
                query="SELECT c FROM Programa c WHERE c.id = :id"),
    @NamedQuery(name="Programa.findByName",
                query="SELECT c FROM Programa c WHERE c.programa = :programa"),
})
@Entity
public class Programa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 100)
    private String programa;
    @ManyToMany(mappedBy = "programas", fetch=FetchType.EAGER)
    private List<Documento> documentos;
    @OneToMany(mappedBy = "programa", fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<Simulacion> simulaciones;

    public List<Simulacion> getSimulaciones() {
        return simulaciones;
    }

    public void setSimulaciones(List<Simulacion> simulaciones) {
        this.simulaciones = simulaciones;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
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
        if (!(object instanceof Programa)) {
            return false;
        }
        Programa other = (Programa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.usach.escalemania.entities.Programa[ id=" + id + " ]";
    }
    
}
