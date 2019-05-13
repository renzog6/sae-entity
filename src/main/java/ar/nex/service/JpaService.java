package ar.nex.service;

import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.RepuestoJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public final class JpaService {

    private EntityManagerFactory factory;

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

}
