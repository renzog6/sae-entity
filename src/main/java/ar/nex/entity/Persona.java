/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import ar.nex.entity.ubicacion.Direccion;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "rh_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findByIdPersona", query = "SELECT p FROM Persona p WHERE p.idPersona = :idPersona"),
    @NamedQuery(name = "Persona.findByNombre", query = "SELECT p FROM Persona p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Persona.findByApellido", query = "SELECT p FROM Persona p WHERE p.apellido = :apellido"),
    @NamedQuery(name = "Persona.findByNacimiento", query = "SELECT p FROM Persona p WHERE p.nacimiento = :nacimiento"),
    @NamedQuery(name = "Persona.findByDni", query = "SELECT p FROM Persona p WHERE p.dni = :dni"),
    @NamedQuery(name = "Persona.findByCuil", query = "SELECT p FROM Persona p WHERE p.cuil = :cuil"),
    @NamedQuery(name = "Persona.findBySexo", query = "SELECT p FROM Persona p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "Persona.findByEstado", query = "SELECT p FROM Persona p WHERE p.estado = :estado"),
    @NamedQuery(name = "Persona.findByEstadoCivil", query = "SELECT p FROM Persona p WHERE p.estadoCivil = :estadoCivil"),
    @NamedQuery(name = "Persona.findByInfo", query = "SELECT p FROM Persona p WHERE p.info = :info")})
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_persona")
    private Long idPersona;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "nacimiento")
    @Temporal(TemporalType.DATE)
    private Date nacimiento;
    @Column(name = "dni")
    private String dni;
    @Column(name = "cuil")
    private String cuil;
    @Column(name = "sexo")
    private String sexo;
    @Column(name = "estado")
    private String estado;
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "info")
    private String info;
    @ManyToMany(mappedBy = "personaList")
    private List<Contacto> contactoList;
    @OneToMany(mappedBy = "datos")
    private List<Familia> familiaList;
    @OneToMany(mappedBy = "persona")
    private List<Familia> familiaList1;
    @JoinColumn(name = "domicilio", referencedColumnName = "id_direccion")
    @ManyToOne
    private Direccion domicilio;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "persona")
    private Empleado empleado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "persona")
    private Usuario usuario;

    public Persona() {
    }

    public Persona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Persona(Long idPersona, String nombre, String apellido) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @XmlTransient
    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @XmlTransient
    public List<Familia> getFamiliaList() {
        return familiaList;
    }

    public void setFamiliaList(List<Familia> familiaList) {
        this.familiaList = familiaList;
    }

    @XmlTransient
    public List<Familia> getFamiliaList1() {
        return familiaList1;
    }

    public void setFamiliaList1(List<Familia> familiaList1) {
        this.familiaList1 = familiaList1;
    }

    public Direccion getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Direccion domicilio) {
        this.domicilio = domicilio;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Persona[ idPersona=" + idPersona + " ]";
    }
    
}
