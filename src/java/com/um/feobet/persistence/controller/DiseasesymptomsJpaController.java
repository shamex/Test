/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Diseasesymptoms;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Symptoms;
import com.um.feobet.persistence.entity.Disease;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class DiseasesymptomsJpaController implements Serializable {

    public DiseasesymptomsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Diseasesymptoms diseasesymptoms) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Symptoms symptomsid = diseasesymptoms.getSymptomsid();
            if (symptomsid != null) {
                symptomsid = em.getReference(symptomsid.getClass(), symptomsid.getId());
                diseasesymptoms.setSymptomsid(symptomsid);
            }
            Disease diseaseid = diseasesymptoms.getDiseaseid();
            if (diseaseid != null) {
                diseaseid = em.getReference(diseaseid.getClass(), diseaseid.getId());
                diseasesymptoms.setDiseaseid(diseaseid);
            }
            em.persist(diseasesymptoms);
            if (symptomsid != null) {
                symptomsid.getDiseasesymptomsList().add(diseasesymptoms);
                symptomsid = em.merge(symptomsid);
            }
            if (diseaseid != null) {
                diseaseid.getDiseasesymptomsList().add(diseasesymptoms);
                diseaseid = em.merge(diseaseid);
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

    public void edit(Diseasesymptoms diseasesymptoms) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diseasesymptoms persistentDiseasesymptoms = em.find(Diseasesymptoms.class, diseasesymptoms.getId());
            Symptoms symptomsidOld = persistentDiseasesymptoms.getSymptomsid();
            Symptoms symptomsidNew = diseasesymptoms.getSymptomsid();
            Disease diseaseidOld = persistentDiseasesymptoms.getDiseaseid();
            Disease diseaseidNew = diseasesymptoms.getDiseaseid();
            if (symptomsidNew != null) {
                symptomsidNew = em.getReference(symptomsidNew.getClass(), symptomsidNew.getId());
                diseasesymptoms.setSymptomsid(symptomsidNew);
            }
            if (diseaseidNew != null) {
                diseaseidNew = em.getReference(diseaseidNew.getClass(), diseaseidNew.getId());
                diseasesymptoms.setDiseaseid(diseaseidNew);
            }
            diseasesymptoms = em.merge(diseasesymptoms);
            if (symptomsidOld != null && !symptomsidOld.equals(symptomsidNew)) {
                symptomsidOld.getDiseasesymptomsList().remove(diseasesymptoms);
                symptomsidOld = em.merge(symptomsidOld);
            }
            if (symptomsidNew != null && !symptomsidNew.equals(symptomsidOld)) {
                symptomsidNew.getDiseasesymptomsList().add(diseasesymptoms);
                symptomsidNew = em.merge(symptomsidNew);
            }
            if (diseaseidOld != null && !diseaseidOld.equals(diseaseidNew)) {
                diseaseidOld.getDiseasesymptomsList().remove(diseasesymptoms);
                diseaseidOld = em.merge(diseaseidOld);
            }
            if (diseaseidNew != null && !diseaseidNew.equals(diseaseidOld)) {
                diseaseidNew.getDiseasesymptomsList().add(diseasesymptoms);
                diseaseidNew = em.merge(diseaseidNew);
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
                Integer id = diseasesymptoms.getId();
                if (findDiseasesymptoms(id) == null) {
                    throw new NonexistentEntityException("The diseasesymptoms with id " + id + " no longer exists.");
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
            Diseasesymptoms diseasesymptoms;
            try {
                diseasesymptoms = em.getReference(Diseasesymptoms.class, id);
                diseasesymptoms.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diseasesymptoms with id " + id + " no longer exists.", enfe);
            }
            Symptoms symptomsid = diseasesymptoms.getSymptomsid();
            if (symptomsid != null) {
                symptomsid.getDiseasesymptomsList().remove(diseasesymptoms);
                symptomsid = em.merge(symptomsid);
            }
            Disease diseaseid = diseasesymptoms.getDiseaseid();
            if (diseaseid != null) {
                diseaseid.getDiseasesymptomsList().remove(diseasesymptoms);
                diseaseid = em.merge(diseaseid);
            }
            em.remove(diseasesymptoms);
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

    public List<Diseasesymptoms> findDiseasesymptomsEntities() {
        return findDiseasesymptomsEntities(true, -1, -1);
    }

    public List<Diseasesymptoms> findDiseasesymptomsEntities(int maxResults, int firstResult) {
        return findDiseasesymptomsEntities(false, maxResults, firstResult);
    }

    private List<Diseasesymptoms> findDiseasesymptomsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diseasesymptoms.class));
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

    public Diseasesymptoms findDiseasesymptoms(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diseasesymptoms.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiseasesymptomsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diseasesymptoms> rt = cq.from(Diseasesymptoms.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
