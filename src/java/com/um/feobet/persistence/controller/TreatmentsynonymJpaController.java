/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Treatmentsynonym;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Synonym;
import com.um.feobet.persistence.entity.Treatment;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class TreatmentsynonymJpaController implements Serializable {

    public TreatmentsynonymJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Treatmentsynonym treatmentsynonym) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Synonym synonymid = treatmentsynonym.getSynonymid();
            if (synonymid != null) {
                synonymid = em.getReference(synonymid.getClass(), synonymid.getId());
                treatmentsynonym.setSynonymid(synonymid);
            }
            Treatment treatmentid = treatmentsynonym.getTreatmentid();
            if (treatmentid != null) {
                treatmentid = em.getReference(treatmentid.getClass(), treatmentid.getId());
                treatmentsynonym.setTreatmentid(treatmentid);
            }
            em.persist(treatmentsynonym);
            if (synonymid != null) {
                synonymid.getTreatmentsynonymList().add(treatmentsynonym);
                synonymid = em.merge(synonymid);
            }
            if (treatmentid != null) {
                treatmentid.getTreatmentsynonymList().add(treatmentsynonym);
                treatmentid = em.merge(treatmentid);
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

    public void edit(Treatmentsynonym treatmentsynonym) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Treatmentsynonym persistentTreatmentsynonym = em.find(Treatmentsynonym.class, treatmentsynonym.getId());
            Synonym synonymidOld = persistentTreatmentsynonym.getSynonymid();
            Synonym synonymidNew = treatmentsynonym.getSynonymid();
            Treatment treatmentidOld = persistentTreatmentsynonym.getTreatmentid();
            Treatment treatmentidNew = treatmentsynonym.getTreatmentid();
            if (synonymidNew != null) {
                synonymidNew = em.getReference(synonymidNew.getClass(), synonymidNew.getId());
                treatmentsynonym.setSynonymid(synonymidNew);
            }
            if (treatmentidNew != null) {
                treatmentidNew = em.getReference(treatmentidNew.getClass(), treatmentidNew.getId());
                treatmentsynonym.setTreatmentid(treatmentidNew);
            }
            treatmentsynonym = em.merge(treatmentsynonym);
            if (synonymidOld != null && !synonymidOld.equals(synonymidNew)) {
                synonymidOld.getTreatmentsynonymList().remove(treatmentsynonym);
                synonymidOld = em.merge(synonymidOld);
            }
            if (synonymidNew != null && !synonymidNew.equals(synonymidOld)) {
                synonymidNew.getTreatmentsynonymList().add(treatmentsynonym);
                synonymidNew = em.merge(synonymidNew);
            }
            if (treatmentidOld != null && !treatmentidOld.equals(treatmentidNew)) {
                treatmentidOld.getTreatmentsynonymList().remove(treatmentsynonym);
                treatmentidOld = em.merge(treatmentidOld);
            }
            if (treatmentidNew != null && !treatmentidNew.equals(treatmentidOld)) {
                treatmentidNew.getTreatmentsynonymList().add(treatmentsynonym);
                treatmentidNew = em.merge(treatmentidNew);
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
                Integer id = treatmentsynonym.getId();
                if (findTreatmentsynonym(id) == null) {
                    throw new NonexistentEntityException("The treatmentsynonym with id " + id + " no longer exists.");
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
            Treatmentsynonym treatmentsynonym;
            try {
                treatmentsynonym = em.getReference(Treatmentsynonym.class, id);
                treatmentsynonym.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The treatmentsynonym with id " + id + " no longer exists.", enfe);
            }
            Synonym synonymid = treatmentsynonym.getSynonymid();
            if (synonymid != null) {
                synonymid.getTreatmentsynonymList().remove(treatmentsynonym);
                synonymid = em.merge(synonymid);
            }
            Treatment treatmentid = treatmentsynonym.getTreatmentid();
            if (treatmentid != null) {
                treatmentid.getTreatmentsynonymList().remove(treatmentsynonym);
                treatmentid = em.merge(treatmentid);
            }
            em.remove(treatmentsynonym);
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

    public List<Treatmentsynonym> findTreatmentsynonymEntities() {
        return findTreatmentsynonymEntities(true, -1, -1);
    }

    public List<Treatmentsynonym> findTreatmentsynonymEntities(int maxResults, int firstResult) {
        return findTreatmentsynonymEntities(false, maxResults, firstResult);
    }

    private List<Treatmentsynonym> findTreatmentsynonymEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Treatmentsynonym.class));
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

    public Treatmentsynonym findTreatmentsynonym(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Treatmentsynonym.class, id);
        } finally {
            em.close();
        }
    }

    public int getTreatmentsynonymCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Treatmentsynonym> rt = cq.from(Treatmentsynonym.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
