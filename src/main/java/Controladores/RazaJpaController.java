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
import Entidades.Raza;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author alanlomeli
 */
public class RazaJpaController implements Serializable {

    public RazaJpaController(){
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit");

    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Raza raza) {
        if (raza.getPublicacionList() == null) {
            raza.setPublicacionList(new ArrayList<Publicacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Publicacion> attachedPublicacionList = new ArrayList<Publicacion>();
            for (Publicacion publicacionListPublicacionToAttach : raza.getPublicacionList()) {
                publicacionListPublicacionToAttach = em.getReference(publicacionListPublicacionToAttach.getClass(), publicacionListPublicacionToAttach.getPublicacionId());
                attachedPublicacionList.add(publicacionListPublicacionToAttach);
            }
            raza.setPublicacionList(attachedPublicacionList);
            em.persist(raza);
            for (Publicacion publicacionListPublicacion : raza.getPublicacionList()) {
                Raza oldRazaFkOfPublicacionListPublicacion = publicacionListPublicacion.getRazaFk();
                publicacionListPublicacion.setRazaFk(raza);
                publicacionListPublicacion = em.merge(publicacionListPublicacion);
                if (oldRazaFkOfPublicacionListPublicacion != null) {
                    oldRazaFkOfPublicacionListPublicacion.getPublicacionList().remove(publicacionListPublicacion);
                    oldRazaFkOfPublicacionListPublicacion = em.merge(oldRazaFkOfPublicacionListPublicacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Raza raza) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Raza persistentRaza = em.find(Raza.class, raza.getRazaId());
            List<Publicacion> publicacionListOld = persistentRaza.getPublicacionList();
            List<Publicacion> publicacionListNew = raza.getPublicacionList();
            List<Publicacion> attachedPublicacionListNew = new ArrayList<Publicacion>();
            for (Publicacion publicacionListNewPublicacionToAttach : publicacionListNew) {
                publicacionListNewPublicacionToAttach = em.getReference(publicacionListNewPublicacionToAttach.getClass(), publicacionListNewPublicacionToAttach.getPublicacionId());
                attachedPublicacionListNew.add(publicacionListNewPublicacionToAttach);
            }
            publicacionListNew = attachedPublicacionListNew;
            raza.setPublicacionList(publicacionListNew);
            raza = em.merge(raza);
            for (Publicacion publicacionListOldPublicacion : publicacionListOld) {
                if (!publicacionListNew.contains(publicacionListOldPublicacion)) {
                    publicacionListOldPublicacion.setRazaFk(null);
                    publicacionListOldPublicacion = em.merge(publicacionListOldPublicacion);
                }
            }
            for (Publicacion publicacionListNewPublicacion : publicacionListNew) {
                if (!publicacionListOld.contains(publicacionListNewPublicacion)) {
                    Raza oldRazaFkOfPublicacionListNewPublicacion = publicacionListNewPublicacion.getRazaFk();
                    publicacionListNewPublicacion.setRazaFk(raza);
                    publicacionListNewPublicacion = em.merge(publicacionListNewPublicacion);
                    if (oldRazaFkOfPublicacionListNewPublicacion != null && !oldRazaFkOfPublicacionListNewPublicacion.equals(raza)) {
                        oldRazaFkOfPublicacionListNewPublicacion.getPublicacionList().remove(publicacionListNewPublicacion);
                        oldRazaFkOfPublicacionListNewPublicacion = em.merge(oldRazaFkOfPublicacionListNewPublicacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = raza.getRazaId();
                if (findRaza(id) == null) {
                    throw new NonexistentEntityException("The raza with id " + id + " no longer exists.");
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
            Raza raza;
            try {
                raza = em.getReference(Raza.class, id);
                raza.getRazaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The raza with id " + id + " no longer exists.", enfe);
            }
            List<Publicacion> publicacionList = raza.getPublicacionList();
            for (Publicacion publicacionListPublicacion : publicacionList) {
                publicacionListPublicacion.setRazaFk(null);
                publicacionListPublicacion = em.merge(publicacionListPublicacion);
            }
            em.remove(raza);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Raza> findRazaEntities() {
        return findRazaEntities(true, -1, -1);
    }

    public List<Raza> findRazaEntities(int maxResults, int firstResult) {
        return findRazaEntities(false, maxResults, firstResult);
    }

    private List<Raza> findRazaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Raza.class));
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

    public Raza findRaza(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Raza.class, id);
        } finally {
            em.close();
        }
    }

    public int getRazaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Raza> rt = cq.from(Raza.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
