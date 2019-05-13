/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "cer_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CerStock.findAll", query = "SELECT c FROM CerStock c"),
    @NamedQuery(name = "CerStock.findByIdStock", query = "SELECT c FROM CerStock c WHERE c.idStock = :idStock"),
    @NamedQuery(name = "CerStock.findByCampania", query = "SELECT c FROM CerStock c WHERE c.campania = :campania"),
    @NamedQuery(name = "CerStock.findByEspecie", query = "SELECT c FROM CerStock c WHERE c.especie = :especie"),
    @NamedQuery(name = "CerStock.findByFecha", query = "SELECT c FROM CerStock c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CerStock.findByMetro", query = "SELECT c FROM CerStock c WHERE c.metro = :metro"),
    @NamedQuery(name = "CerStock.findBySilo", query = "SELECT c FROM CerStock c WHERE c.silo = :silo"),
    @NamedQuery(name = "CerStock.findByTonelada", query = "SELECT c FROM CerStock c WHERE c.tonelada = :tonelada")})
public class CerStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_stock")
    private Long idStock;
    @Column(name = "campania")
    private String campania;
    @Column(name = "especie")
    private String especie;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "metro")
    private Double metro;
    @Column(name = "silo")
    private String silo;
    @Column(name = "tonelada")
    private Double tonelada;

    public CerStock() {
    }

    public CerStock(Long idStock) {
        this.idStock = idStock;
    }

    public Long getIdStock() {
        return idStock;
    }

    public void setIdStock(Long idStock) {
        this.idStock = idStock;
    }

    public String getCampania() {
        return campania;
    }

    public void setCampania(String campania) {
        this.campania = campania;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getMetro() {
        return metro;
    }

    public void setMetro(Double metro) {
        this.metro = metro;
    }

    public String getSilo() {
        return silo;
    }

    public void setSilo(String silo) {
        this.silo = silo;
    }

    public Double getTonelada() {
        return tonelada;
    }

    public void setTonelada(Double tonelada) {
        this.tonelada = tonelada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStock != null ? idStock.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CerStock)) {
            return false;
        }
        CerStock other = (CerStock) object;
        if ((this.idStock == null && other.idStock != null) || (this.idStock != null && !this.idStock.equals(other.idStock))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.CerStock[ idStock=" + idStock + " ]";
    }
    
}
