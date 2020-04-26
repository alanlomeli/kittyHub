/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Publicacion;
import Entidades.Ubicacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author marianabojorquez
 */
public class UbicacionJpaController implements Serializable {

    public UbicacionJpaController(){
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit");

    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ubicacion ubicacion) {
        if (ubicacion.getPublicacionList() == null) {
            ubicacion.setPublicacionList(new ArrayList<Publicacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Publicacion> attachedPublicacionList = new ArrayList<Publicacion>();
            for (Publicacion publicacionListPublicacionToAttach : ubicacion.getPublicacionList()) {
                publicacionListPublicacionToAttach = em.getReference(publicacionListPublicacionToAttach.getClass(), publicacionListPublicacionToAttach.getPublicacionId());
                attachedPublicacionList.add(publicacionListPublicacionToAttach);
            }
            ubicacion.setPublicacionList(attachedPublicacionList);
            em.persist(ubicacion);
            for (Publicacion publicacionListPublicacion : ubicacion.getPublicacionList()) {
                Ubicacion oldUbicacionFkOfPublicacionListPublicacion = publicacionListPublicacion.getUbicacionFk();
                publicacionListPublicacion.setUbicacionFk(ubicacion);
                publicacionListPublicacion = em.merge(publicacionListPublicacion);
                if (oldUbicacionFkOfPublicacionListPublicacion != null) {
                    oldUbicacionFkOfPublicacionListPublicacion.getPublicacionList().remove(publicacionListPublicacion);
                    oldUbicacionFkOfPublicacionListPublicacion = em.merge(oldUbicacionFkOfPublicacionListPublicacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ubicacion ubicacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion persistentUbicacion = em.find(Ubicacion.class, ubicacion.getUbicacionId());
            List<Publicacion> publicacionListOld = persistentUbicacion.getPublicacionList();
            List<Publicacion> publicacionListNew = ubicacion.getPublicacionList();
            List<Publicacion> attachedPublicacionListNew = new ArrayList<Publicacion>();
            for (Publicacion publicacionListNewPublicacionToAttach : publicacionListNew) {
                publicacionListNewPublicacionToAttach = em.getReference(publicacionListNewPublicacionToAttach.getClass(), publicacionListNewPublicacionToAttach.getPublicacionId());
                attachedPublicacionListNew.add(publicacionListNewPublicacionToAttach);
            }
            publicacionListNew = attachedPublicacionListNew;
            ubicacion.setPublicacionList(publicacionListNew);
            ubicacion = em.merge(ubicacion);
            for (Publicacion publicacionListOldPublicacion : publicacionListOld) {
                if (!publicacionListNew.contains(publicacionListOldPublicacion)) {
                    publicacionListOldPublicacion.setUbicacionFk(null);
                    publicacionListOldPublicacion = em.merge(publicacionListOldPublicacion);
                }
            }
            for (Publicacion publicacionListNewPublicacion : publicacionListNew) {
                if (!publicacionListOld.contains(publicacionListNewPublicacion)) {
                    Ubicacion oldUbicacionFkOfPublicacionListNewPublicacion = publicacionListNewPublicacion.getUbicacionFk();
                    publicacionListNewPublicacion.setUbicacionFk(ubicacion);
                    publicacionListNewPublicacion = em.merge(publicacionListNewPublicacion);
                    if (oldUbicacionFkOfPublicacionListNewPublicacion != null && !oldUbicacionFkOfPublicacionListNewPublicacion.equals(ubicacion)) {
                        oldUbicacionFkOfPublicacionListNewPublicacion.getPublicacionList().remove(publicacionListNewPublicacion);
                        oldUbicacionFkOfPublicacionListNewPublicacion = em.merge(oldUbicacionFkOfPublicacionListNewPublicacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ubicacion.getUbicacionId();
                if (findUbicacion(id) == null) {
                    throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.");
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
            Ubicacion ubicacion;
            try {
                ubicacion = em.getReference(Ubicacion.class, id);
                ubicacion.getUbicacionId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.", enfe);
            }
            List<Publicacion> publicacionList = ubicacion.getPublicacionList();
            for (Publicacion publicacionListPublicacion : publicacionList) {
                publicacionListPublicacion.setUbicacionFk(null);
                publicacionListPublicacion = em.merge(publicacionListPublicacion);
            }
            em.remove(ubicacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ubicacion> findUbicacionEntities() {
        return findUbicacionEntities(true, -1, -1);
    }

    public List<Ubicacion> findUbicacionEntities(int maxResults, int firstResult) {
        return findUbicacionEntities(false, maxResults, firstResult);
    }

    private List<Ubicacion> findUbicacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ubicacion.class));
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

    public Ubicacion findUbicacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ubicacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getUbicacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ubicacion> rt = cq.from(Ubicacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
