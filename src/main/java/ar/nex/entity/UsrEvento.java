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
@Table(name = "usr_evento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsrEvento.findAll", query = "SELECT u FROM UsrEvento u"),
    @NamedQuery(name = "UsrEvento.findByIdEvento", query = "SELECT u FROM UsrEvento u WHERE u.idEvento = :idEvento"),
    @NamedQuery(name = "UsrEvento.findByNombre", query = "SELECT u FROM UsrEvento u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "UsrEvento.findByCode", query = "SELECT u FROM UsrEvento u WHERE u.code = :code"),
    @NamedQuery(name = "UsrEvento.findByInfo", query = "SELECT u FROM UsrEvento u WHERE u.info = :info")})
public class UsrEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_evento")
    private Long idEvento;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "code")
    private Integer code;
    @Column(name = "info")
    private String info;
    @OneToMany(mappedBy = "evento")
    private List<UsrHistorial> usrHistorialList;

    public UsrEvento() {
    }

    public UsrEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @XmlTransient
    public List<UsrHistorial> getUsrHistorialList() {
        return usrHistorialList;
    }

    public void setUsrHistorialList(List<UsrHistorial> usrHistorialList) {
        this.usrHistorialList = usrHistorialList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEvento != null ? idEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsrEvento)) {
            return false;
        }
        UsrEvento other = (UsrEvento) object;
        if ((this.idEvento == null && other.idEvento != null) || (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.UsrEvento[ idEvento=" + idEvento + " ]";
    }
    
}
