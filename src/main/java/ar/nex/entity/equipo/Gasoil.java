package ar.nex.entity.equipo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_gasoil")
@XmlRootElement
public class Gasoil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_gasoil")
    private Long idGasoil;
    @Column(name = "id_equipo")
    private BigInteger idEquipo;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "movimineto")
    private int movimineto;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "litros")
    private Double litros;
    @Column(name = "precio")
    private Double precio;    
    @Column(name = "stock")
    private Double stock;
    @Column(name = "info")
    private String info;

    public Gasoil() {
    }

    public Gasoil(Long idGasoil) {
        this.idGasoil = idGasoil;
    }

    public Long getIdGasoil() {
        return idGasoil;
    }

    public void setIdGasoil(Long idGasoil) {
        this.idGasoil = idGasoil;
    }

    public BigInteger getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(BigInteger idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getMovimineto() {
        return movimineto;
    }

    public void setMovimineto(int movimineto) {
        this.movimineto = movimineto;
    }

    public Double getLitros() {
        return litros;
    }

    public void setLitros(Double litros) {
        this.litros = litros;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGasoil != null ? idGasoil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gasoil)) {
            return false;
        }
        Gasoil other = (Gasoil) object;
        if ((this.idGasoil == null && other.idGasoil != null) || (this.idGasoil != null && !this.idGasoil.equals(other.idGasoil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.equipo.Gasoil[ idGasoil=" + idGasoil + " ]";
    }
    
}
