/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Empleado;
import ar.nex.entity.RhPuesto;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class RhPuestoJpaController implements Serializable {

    public RhPuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RhPuesto rhPuesto) throws PreexistingEntityException, Exception {
        if (rhPuesto.getEmpleadoList() == null) {
            rhPuesto.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : rhPuesto.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            rhPuesto.setEmpleadoList(attachedEmpleadoList);
            em.persist(rhPuesto);
            for (Empleado empleadoListEmpleado : rhPuesto.getEmpleadoList()) {
                RhPuesto oldPuestoOfEmpleadoListEmpleado = empleadoListEmpleado.getPuesto();
                empleadoListEmpleado.setPuesto(rhPuesto);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldPuestoOfEmpleadoListEmpleado != null) {
                    oldPuestoOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldPuestoOfEmpleadoListEmpleado = em.merge(oldPuestoOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRhPuesto(rhPuesto.getIdPuesto()) != null) {
                throw new PreexistingEntityException("RhPuesto " + rhPuesto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RhPuesto rhPuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RhPuesto persistentRhPuesto = em.find(RhPuesto.class, rhPuesto.getIdPuesto());
            List<Empleado> empleadoListOld = persistentRhPuesto.getEmpleadoList();
            List<Empleado> empleadoListNew = rhPuesto.getEmpleadoList();
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            rhPuesto.setEmpleadoList(empleadoListNew);
            rhPuesto = em.merge(rhPuesto);
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    empleadoListOldEmpleado.setPuesto(null);
                    empleadoListOldEmpleado = em.merge(empleadoListOldEmpleado);
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    RhPuesto oldPuestoOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getPuesto();
                    empleadoListNewEmpleado.setPuesto(rhPuesto);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldPuestoOfEmpleadoListNewEmpleado != null && !oldPuestoOfEmpleadoListNewEmpleado.equals(rhPuesto)) {
                        oldPuestoOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldPuestoOfEmpleadoListNewEmpleado = em.merge(oldPuestoOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = rhPuesto.getIdPuesto();
                if (findRhPuesto(id) == null) {
                    throw new NonexistentEntityException("The rhPuesto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RhPuesto rhPuesto;
            try {
                rhPuesto = em.getReference(RhPuesto.class, id);
                rhPuesto.getIdPuesto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rhPuesto with id " + id + " no longer exists.", enfe);
            }
            List<Empleado> empleadoList = rhPuesto.getEmpleadoList();
            for (Empleado empleadoListEmpleado : empleadoList) {
                empleadoListEmpleado.setPuesto(null);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
            }
            em.remove(rhPuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RhPuesto> findRhPuestoEntities() {
        return findRhPuestoEntities(true, -1, -1);
    }

    public List<RhPuesto> findRhPuestoEntities(int maxResults, int firstResult) {
        return findRhPuestoEntities(false, maxResults, firstResult);
    }

    private List<RhPuesto> findRhPuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RhPuesto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RhPuesto findRhPuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RhPuesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getRhPuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RhPuesto> rt = cq.from(RhPuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
