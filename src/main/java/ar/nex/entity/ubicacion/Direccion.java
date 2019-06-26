package ar.nex.entity.ubicacion;

import ar.nex.entity.Empresa;
import java.io.Serializable;
import java.util.List;
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

    @OneToMany(mappedBy = "direccion")
    private List<Coordenada> coordenadaList;

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
   
    @ManyToMany(mappedBy = "direccionList")
    private List<Empresa> empresaList;

    @JoinColumn(name = "localidad", referencedColumnName = "id_localidad")
    @ManyToOne
    private Localidad localidad;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDireccion != null ? idDireccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Direccion)) {
            return false;
        }
        Direccion other = (Direccion) object;
        if ((this.idDireccion == null && other.idDireccion != null) || (this.idDireccion != null && !this.idDireccion.equals(other.idDireccion))) {
            return false;
        }
        return true;
    }

    @Override    
    public String toString(){
        return this.nombre + ": " + this.calle + "  " + this.numero + " - " + getLocalidad().getNombre() + " (" + getLocalidad().getCodigoPostal() + ") - " + getLocalidad().getProvincia();
    }

}
