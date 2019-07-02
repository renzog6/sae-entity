/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.empleado.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.empleado.EmpleadoCategoria;
import ar.nex.entity.empleado.Persona;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.ubicacion.Contacto;
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
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws IllegalOrphanException {
        if (empleado.getContactoList() == null) {
            empleado.setContactoList(new ArrayList<Contacto>());
        }
        List<String> illegalOrphanMessages = null;
        Persona personaOrphanCheck = empleado.getPersona();
        if (personaOrphanCheck != null) {
            Empleado oldEmpleadoOfPersona = personaOrphanCheck.getEmpleado();
            if (oldEmpleadoOfPersona != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Persona " + personaOrphanCheck + " already has an item of type Empleado whose persona column cannot be null. Please make another selection for the persona field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpleadoCategoria categoria = empleado.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getIdCategoria());
                empleado.setCategoria(categoria);
            }
            Persona persona = empleado.getPersona();
            if (persona != null) {
                persona = em.getReference(persona.getClass(), persona.getIdPersona());
                empleado.setPersona(persona);
            }
            Empresa empresa = empleado.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                empleado.setEmpresa(empresa);
            }
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : empleado.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            empleado.setContactoList(attachedContactoList);
            em.persist(empleado);
            if (categoria != null) {
                categoria.getEmpleadoList().add(empleado);
                categoria = em.merge(categoria);
            }
            if (persona != null) {
                persona.setEmpleado(empleado);
                persona = em.merge(persona);
            }
            if (empresa != null) {
                empresa.getEmpleadoList().add(empleado);
                empresa = em.merge(empresa);
            }
            for (Contacto contactoListContacto : empleado.getContactoList()) {
                contactoListContacto.getEmpleadoList().add(empleado);
                contactoListContacto = em.merge(contactoListContacto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            EmpleadoCategoria categoriaOld = persistentEmpleado.getCategoria();
            EmpleadoCategoria categoriaNew = empleado.getCategoria();
            Persona personaOld = persistentEmpleado.getPersona();
            Persona personaNew = empleado.getPersona();
            Empresa empresaOld = persistentEmpleado.getEmpresa();
            Empresa empresaNew = empleado.getEmpresa();
            List<Contacto> contactoListOld = persistentEmpleado.getContactoList();
            List<Contacto> contactoListNew = empleado.getContactoList();
            List<String> illegalOrphanMessages = null;
            if (personaNew != null && !personaNew.equals(personaOld)) {
                Empleado oldEmpleadoOfPersona = personaNew.getEmpleado();
                if (oldEmpleadoOfPersona != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Persona " + personaNew + " already has an item of type Empleado whose persona column cannot be null. Please make another selection for the persona field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getIdCategoria());
                empleado.setCategoria(categoriaNew);
            }
            if (personaNew != null) {
                personaNew = em.getReference(personaNew.getClass(), personaNew.getIdPersona());
                empleado.setPersona(personaNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                empleado.setEmpresa(empresaNew);
            }
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            empleado.setContactoList(contactoListNew);
            empleado = em.merge(empleado);
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getEmpleadoList().remove(empleado);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getEmpleadoList().add(empleado);
                categoriaNew = em.merge(categoriaNew);
            }
            if (personaOld != null && !personaOld.equals(personaNew)) {
                personaOld.setEmpleado(null);
                personaOld = em.merge(personaOld);
            }
            if (personaNew != null && !personaNew.equals(personaOld)) {
                personaNew.setEmpleado(empleado);
                personaNew = em.merge(personaNew);
            }
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getEmpleadoList().remove(empleado);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getEmpleadoList().add(empleado);
                empresaNew = em.merge(empresaNew);
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getEmpleadoList().remove(empleado);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getEmpleadoList().add(empleado);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            EmpleadoCategoria categoria = empleado.getCategoria();
            if (categoria != null) {
                categoria.getEmpleadoList().remove(empleado);
                categoria = em.merge(categoria);
            }
            Persona persona = empleado.getPersona();
            if (persona != null) {
                persona.setEmpleado(null);
                persona = em.merge(persona);
            }
            Empresa empresa = empleado.getEmpresa();
            if (empresa != null) {
                empresa.getEmpleadoList().remove(empleado);
                empresa = em.merge(empresa);
            }
            List<Contacto> contactoList = empleado.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getEmpleadoList().remove(empleado);
                contactoListContacto = em.merge(contactoListContacto);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
