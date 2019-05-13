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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "usr_historial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsrHistorial.findAll", query = "SELECT u FROM UsrHistorial u"),
    @NamedQuery(name = "UsrHistorial.findByIdHistorial", query = "SELECT u FROM UsrHistorial u WHERE u.idHistorial = :idHistorial"),
    @NamedQuery(name = "UsrHistorial.findByFecha", query = "SELECT u FROM UsrHistorial u WHERE u.fecha = :fecha"),
    @NamedQuery(name = "UsrHistorial.findByDetalle", query = "SELECT u FROM UsrHistorial u WHERE u.detalle = :detalle"),
    @NamedQuery(name = "UsrHistorial.findByInfo", query = "SELECT u FROM UsrHistorial u WHERE u.info = :info")})
public class UsrHistorial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_historial")
    private Long idHistorial;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "info")
    private String info;
    @JoinColumn(name = "evento", referencedColumnName = "id_evento")
    @ManyToOne
    private UsrEvento evento;
    @JoinColumn(name = "usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuario;

    public UsrHistorial() {
    }

    public UsrHistorial(Long idHistorial) {
        this.idHistorial = idHistorial;
    }

    public UsrHistorial(Long idHistorial, Date fecha) {
        this.idHistorial = idHistorial;
        this.fecha = fecha;
    }

    public Long getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Long idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public UsrEvento getEvento() {
        return evento;
    }

    public void setEvento(UsrEvento evento) {
        this.evento = evento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsrHistorial)) {
            return false;
        }
        UsrHistorial other = (UsrHistorial) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.UsrHistorial[ idHistorial=" + idHistorial + " ]";
    }
    
}
