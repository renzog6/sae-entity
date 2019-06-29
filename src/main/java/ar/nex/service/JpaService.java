package ar.nex.service;

import ar.nex.jpa.ContactoJpaController;
import ar.nex.jpa.DireccionJpaController;
import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.LocalidadJpaController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.ProvinciaJpaController;
import ar.nex.jpa.RepuestoJpaController;
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
        if (factory == null) {
            getFactory();
        }
        return new EmpresaJpaController(factory);
    }

    public PedidoJpaController getPedido() {
        if (factory == null) {
            getFactory();
        }
        return new PedidoJpaController(factory);
    }

    public RepuestoJpaController getRepuesto() {
        if (factory == null) {
            getFactory();
        }
        return new RepuestoJpaController(factory);
    }

    public UsuarioJpaController getUsuario() {
        if (factory == null) {
            getFactory();
        }
        return new UsuarioJpaController(factory);
    }

    public ProvinciaJpaController getProvincia() {
        if (factory == null) {
            getFactory();
        }
        return new ProvinciaJpaController(factory);
    }

    public LocalidadJpaController getLocalidad() {
        if (factory == null) {
            getFactory();
        }
        return new LocalidadJpaController(factory);
    }

    public DireccionJpaController getDireccion() {
        if (factory == null) {
            getFactory();
        }
        return new DireccionJpaController(factory);
    }

    public ContactoJpaController getContacto() {
        if (factory == null) {
            getFactory();
        }
        return new ContactoJpaController(factory);
    }
}
