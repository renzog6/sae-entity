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
import ar.nex.entity.UsuarioEvento;
import ar.nex.entity.Usuario;
import ar.nex.entity.UsuarioHistorial;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class UsuarioHistorialJpaController implements Serializable {

    public UsuarioHistorialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioHistorial usuarioHistorial) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioEvento evento = usuarioHistorial.getEvento();
            if (evento != null) {
                evento = em.getReference(evento.getClass(), evento.getIdEvento());
                usuarioHistorial.setEvento(evento);
            }
            Usuario usuario = usuarioHistorial.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                usuarioHistorial.setUsuario(usuario);
            }
            em.persist(usuarioHistorial);
            if (evento != null) {
                evento.getUsrHistorialList().add(usuarioHistorial);
                evento = em.merge(evento);
            }
            if (usuario != null) {
                usuario.getUsuarioHistorialList().add(usuarioHistorial);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioHistorial usuarioHistorial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioHistorial persistentUsuarioHistorial = em.find(UsuarioHistorial.class, usuarioHistorial.getIdHistorial());
            UsuarioEvento eventoOld = persistentUsuarioHistorial.getEvento();
            UsuarioEvento eventoNew = usuarioHistorial.getEvento();
            Usuario usuarioOld = persistentUsuarioHistorial.getUsuario();
            Usuario usuarioNew = usuarioHistorial.getUsuario();
            if (eventoNew != null) {
                eventoNew = em.getReference(eventoNew.getClass(), eventoNew.getIdEvento());
                usuarioHistorial.setEvento(eventoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                usuarioHistorial.setUsuario(usuarioNew);
            }
            usuarioHistorial = em.merge(usuarioHistorial);
            if (eventoOld != null && !eventoOld.equals(eventoNew)) {
                eventoOld.getUsrHistorialList().remove(usuarioHistorial);
                eventoOld = em.merge(eventoOld);
            }
            if (eventoNew != null && !eventoNew.equals(eventoOld)) {
                eventoNew.getUsrHistorialList().add(usuarioHistorial);
                eventoNew = em.merge(eventoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getUsuarioHistorialList().remove(usuarioHistorial);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getUsuarioHistorialList().add(usuarioHistorial);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuarioHistorial.getIdHistorial();
                if (findUsuarioHistorial(id) == null) {
                    throw new NonexistentEntityException("The usuarioHistorial with id " + id + " no longer exists.");
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
            UsuarioHistorial usuarioHistorial;
            try {
                usuarioHistorial = em.getReference(UsuarioHistorial.class, id);
                usuarioHistorial.getIdHistorial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioHistorial with id " + id + " no longer exists.", enfe);
            }
            UsuarioEvento evento = usuarioHistorial.getEvento();
            if (evento != null) {
                evento.getUsrHistorialList().remove(usuarioHistorial);
                evento = em.merge(evento);
            }
            Usuario usuario = usuarioHistorial.getUsuario();
            if (usuario != null) {
                usuario.getUsuarioHistorialList().remove(usuarioHistorial);
                usuario = em.merge(usuario);
            }
            em.remove(usuarioHistorial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioHistorial> findUsuarioHistorialEntities() {
        return findUsuarioHistorialEntities(true, -1, -1);
    }

    public List<UsuarioHistorial> findUsuarioHistorialEntities(int maxResults, int firstResult) {
        return findUsuarioHistorialEntities(false, maxResults, firstResult);
    }

    private List<UsuarioHistorial> findUsuarioHistorialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioHistorial.class));
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

    public UsuarioHistorial findUsuarioHistorial(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioHistorial.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioHistorialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioHistorial> rt = cq.from(UsuarioHistorial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
