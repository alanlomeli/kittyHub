/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Entidades.Peticion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Publicacion;
import Entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author marianabojorquez
 */
public class PeticionJpaController implements Serializable {

    public PeticionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Peticion peticion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Publicacion publicacionFk = peticion.getPublicacionFk();
            if (publicacionFk != null) {
                publicacionFk = em.getReference(publicacionFk.getClass(), publicacionFk.getPublicacionId());
                peticion.setPublicacionFk(publicacionFk);
            }
            Usuario usuarioFk = peticion.getUsuarioFk();
            if (usuarioFk != null) {
                usuarioFk = em.getReference(usuarioFk.getClass(), usuarioFk.getUsuarioId());
                peticion.setUsuarioFk(usuarioFk);
            }
            em.persist(peticion);
            if (publicacionFk != null) {
                publicacionFk.getPeticionList().add(peticion);
                publicacionFk = em.merge(publicacionFk);
            }
            if (usuarioFk != null) {
                usuarioFk.getPeticionList().add(peticion);
                usuarioFk = em.merge(usuarioFk);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Peticion peticion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peticion persistentPeticion = em.find(Peticion.class, peticion.getPeticionId());
            Publicacion publicacionFkOld = persistentPeticion.getPublicacionFk();
            Publicacion publicacionFkNew = peticion.getPublicacionFk();
            Usuario usuarioFkOld = persistentPeticion.getUsuarioFk();
            Usuario usuarioFkNew = peticion.getUsuarioFk();
            if (publicacionFkNew != null) {
                publicacionFkNew = em.getReference(publicacionFkNew.getClass(), publicacionFkNew.getPublicacionId());
                peticion.setPublicacionFk(publicacionFkNew);
            }
            if (usuarioFkNew != null) {
                usuarioFkNew = em.getReference(usuarioFkNew.getClass(), usuarioFkNew.getUsuarioId());
                peticion.setUsuarioFk(usuarioFkNew);
            }
            peticion = em.merge(peticion);
            if (publicacionFkOld != null && !publicacionFkOld.equals(publicacionFkNew)) {
                publicacionFkOld.getPeticionList().remove(peticion);
                publicacionFkOld = em.merge(publicacionFkOld);
            }
            if (publicacionFkNew != null && !publicacionFkNew.equals(publicacionFkOld)) {
                publicacionFkNew.getPeticionList().add(peticion);
                publicacionFkNew = em.merge(publicacionFkNew);
            }
            if (usuarioFkOld != null && !usuarioFkOld.equals(usuarioFkNew)) {
                usuarioFkOld.getPeticionList().remove(peticion);
                usuarioFkOld = em.merge(usuarioFkOld);
            }
            if (usuarioFkNew != null && !usuarioFkNew.equals(usuarioFkOld)) {
                usuarioFkNew.getPeticionList().add(peticion);
                usuarioFkNew = em.merge(usuarioFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = peticion.getPeticionId();
                if (findPeticion(id) == null) {
                    throw new NonexistentEntityException("The peticion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peticion peticion;
            try {
                peticion = em.getReference(Peticion.class, id);
                peticion.getPeticionId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peticion with id " + id + " no longer exists.", enfe);
            }
            Publicacion publicacionFk = peticion.getPublicacionFk();
            if (publicacionFk != null) {
                publicacionFk.getPeticionList().remove(peticion);
                publicacionFk = em.merge(publicacionFk);
            }
            Usuario usuarioFk = peticion.getUsuarioFk();
            if (usuarioFk != null) {
                usuarioFk.getPeticionList().remove(peticion);
                usuarioFk = em.merge(usuarioFk);
            }
            em.remove(peticion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Peticion> findPeticionEntities() {
        return findPeticionEntities(true, -1, -1);
    }

    public List<Peticion> findPeticionEntities(int maxResults, int firstResult) {
        return findPeticionEntities(false, maxResults, firstResult);
    }

    private List<Peticion> findPeticionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Peticion.class));
            Query q = em.createQuery(cq);
            q.setHint("javax.persistence.cache.storeMode", "REFRESH");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Peticion findPeticion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Peticion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeticionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Peticion> rt = cq.from(Peticion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
