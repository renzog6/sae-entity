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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "usr_grupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsrGrupo.findAll", query = "SELECT u FROM UsrGrupo u"),
    @NamedQuery(name = "UsrGrupo.findByIdGrupo", query = "SELECT u FROM UsrGrupo u WHERE u.idGrupo = :idGrupo"),
    @NamedQuery(name = "UsrGrupo.findByNombre", query = "SELECT u FROM UsrGrupo u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "UsrGrupo.findByCode", query = "SELECT u FROM UsrGrupo u WHERE u.code = :code"),
    @NamedQuery(name = "UsrGrupo.findByInfo", query = "SELECT u FROM UsrGrupo u WHERE u.info = :info")})
public class UsrGrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_grupo")
    private Long idGrupo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "code")
    private Integer code;
    @Column(name = "info")
    private String info;
    @OneToMany(mappedBy = "grupo")
    private List<Usuario> usuarioList;

    public UsrGrupo() {
    }

    public UsrGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
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
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupo != null ? idGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsrGrupo)) {
            return false;
        }
        UsrGrupo other = (UsrGrupo) object;
        if ((this.idGrupo == null && other.idGrupo != null) || (this.idGrupo != null && !this.idGrupo.equals(other.idGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.UsrGrupo[ idGrupo=" + idGrupo + " ]";
    }
    
}
