/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "rh_puesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhPuesto.findAll", query = "SELECT r FROM RhPuesto r"),
    @NamedQuery(name = "RhPuesto.findByIdPuesto", query = "SELECT r FROM RhPuesto r WHERE r.idPuesto = :idPuesto"),
    @NamedQuery(name = "RhPuesto.findByNombre", query = "SELECT r FROM RhPuesto r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RhPuesto.findByInfo", query = "SELECT r FROM RhPuesto r WHERE r.info = :info")})
public class RhPuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_puesto")
    private Long idPuesto;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "info")
    private String info;
    @OneToMany(mappedBy = "puesto")
    private List<Empleado> empleadoList;

    public RhPuesto() {
    }

    public RhPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPuesto != null ? idPuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RhPuesto)) {
            return false;
        }
        RhPuesto other = (RhPuesto) object;
        if ((this.idPuesto == null && other.idPuesto != null) || (this.idPuesto != null && !this.idPuesto.equals(other.idPuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.RhPuesto[ idPuesto=" + idPuesto + " ]";
    }
    
}
