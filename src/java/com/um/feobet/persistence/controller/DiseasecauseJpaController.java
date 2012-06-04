/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Diseasecause;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Disease;
import com.um.feobet.persistence.entity.Cause;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class DiseasecauseJpaController implements Serializable {

    public DiseasecauseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Diseasecause diseasecause) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Disease diseaseid = diseasecause.getDiseaseid();
            if (diseaseid != null) {
                diseaseid = em.getReference(diseaseid.getClass(), diseaseid.getId());
                diseasecause.setDiseaseid(diseaseid);
            }
            Cause causeid = diseasecause.getCauseid();
            if (causeid != null) {
                causeid = em.getReference(causeid.getClass(), causeid.getId());
                diseasecause.setCauseid(causeid);
            }
            em.persist(diseasecause);
            if (diseaseid != null) {
                diseaseid.getDiseasecauseList().add(diseasecause);
                diseaseid = em.merge(diseaseid);
            }
            if (causeid != null) {
                causeid.getDiseasecauseList().add(diseasecause);
                causeid = em.merge(causeid);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Diseasecause diseasecause) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diseasecause persistentDiseasecause = em.find(Diseasecause.class, diseasecause.getId());
            Disease diseaseidOld = persistentDiseasecause.getDiseaseid();
            Disease diseaseidNew = diseasecause.getDiseaseid();
            Cause causeidOld = persistentDiseasecause.getCauseid();
            Cause causeidNew = diseasecause.getCauseid();
            if (diseaseidNew != null) {
                diseaseidNew = em.getReference(diseaseidNew.getClass(), diseaseidNew.getId());
                diseasecause.setDiseaseid(diseaseidNew);
            }
            if (causeidNew != null) {
                causeidNew = em.getReference(causeidNew.getClass(), causeidNew.getId());
                diseasecause.setCauseid(causeidNew);
            }
            diseasecause = em.merge(diseasecause);
            if (diseaseidOld != null && !diseaseidOld.equals(diseaseidNew)) {
                diseaseidOld.getDiseasecauseList().remove(diseasecause);
                diseaseidOld = em.merge(diseaseidOld);
            }
            if (diseaseidNew != null && !diseaseidNew.equals(diseaseidOld)) {
                diseaseidNew.getDiseasecauseList().add(diseasecause);
                diseaseidNew = em.merge(diseaseidNew);
            }
            if (causeidOld != null && !causeidOld.equals(causeidNew)) {
                causeidOld.getDiseasecauseList().remove(diseasecause);
                causeidOld = em.merge(causeidOld);
            }
            if (causeidNew != null && !causeidNew.equals(causeidOld)) {
                causeidNew.getDiseasecauseList().add(diseasecause);
                causeidNew = em.merge(causeidNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = diseasecause.getId();
                if (findDiseasecause(id) == null) {
                    throw new NonexistentEntityException("The diseasecause with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diseasecause diseasecause;
            try {
                diseasecause = em.getReference(Diseasecause.class, id);
                diseasecause.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diseasecause with id " + id + " no longer exists.", enfe);
            }
            Disease diseaseid = diseasecause.getDiseaseid();
            if (diseaseid != null) {
                diseaseid.getDiseasecauseList().remove(diseasecause);
                diseaseid = em.merge(diseaseid);
            }
            Cause causeid = diseasecause.getCauseid();
            if (causeid != null) {
                causeid.getDiseasecauseList().remove(diseasecause);
                causeid = em.merge(causeid);
            }
            em.remove(diseasecause);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Diseasecause> findDiseasecauseEntities() {
        return findDiseasecauseEntities(true, -1, -1);
    }

    public List<Diseasecause> findDiseasecauseEntities(int maxResults, int firstResult) {
        return findDiseasecauseEntities(false, maxResults, firstResult);
    }

    private List<Diseasecause> findDiseasecauseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diseasecause.class));
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

    public Diseasecause findDiseasecause(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diseasecause.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiseasecauseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diseasecause> rt = cq.from(Diseasecause.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
