/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity.equipo;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "transporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transporte.findAll", query = "SELECT t FROM Transporte t"),
    @NamedQuery(name = "Transporte.findByIdTransporte", query = "SELECT t FROM Transporte t WHERE t.idTransporte = :idTransporte"),
    @NamedQuery(name = "Transporte.findByIdChofer", query = "SELECT t FROM Transporte t WHERE t.idChofer = :idChofer"),
    @NamedQuery(name = "Transporte.findByNombre", query = "SELECT t FROM Transporte t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Transporte.findByGasoil", query = "SELECT t FROM Transporte t WHERE t.gasoil = :gasoil")})
public class Transporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_transporte")
    private Long idTransporte;
    @Column(name = "id_chofer")
    private BigInteger idChofer;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "gasoil")
    private Boolean gasoil;

    public Transporte() {
    }

    public Transporte(Long idTransporte) {
        this.idTransporte = idTransporte;
    }

    public Long getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(Long idTransporte) {
        this.idTransporte = idTransporte;
    }

    public BigInteger getIdChofer() {
        return idChofer;
    }

    public void setIdChofer(BigInteger idChofer) {
        this.idChofer = idChofer;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getGasoil() {
        return gasoil;
    }

    public void setGasoil(Boolean gasoil) {
        this.gasoil = gasoil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransporte != null ? idTransporte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transporte)) {
            return false;
        }
        Transporte other = (Transporte) object;
        if ((this.idTransporte == null && other.idTransporte != null) || (this.idTransporte != null && !this.idTransporte.equals(other.idTransporte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.equipo.Transporte[ idTransporte=" + idTransporte + " ]";
    }
    
}
