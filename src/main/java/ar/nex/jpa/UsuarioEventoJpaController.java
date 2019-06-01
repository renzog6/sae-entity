/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.UsuarioEvento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.UsuarioHistorial;
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
public class UsuarioEventoJpaController implements Serializable {

    public UsuarioEventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioEvento usuarioEvento) throws PreexistingEntityException, Exception {
        if (usuarioEvento.getUsrHistorialList() == null) {
            usuarioEvento.setUsrHistorialList(new ArrayList<UsuarioHistorial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UsuarioHistorial> attachedUsrHistorialList = new ArrayList<UsuarioHistorial>();
            for (UsuarioHistorial usrHistorialListUsuarioHistorialToAttach : usuarioEvento.getUsrHistorialList()) {
                usrHistorialListUsuarioHistorialToAttach = em.getReference(usrHistorialListUsuarioHistorialToAttach.getClass(), usrHistorialListUsuarioHistorialToAttach.getIdHistorial());
                attachedUsrHistorialList.add(usrHistorialListUsuarioHistorialToAttach);
            }
            usuarioEvento.setUsrHistorialList(attachedUsrHistorialList);
            em.persist(usuarioEvento);
            for (UsuarioHistorial usrHistorialListUsuarioHistorial : usuarioEvento.getUsrHistorialList()) {
                UsuarioEvento oldEventoOfUsrHistorialListUsuarioHistorial = usrHistorialListUsuarioHistorial.getEvento();
                usrHistorialListUsuarioHistorial.setEvento(usuarioEvento);
                usrHistorialListUsuarioHistorial = em.merge(usrHistorialListUsuarioHistorial);
                if (oldEventoOfUsrHistorialListUsuarioHistorial != null) {
                    oldEventoOfUsrHistorialListUsuarioHistorial.getUsrHistorialList().remove(usrHistorialListUsuarioHistorial);
                    oldEventoOfUsrHistorialListUsuarioHistorial = em.merge(oldEventoOfUsrHistorialListUsuarioHistorial);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioEvento(usuarioEvento.getIdEvento()) != null) {
                throw new PreexistingEntityException("UsuarioEvento " + usuarioEvento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioEvento usuarioEvento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioEvento persistentUsuarioEvento = em.find(UsuarioEvento.class, usuarioEvento.getIdEvento());
            List<UsuarioHistorial> usrHistorialListOld = persistentUsuarioEvento.getUsrHistorialList();
            List<UsuarioHistorial> usrHistorialListNew = usuarioEvento.getUsrHistorialList();
            List<UsuarioHistorial> attachedUsrHistorialListNew = new ArrayList<UsuarioHistorial>();
            for (UsuarioHistorial usrHistorialListNewUsuarioHistorialToAttach : usrHistorialListNew) {
                usrHistorialListNewUsuarioHistorialToAttach = em.getReference(usrHistorialListNewUsuarioHistorialToAttach.getClass(), usrHistorialListNewUsuarioHistorialToAttach.getIdHistorial());
                attachedUsrHistorialListNew.add(usrHistorialListNewUsuarioHistorialToAttach);
            }
            usrHistorialListNew = attachedUsrHistorialListNew;
            usuarioEvento.setUsrHistorialList(usrHistorialListNew);
            usuarioEvento = em.merge(usuarioEvento);
            for (UsuarioHistorial usrHistorialListOldUsuarioHistorial : usrHistorialListOld) {
                if (!usrHistorialListNew.contains(usrHistorialListOldUsuarioHistorial)) {
                    usrHistorialListOldUsuarioHistorial.setEvento(null);
                    usrHistorialListOldUsuarioHistorial = em.merge(usrHistorialListOldUsuarioHistorial);
                }
            }
            for (UsuarioHistorial usrHistorialListNewUsuarioHistorial : usrHistorialListNew) {
                if (!usrHistorialListOld.contains(usrHistorialListNewUsuarioHistorial)) {
                    UsuarioEvento oldEventoOfUsrHistorialListNewUsuarioHistorial = usrHistorialListNewUsuarioHistorial.getEvento();
                    usrHistorialListNewUsuarioHistorial.setEvento(usuarioEvento);
                    usrHistorialListNewUsuarioHistorial = em.merge(usrHistorialListNewUsuarioHistorial);
                    if (oldEventoOfUsrHistorialListNewUsuarioHistorial != null && !oldEventoOfUsrHistorialListNewUsuarioHistorial.equals(usuarioEvento)) {
                        oldEventoOfUsrHistorialListNewUsuarioHistorial.getUsrHistorialList().remove(usrHistorialListNewUsuarioHistorial);
                        oldEventoOfUsrHistorialListNewUsuarioHistorial = em.merge(oldEventoOfUsrHistorialListNewUsuarioHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuarioEvento.getIdEvento();
                if (findUsuarioEvento(id) == null) {
                    throw new NonexistentEntityException("The usuarioEvento with id " + id + " no longer exists.");
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
            UsuarioEvento usuarioEvento;
            try {
                usuarioEvento = em.getReference(UsuarioEvento.class, id);
                usuarioEvento.getIdEvento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioEvento with id " + id + " no longer exists.", enfe);
            }
            List<UsuarioHistorial> usrHistorialList = usuarioEvento.getUsrHistorialList();
            for (UsuarioHistorial usrHistorialListUsuarioHistorial : usrHistorialList) {
                usrHistorialListUsuarioHistorial.setEvento(null);
                usrHistorialListUsuarioHistorial = em.merge(usrHistorialListUsuarioHistorial);
            }
            em.remove(usuarioEvento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioEvento> findUsuarioEventoEntities() {
        return findUsuarioEventoEntities(true, -1, -1);
    }

    public List<UsuarioEvento> findUsuarioEventoEntities(int maxResults, int firstResult) {
        return findUsuarioEventoEntities(false, maxResults, firstResult);
    }

    private List<UsuarioEvento> findUsuarioEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioEvento.class));
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

    public UsuarioEvento findUsuarioEvento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioEvento.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioEvento> rt = cq.from(UsuarioEvento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
