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
import ar.nex.entity.RhCategoria;
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
public class RhCategoriaJpaController implements Serializable {

    public RhCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RhCategoria rhCategoria) throws PreexistingEntityException, Exception {
        if (rhCategoria.getEmpleadoList() == null) {
            rhCategoria.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : rhCategoria.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            rhCategoria.setEmpleadoList(attachedEmpleadoList);
            em.persist(rhCategoria);
            for (Empleado empleadoListEmpleado : rhCategoria.getEmpleadoList()) {
                RhCategoria oldCategoriaOfEmpleadoListEmpleado = empleadoListEmpleado.getCategoria();
                empleadoListEmpleado.setCategoria(rhCategoria);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldCategoriaOfEmpleadoListEmpleado != null) {
                    oldCategoriaOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldCategoriaOfEmpleadoListEmpleado = em.merge(oldCategoriaOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRhCategoria(rhCategoria.getIdCategoria()) != null) {
                throw new PreexistingEntityException("RhCategoria " + rhCategoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RhCategoria rhCategoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RhCategoria persistentRhCategoria = em.find(RhCategoria.class, rhCategoria.getIdCategoria());
            List<Empleado> empleadoListOld = persistentRhCategoria.getEmpleadoList();
            List<Empleado> empleadoListNew = rhCategoria.getEmpleadoList();
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            rhCategoria.setEmpleadoList(empleadoListNew);
            rhCategoria = em.merge(rhCategoria);
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    empleadoListOldEmpleado.setCategoria(null);
                    empleadoListOldEmpleado = em.merge(empleadoListOldEmpleado);
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    RhCategoria oldCategoriaOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getCategoria();
                    empleadoListNewEmpleado.setCategoria(rhCategoria);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldCategoriaOfEmpleadoListNewEmpleado != null && !oldCategoriaOfEmpleadoListNewEmpleado.equals(rhCategoria)) {
                        oldCategoriaOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldCategoriaOfEmpleadoListNewEmpleado = em.merge(oldCategoriaOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = rhCategoria.getIdCategoria();
                if (findRhCategoria(id) == null) {
                    throw new NonexistentEntityException("The rhCategoria with id " + id + " no longer exists.");
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
            RhCategoria rhCategoria;
            try {
                rhCategoria = em.getReference(RhCategoria.class, id);
                rhCategoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rhCategoria with id " + id + " no longer exists.", enfe);
            }
            List<Empleado> empleadoList = rhCategoria.getEmpleadoList();
            for (Empleado empleadoListEmpleado : empleadoList) {
                empleadoListEmpleado.setCategoria(null);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
            }
            em.remove(rhCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RhCategoria> findRhCategoriaEntities() {
        return findRhCategoriaEntities(true, -1, -1);
    }

    public List<RhCategoria> findRhCategoriaEntities(int maxResults, int firstResult) {
        return findRhCategoriaEntities(false, maxResults, firstResult);
    }

    private List<RhCategoria> findRhCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RhCategoria.class));
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

    public RhCategoria findRhCategoria(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RhCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getRhCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RhCategoria> rt = cq.from(RhCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
