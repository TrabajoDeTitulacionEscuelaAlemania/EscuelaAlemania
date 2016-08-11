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
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Rodrigo Rivas
 */
@Table(name = "simulacion")
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
    private int docCompletosVital;
    
    @Column(nullable = false)
    private int docIncompletosVital;
    
    @Column(nullable = false)
    private int docSinInformacionVital;
    
    @Column(nullable = false)
    private int docDesactualizadosVital;
    
    @Column(nullable = false)
    private int docCompletosImportante;
    
    @Column(nullable = false)
    private int docIncompletosImportante;
    
    @Column(nullable = false)
    private int docSinInformacionImportante;
    
    @Column(nullable = false)
    private int docDesactualizadosImportante;
    
    @Column(nullable = false)
    private int docCompletosNormal;
    
    @Column(nullable = false)
    private int docIncompletosNormal;
    
    @Column(nullable = false)
    private int docSinInformacionNormal;
    
    @Column(nullable = false)
    private int docDesactualizadosNormal;

    public int getDocCompletosVital() {
        return docCompletosVital;
    }

    public void setDocCompletosVital(int docCompletosVital) {
        this.docCompletosVital = docCompletosVital;
    }

    public int getDocIncompletosVital() {
        return docIncompletosVital;
    }

    public void setDocIncompletosVital(int docIncompletosVital) {
        this.docIncompletosVital = docIncompletosVital;
    }

    public int getDocSinInformacionVital() {
        return docSinInformacionVital;
    }

    public void setDocSinInformacionVital(int docSinInformacionVital) {
        this.docSinInformacionVital = docSinInformacionVital;
    }

    public int getDocDesactualizadosVital() {
        return docDesactualizadosVital;
    }

    public void setDocDesactualizadosVital(int docDesactualizadosVital) {
        this.docDesactualizadosVital = docDesactualizadosVital;
    }

    public int getDocCompletosImportante() {
        return docCompletosImportante;
    }

    public void setDocCompletosImportante(int docCompletosImportante) {
        this.docCompletosImportante = docCompletosImportante;
    }

    public int getDocIncompletosImportante() {
        return docIncompletosImportante;
    }

    public void setDocIncompletosImportante(int docIncompletosImportante) {
        this.docIncompletosImportante = docIncompletosImportante;
    }

    public int getDocSinInformacionImportante() {
        return docSinInformacionImportante;
    }

    public void setDocSinInformacionImportante(int docSinInformacionImportante) {
        this.docSinInformacionImportante = docSinInformacionImportante;
    }

    public int getDocDesactualizadosImportante() {
        return docDesactualizadosImportante;
    }

    public void setDocDesactualizadosImportante(int docDesactualizadosImportante) {
        this.docDesactualizadosImportante = docDesactualizadosImportante;
    }

    public int getDocCompletosNormal() {
        return docCompletosNormal;
    }

    public void setDocCompletosNormal(int docCompletosNormal) {
        this.docCompletosNormal = docCompletosNormal;
    }

    public int getDocIncompletosNormal() {
        return docIncompletosNormal;
    }

    public void setDocIncompletosNormal(int docIncompletosNormal) {
        this.docIncompletosNormal = docIncompletosNormal;
    }

    public int getDocSinInformacionNormal() {
        return docSinInformacionNormal;
    }

    public void setDocSinInformacionNormal(int docSinInformacionNormal) {
        this.docSinInformacionNormal = docSinInformacionNormal;
    }

    public int getDocDesactualizadosNormal() {
        return docDesactualizadosNormal;
    }

    public void setDocDesactualizadosNormal(int docDesactualizadosNormal) {
        this.docDesactualizadosNormal = docDesactualizadosNormal;
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
