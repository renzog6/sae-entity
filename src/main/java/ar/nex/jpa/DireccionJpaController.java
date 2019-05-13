/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.Direccion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Localidad;
import ar.nex.entity.Empresa;
import ar.nex.entity.Persona;
import ar.nex.jpa.exceptions.IllegalOrphanException;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class DireccionJpaController implements Serializable {

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) {
        if (direccion.getPersonaList() == null) {
            direccion.setPersonaList(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad localidad = direccion.getLocalidad();
            if (localidad != null) {
                localidad = em.getReference(localidad.getClass(), localidad.getIdLocalidad());
                direccion.setLocalidad(localidad);
            }
            Empresa empresa = direccion.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                direccion.setEmpresa(empresa);
            }
            List<Persona> attachedPersonaList = new ArrayList<Persona>();
            for (Persona personaListPersonaToAttach : direccion.getPersonaList()) {
                personaListPersonaToAttach = em.getReference(personaListPersonaToAttach.getClass(), personaListPersonaToAttach.getIdPersona());
                attachedPersonaList.add(personaListPersonaToAttach);
            }
            direccion.setPersonaList(attachedPersonaList);
            em.persist(direccion);
            if (localidad != null) {
                localidad.getDireccionList().add(direccion);
                localidad = em.merge(localidad);
            }
            if (empresa != null) {
                Direccion oldDomicilioOfEmpresa = empresa.getDomicilio();
                if (oldDomicilioOfEmpresa != null) {
                    oldDomicilioOfEmpresa.setEmpresa(null);
                    oldDomicilioOfEmpresa = em.merge(oldDomicilioOfEmpresa);
                }
                empresa.setDomicilio(direccion);
                empresa = em.merge(empresa);
            }
            for (Persona personaListPersona : direccion.getPersonaList()) {
                Direccion oldDomicilioOfPersonaListPersona = personaListPersona.getDomicilio();
                personaListPersona.setDomicilio(direccion);
                personaListPersona = em.merge(personaListPersona);
                if (oldDomicilioOfPersonaListPersona != null) {
                    oldDomicilioOfPersonaListPersona.getPersonaList().remove(personaListPersona);
                    oldDomicilioOfPersonaListPersona = em.merge(oldDomicilioOfPersonaListPersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getIdDireccion());
            Localidad localidadOld = persistentDireccion.getLocalidad();
            Localidad localidadNew = direccion.getLocalidad();
            Empresa empresaOld = persistentDireccion.getEmpresa();
            Empresa empresaNew = direccion.getEmpresa();
            List<Persona> personaListOld = persistentDireccion.getPersonaList();
            List<Persona> personaListNew = direccion.getPersonaList();
            List<String> illegalOrphanMessages = null;
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Empresa " + empresaOld + " since its domicilio field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (localidadNew != null) {
                localidadNew = em.getReference(localidadNew.getClass(), localidadNew.getIdLocalidad());
                direccion.setLocalidad(localidadNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                direccion.setEmpresa(empresaNew);
            }
            List<Persona> attachedPersonaListNew = new ArrayList<Persona>();
            for (Persona personaListNewPersonaToAttach : personaListNew) {
                personaListNewPersonaToAttach = em.getReference(personaListNewPersonaToAttach.getClass(), personaListNewPersonaToAttach.getIdPersona());
                attachedPersonaListNew.add(personaListNewPersonaToAttach);
            }
            personaListNew = attachedPersonaListNew;
            direccion.setPersonaList(personaListNew);
            direccion = em.merge(direccion);
            if (localidadOld != null && !localidadOld.equals(localidadNew)) {
                localidadOld.getDireccionList().remove(direccion);
                localidadOld = em.merge(localidadOld);
            }
            if (localidadNew != null && !localidadNew.equals(localidadOld)) {
                localidadNew.getDireccionList().add(direccion);
                localidadNew = em.merge(localidadNew);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                Direccion oldDomicilioOfEmpresa = empresaNew.getDomicilio();
                if (oldDomicilioOfEmpresa != null) {
                    oldDomicilioOfEmpresa.setEmpresa(null);
                    oldDomicilioOfEmpresa = em.merge(oldDomicilioOfEmpresa);
                }
                empresaNew.setDomicilio(direccion);
                empresaNew = em.merge(empresaNew);
            }
            for (Persona personaListOldPersona : personaListOld) {
                if (!personaListNew.contains(personaListOldPersona)) {
                    personaListOldPersona.setDomicilio(null);
                    personaListOldPersona = em.merge(personaListOldPersona);
                }
            }
            for (Persona personaListNewPersona : personaListNew) {
                if (!personaListOld.contains(personaListNewPersona)) {
                    Direccion oldDomicilioOfPersonaListNewPersona = personaListNewPersona.getDomicilio();
                    personaListNewPersona.setDomicilio(direccion);
                    personaListNewPersona = em.merge(personaListNewPersona);
                    if (oldDomicilioOfPersonaListNewPersona != null && !oldDomicilioOfPersonaListNewPersona.equals(direccion)) {
                        oldDomicilioOfPersonaListNewPersona.getPersonaList().remove(personaListNewPersona);
                        oldDomicilioOfPersonaListNewPersona = em.merge(oldDomicilioOfPersonaListNewPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = direccion.getIdDireccion();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getIdDireccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Empresa empresaOrphanCheck = direccion.getEmpresa();
            if (empresaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direccion (" + direccion + ") cannot be destroyed since the Empresa " + empresaOrphanCheck + " in its empresa field has a non-nullable domicilio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Localidad localidad = direccion.getLocalidad();
            if (localidad != null) {
                localidad.getDireccionList().remove(direccion);
                localidad = em.merge(localidad);
            }
            List<Persona> personaList = direccion.getPersonaList();
            for (Persona personaListPersona : personaList) {
                personaListPersona.setDomicilio(null);
                personaListPersona = em.merge(personaListPersona);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
