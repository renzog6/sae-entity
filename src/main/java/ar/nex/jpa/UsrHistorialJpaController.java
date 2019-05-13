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
import ar.nex.entity.UsrEvento;
import ar.nex.entity.UsrHistorial;
import ar.nex.entity.Usuario;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class UsrHistorialJpaController implements Serializable {

    public UsrHistorialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsrHistorial usrHistorial) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrEvento evento = usrHistorial.getEvento();
            if (evento != null) {
                evento = em.getReference(evento.getClass(), evento.getIdEvento());
                usrHistorial.setEvento(evento);
            }
            Usuario usuario = usrHistorial.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                usrHistorial.setUsuario(usuario);
            }
            em.persist(usrHistorial);
            if (evento != null) {
                evento.getUsrHistorialList().add(usrHistorial);
                evento = em.merge(evento);
            }
            if (usuario != null) {
                usuario.getUsrHistorialList().add(usrHistorial);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsrHistorial usrHistorial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrHistorial persistentUsrHistorial = em.find(UsrHistorial.class, usrHistorial.getIdHistorial());
            UsrEvento eventoOld = persistentUsrHistorial.getEvento();
            UsrEvento eventoNew = usrHistorial.getEvento();
            Usuario usuarioOld = persistentUsrHistorial.getUsuario();
            Usuario usuarioNew = usrHistorial.getUsuario();
            if (eventoNew != null) {
                eventoNew = em.getReference(eventoNew.getClass(), eventoNew.getIdEvento());
                usrHistorial.setEvento(eventoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                usrHistorial.setUsuario(usuarioNew);
            }
            usrHistorial = em.merge(usrHistorial);
            if (eventoOld != null && !eventoOld.equals(eventoNew)) {
                eventoOld.getUsrHistorialList().remove(usrHistorial);
                eventoOld = em.merge(eventoOld);
            }
            if (eventoNew != null && !eventoNew.equals(eventoOld)) {
                eventoNew.getUsrHistorialList().add(usrHistorial);
                eventoNew = em.merge(eventoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getUsrHistorialList().remove(usrHistorial);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getUsrHistorialList().add(usrHistorial);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usrHistorial.getIdHistorial();
                if (findUsrHistorial(id) == null) {
                    throw new NonexistentEntityException("The usrHistorial with id " + id + " no longer exists.");
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
            UsrHistorial usrHistorial;
            try {
                usrHistorial = em.getReference(UsrHistorial.class, id);
                usrHistorial.getIdHistorial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usrHistorial with id " + id + " no longer exists.", enfe);
            }
            UsrEvento evento = usrHistorial.getEvento();
            if (evento != null) {
                evento.getUsrHistorialList().remove(usrHistorial);
                evento = em.merge(evento);
            }
            Usuario usuario = usrHistorial.getUsuario();
            if (usuario != null) {
                usuario.getUsrHistorialList().remove(usrHistorial);
                usuario = em.merge(usuario);
            }
            em.remove(usrHistorial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsrHistorial> findUsrHistorialEntities() {
        return findUsrHistorialEntities(true, -1, -1);
    }

    public List<UsrHistorial> findUsrHistorialEntities(int maxResults, int firstResult) {
        return findUsrHistorialEntities(false, maxResults, firstResult);
    }

    private List<UsrHistorial> findUsrHistorialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsrHistorial.class));
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

    public UsrHistorial findUsrHistorial(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsrHistorial.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsrHistorialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsrHistorial> rt = cq.from(UsrHistorial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
