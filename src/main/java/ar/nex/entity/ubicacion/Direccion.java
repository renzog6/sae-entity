package ar.nex.entity.ubicacion;

import ar.nex.entity.Empresa;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "ubi_direccion")
@XmlRootElement
public class Direccion implements Serializable {

    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_direccion")
    private Long idDireccion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "numero")
    private String numero;
    @Column(name = "calle")
    private String calle;
    @Column(name = "codigo")
    private String codigo;

    @ManyToMany(mappedBy = "direccionList")
    private List<Empresa> empresaList;

    @JoinColumn(name = "localidad", referencedColumnName = "id_localidad")
    @ManyToOne
    private Localidad localidad;

    @OneToMany(mappedBy = "direccion")
    private List<Coordenada> coordenadaList;
    
    public Direccion() {
    }

    public Direccion(Long idDireccion) {
        this.idDireccion = idDireccion;
    }

    public Long getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Long idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    @XmlTransient
    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    @XmlTransient
    public List<Coordenada> getCoordenadaList() {
        return coordenadaList;
    }

    public void setCoordenadaList(List<Coordenada> coordenadaList) {
        this.coordenadaList = coordenadaList;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.numero);
        hash = 67 * hash + Objects.hashCode(this.calle);
        hash = 67 * hash + Objects.hashCode(this.codigo);
        hash = 67 * hash + Objects.hashCode(this.localidad);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Direccion other = (Direccion) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.calle, other.calle)) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.localidad, other.localidad)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombre + ": " + this.calle + "  " + this.numero + " - " + getLocalidad().getNombre() + " (" + getLocalidad().getCodigoPostal() + ") - " + getLocalidad().getProvincia();
    }

}