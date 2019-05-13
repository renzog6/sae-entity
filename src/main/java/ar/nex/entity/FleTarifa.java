/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "fle_tarifa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FleTarifa.findAll", query = "SELECT f FROM FleTarifa f"),
    @NamedQuery(name = "FleTarifa.findByIdTarifa", query = "SELECT f FROM FleTarifa f WHERE f.idTarifa = :idTarifa"),
    @NamedQuery(name = "FleTarifa.findByEspecies", query = "SELECT f FROM FleTarifa f WHERE f.especies = :especies"),
    @NamedQuery(name = "FleTarifa.findByEstado", query = "SELECT f FROM FleTarifa f WHERE f.estado = :estado"),
    @NamedQuery(name = "FleTarifa.findByFFin", query = "SELECT f FROM FleTarifa f WHERE f.fFin = :fFin"),
    @NamedQuery(name = "FleTarifa.findByFInicio", query = "SELECT f FROM FleTarifa f WHERE f.fInicio = :fInicio"),
    @NamedQuery(name = "FleTarifa.findByIdDestino", query = "SELECT f FROM FleTarifa f WHERE f.idDestino = :idDestino"),
    @NamedQuery(name = "FleTarifa.findByIdOrigen", query = "SELECT f FROM FleTarifa f WHERE f.idOrigen = :idOrigen"),
    @NamedQuery(name = "FleTarifa.findByKms", query = "SELECT f FROM FleTarifa f WHERE f.kms = :kms")})
public class FleTarifa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tarifa")
    private Long idTarifa;
    @Column(name = "especies")
    private String especies;
    @Column(name = "estado")
    private String estado;
    @Column(name = "f_fin")
    private String fFin;
    @Column(name = "f_inicio")
    private String fInicio;
    @Column(name = "id_destino")
    private String idDestino;
    @Column(name = "id_origen")
    private String idOrigen;
    @Column(name = "kms")
    private String kms;

    public FleTarifa() {
    }

    public FleTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getEspecies() {
        return especies;
    }

    public void setEspecies(String especies) {
        this.especies = especies;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFFin() {
        return fFin;
    }

    public void setFFin(String fFin) {
        this.fFin = fFin;
    }

    public String getFInicio() {
        return fInicio;
    }

    public void setFInicio(String fInicio) {
        this.fInicio = fInicio;
    }

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTarifa != null ? idTarifa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FleTarifa)) {
            return false;
        }
        FleTarifa other = (FleTarifa) object;
        if ((this.idTarifa == null && other.idTarifa != null) || (this.idTarifa != null && !this.idTarifa.equals(other.idTarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.FleTarifa[ idTarifa=" + idTarifa + " ]";
    }
    
}
