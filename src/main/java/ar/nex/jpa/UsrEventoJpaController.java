/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.UsrEvento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.UsrHistorial;
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
public class UsrEventoJpaController implements Serializable {

    public UsrEventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsrEvento usrEvento) throws PreexistingEntityException, Exception {
        if (usrEvento.getUsrHistorialList() == null) {
            usrEvento.setUsrHistorialList(new ArrayList<UsrHistorial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UsrHistorial> attachedUsrHistorialList = new ArrayList<UsrHistorial>();
            for (UsrHistorial usrHistorialListUsrHistorialToAttach : usrEvento.getUsrHistorialList()) {
                usrHistorialListUsrHistorialToAttach = em.getReference(usrHistorialListUsrHistorialToAttach.getClass(), usrHistorialListUsrHistorialToAttach.getIdHistorial());
                attachedUsrHistorialList.add(usrHistorialListUsrHistorialToAttach);
            }
            usrEvento.setUsrHistorialList(attachedUsrHistorialList);
            em.persist(usrEvento);
            for (UsrHistorial usrHistorialListUsrHistorial : usrEvento.getUsrHistorialList()) {
                UsrEvento oldEventoOfUsrHistorialListUsrHistorial = usrHistorialListUsrHistorial.getEvento();
                usrHistorialListUsrHistorial.setEvento(usrEvento);
                usrHistorialListUsrHistorial = em.merge(usrHistorialListUsrHistorial);
                if (oldEventoOfUsrHistorialListUsrHistorial != null) {
                    oldEventoOfUsrHistorialListUsrHistorial.getUsrHistorialList().remove(usrHistorialListUsrHistorial);
                    oldEventoOfUsrHistorialListUsrHistorial = em.merge(oldEventoOfUsrHistorialListUsrHistorial);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsrEvento(usrEvento.getIdEvento()) != null) {
                throw new PreexistingEntityException("UsrEvento " + usrEvento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsrEvento usrEvento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrEvento persistentUsrEvento = em.find(UsrEvento.class, usrEvento.getIdEvento());
            List<UsrHistorial> usrHistorialListOld = persistentUsrEvento.getUsrHistorialList();
            List<UsrHistorial> usrHistorialListNew = usrEvento.getUsrHistorialList();
            List<UsrHistorial> attachedUsrHistorialListNew = new ArrayList<UsrHistorial>();
            for (UsrHistorial usrHistorialListNewUsrHistorialToAttach : usrHistorialListNew) {
                usrHistorialListNewUsrHistorialToAttach = em.getReference(usrHistorialListNewUsrHistorialToAttach.getClass(), usrHistorialListNewUsrHistorialToAttach.getIdHistorial());
                attachedUsrHistorialListNew.add(usrHistorialListNewUsrHistorialToAttach);
            }
            usrHistorialListNew = attachedUsrHistorialListNew;
            usrEvento.setUsrHistorialList(usrHistorialListNew);
            usrEvento = em.merge(usrEvento);
            for (UsrHistorial usrHistorialListOldUsrHistorial : usrHistorialListOld) {
                if (!usrHistorialListNew.contains(usrHistorialListOldUsrHistorial)) {
                    usrHistorialListOldUsrHistorial.setEvento(null);
                    usrHistorialListOldUsrHistorial = em.merge(usrHistorialListOldUsrHistorial);
                }
            }
            for (UsrHistorial usrHistorialListNewUsrHistorial : usrHistorialListNew) {
                if (!usrHistorialListOld.contains(usrHistorialListNewUsrHistorial)) {
                    UsrEvento oldEventoOfUsrHistorialListNewUsrHistorial = usrHistorialListNewUsrHistorial.getEvento();
                    usrHistorialListNewUsrHistorial.setEvento(usrEvento);
                    usrHistorialListNewUsrHistorial = em.merge(usrHistorialListNewUsrHistorial);
                    if (oldEventoOfUsrHistorialListNewUsrHistorial != null && !oldEventoOfUsrHistorialListNewUsrHistorial.equals(usrEvento)) {
                        oldEventoOfUsrHistorialListNewUsrHistorial.getUsrHistorialList().remove(usrHistorialListNewUsrHistorial);
                        oldEventoOfUsrHistorialListNewUsrHistorial = em.merge(oldEventoOfUsrHistorialListNewUsrHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usrEvento.getIdEvento();
                if (findUsrEvento(id) == null) {
                    throw new NonexistentEntityException("The usrEvento with id " + id + " no longer exists.");
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
            UsrEvento usrEvento;
            try {
                usrEvento = em.getReference(UsrEvento.class, id);
                usrEvento.getIdEvento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usrEvento with id " + id + " no longer exists.", enfe);
            }
            List<UsrHistorial> usrHistorialList = usrEvento.getUsrHistorialList();
            for (UsrHistorial usrHistorialListUsrHistorial : usrHistorialList) {
                usrHistorialListUsrHistorial.setEvento(null);
                usrHistorialListUsrHistorial = em.merge(usrHistorialListUsrHistorial);
            }
            em.remove(usrEvento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsrEvento> findUsrEventoEntities() {
        return findUsrEventoEntities(true, -1, -1);
    }

    public List<UsrEvento> findUsrEventoEntities(int maxResults, int firstResult) {
        return findUsrEventoEntities(false, maxResults, firstResult);
    }

    private List<UsrEvento> findUsrEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsrEvento.class));
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

    public UsrEvento findUsrEvento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsrEvento.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsrEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsrEvento> rt = cq.from(UsrEvento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
