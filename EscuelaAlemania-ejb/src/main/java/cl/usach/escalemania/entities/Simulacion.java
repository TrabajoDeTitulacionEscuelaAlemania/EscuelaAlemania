/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Rodrigo Rivas
 */
@Entity
public class Simulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    
    @Column(nullable = false)
    private double porcentajeAprobacion;
    
    @Column(nullable = false)
    private double nota;
    
    @JoinColumn(nullable = false)
    @ManyToOne
    private Programa programa;
    
    @Column(nullable = false)
    private int docCompletos;
    
    @Column(nullable = false)
    private int docIncompletos;
    
    @Column(nullable = false)
    private int docSinInformacion;
    
    @Column(nullable = false)
    private int docDesactualizados;

    public int getDocCompletos() {
        return docCompletos;
    }

    public void setDocCompletos(int docCompletos) {
        this.docCompletos = docCompletos;
    }

    public int getDocIncompletos() {
        return docIncompletos;
    }

    public void setDocIncompletos(int docIncompletos) {
        this.docIncompletos = docIncompletos;
    }

    public int getDocSinInformacion() {
        return docSinInformacion;
    }

    public void setDocSinInformacion(int docSinInformacion) {
        this.docSinInformacion = docSinInformacion;
    }

    public int getDocDesactualizados() {
        return docDesactualizados;
    }

    public void setDocDesactualizados(int docDesactualizados) {
        this.docDesactualizados = docDesactualizados;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }
    

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPorcentajeAprobacion() {
        return porcentajeAprobacion;
    }

    public void setPorcentajeAprobacion(double porcentajeAprobacion) {
        this.porcentajeAprobacion = porcentajeAprobacion;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
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
        if (!(object instanceof Simulacion)) {
            return false;
        }
        Simulacion other = (Simulacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.usach.escalemania.entities.Simulacion[ id=" + id + " ]";
    }
    
}
