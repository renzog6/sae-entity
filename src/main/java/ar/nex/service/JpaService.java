package ar.nex.service;

import ar.nex.jpa.ContactoJpaController;
import ar.nex.jpa.DireccionJpaController;
import ar.nex.jpa.EmpleadoJpaController;
import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.EquipoCategoriaJpaController;
import ar.nex.jpa.EquipoCompraVentaJpaController;
import ar.nex.jpa.EquipoJpaController;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.EquipoTipoJpaController;
import ar.nex.jpa.LocalidadJpaController;
import ar.nex.jpa.MarcaJpaController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.PersonaJpaController;
import ar.nex.jpa.ProvinciaJpaController;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.jpa.RubroJpaController;
import ar.nex.jpa.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Renzo
 */
public final class JpaService {

    private final static Logger LOGGER = LogManager.getLogger(JpaService.class.getName());

    private EntityManagerFactory factory;

    public static void init() {
        LOGGER.info("JpaService init()");
    }

    public JpaService() {
        this.factory = getFactory();
    }

    public EntityManagerFactory getFactory() {
        if (factory == null) {
            this.factory = Persistence.createEntityManagerFactory("SaeFxPU");
        }
        return factory;
    }

    public EmpresaJpaController getEmpresa() {
        return new EmpresaJpaController(getFactory());
    }

    public MarcaJpaController getMarca() {
        return new MarcaJpaController(getFactory());
    }

    public EquipoJpaController getEquipo() {        
        return new EquipoJpaController(getFactory());
    }

    public EquipoCategoriaJpaController getEquipoCategoria() {
        return new EquipoCategoriaJpaController(getFactory());
    }

    public EquipoModeloJpaController getEquipoModelo() {
        return new EquipoModeloJpaController(getFactory());
    }

    public EquipoTipoJpaController getEquipoTipo() {        
        return new EquipoTipoJpaController(getFactory());
    }

    public EquipoCompraVentaJpaController getEquipoCompraVenta() {        
        return new EquipoCompraVentaJpaController(getFactory());
    }

    public PedidoJpaController getPedido() {        
        return new PedidoJpaController(getFactory());
    }

    public RepuestoJpaController getRepuesto() {        
        return new RepuestoJpaController(getFactory());
    }

    public UsuarioJpaController getUsuario() {        
        return new UsuarioJpaController(getFactory());
    }

    public EmpleadoJpaController getEmpleado() {        
        return new EmpleadoJpaController(getFactory());
    }

    public PersonaJpaController getPersona() {
        return new PersonaJpaController(getFactory());
    }

    public ProvinciaJpaController getProvincia() {        
        return new ProvinciaJpaController(getFactory());
    }

    public LocalidadJpaController getLocalidad() {        
        return new LocalidadJpaController(getFactory());
    }

    public DireccionJpaController getDireccion() {        
        return new DireccionJpaController(getFactory());
    }

    public ContactoJpaController getContacto() {        
        return new ContactoJpaController(getFactory());
    }

    public RubroJpaController getRubro() {        
        return new RubroJpaController(getFactory());
    }
}
