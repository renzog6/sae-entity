package ar.nex.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_equipo")
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_equipo")
    private Long idEquipo;
    
    @Column(name = "anio")
    private String anio;
    @Column(name = "chasis")
    private String chasis;
    @Column(name = "motor")
    private String motor;
    @Column(name = "patente")
    private String patente;
    @Column(name = "color")
    private String color;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "otro")
    private String otro;
    
    @JoinTable(name = "ped_repuesto_equipo", joinColumns = {
        @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo")}, inverseJoinColumns = {
        @JoinColumn(name = "id_repuesto", referencedColumnName = "id_repuesto")})
    @ManyToMany
    private List<Repuesto> repuestoList;
    
    @OneToMany(mappedBy = "equipo")
    private List<RepuestoStockDetalle> repuestoStockDetalleList;
    
    @JoinColumn(name = "categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private EquipoCategoria categoria;
    @JoinColumn(name = "compra_venta", referencedColumnName = "id_compra_venta")
    @ManyToOne
    private EquipoCompraVenta compraVenta;
    @JoinColumn(name = "empresa", referencedColumnName = "id_empresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "marca", referencedColumnName = "id_marca")
    @ManyToOne
    private Marca marca;
    @JoinColumn(name = "modelo", referencedColumnName = "id_modelo")
    @ManyToOne
    private EquipoModelo modelo;
    @JoinColumn(name = "tipo", referencedColumnName = "id_tipo")
    @ManyToOne
    private EquipoTipo tipo;

    public Equipo() {
    }

    public Equipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOtro() {
        return otro;
    }

    public void setOtro(String otro) {
        this.otro = otro;
    }

    @XmlTransient
    public List<Repuesto> getRepuestoList() {
        return repuestoList;
    }

    public void setRepuestoList(List<Repuesto> repuestoList) {
        this.repuestoList = repuestoList;
    }

    @XmlTransient
    public List<RepuestoStockDetalle> getRepuestoStockDetalleList() {
        return repuestoStockDetalleList;
    }

    public void setRepuestoStockDetalleList(List<RepuestoStockDetalle> repuestoStockDetalleList) {
        this.repuestoStockDetalleList = repuestoStockDetalleList;
    }

    public EquipoCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(EquipoCategoria categoria) {
        this.categoria = categoria;
    }

    public EquipoCompraVenta getCompraVenta() {
        return compraVenta;
    }

    public void setCompraVenta(EquipoCompraVenta compraVenta) {
        this.compraVenta = compraVenta;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public EquipoModelo getModelo() {
        return modelo;
    }

    public void setModelo(EquipoModelo modelo) {
        this.modelo = modelo;
    }

    public EquipoTipo getTipo() {
        return tipo;
    }

    public void setTipo(EquipoTipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEquipo != null ? idEquipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.idEquipo == null && other.idEquipo != null) || (this.idEquipo != null && !this.idEquipo.equals(other.idEquipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.modelo.getNombre();
    }
    
}
