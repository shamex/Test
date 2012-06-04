/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Diseasetreatment;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Treatment;
import com.um.feobet.persistence.entity.Disease;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class DiseasetreatmentJpaController implements Serializable {

    public DiseasetreatmentJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Diseasetreatment diseasetreatment) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Treatment treatmentid = diseasetreatment.getTreatmentid();
            if (treatmentid != null) {
                treatmentid = em.getReference(treatmentid.getClass(), treatmentid.getId());
                diseasetreatment.setTreatmentid(treatmentid);
            }
            Disease diseaseid = diseasetreatment.getDiseaseid();
            if (diseaseid != null) {
                diseaseid = em.getReference(diseaseid.getClass(), diseaseid.getId());
                diseasetreatment.setDiseaseid(diseaseid);
            }
            em.persist(diseasetreatment);
            if (treatmentid != null) {
                treatmentid.getDiseasetreatmentList().add(diseasetreatment);
                treatmentid = em.merge(treatmentid);
            }
            if (diseaseid != null) {
                diseaseid.getDiseasetreatmentList().add(diseasetreatment);
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

    public void edit(Diseasetreatment diseasetreatment) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diseasetreatment persistentDiseasetreatment = em.find(Diseasetreatment.class, diseasetreatment.getId());
            Treatment treatmentidOld = persistentDiseasetreatment.getTreatmentid();
            Treatment treatmentidNew = diseasetreatment.getTreatmentid();
            Disease diseaseidOld = persistentDiseasetreatment.getDiseaseid();
            Disease diseaseidNew = diseasetreatment.getDiseaseid();
            if (treatmentidNew != null) {
                treatmentidNew = em.getReference(treatmentidNew.getClass(), treatmentidNew.getId());
                diseasetreatment.setTreatmentid(treatmentidNew);
            }
            if (diseaseidNew != null) {
                diseaseidNew = em.getReference(diseaseidNew.getClass(), diseaseidNew.getId());
                diseasetreatment.setDiseaseid(diseaseidNew);
            }
            diseasetreatment = em.merge(diseasetreatment);
            if (treatmentidOld != null && !treatmentidOld.equals(treatmentidNew)) {
                treatmentidOld.getDiseasetreatmentList().remove(diseasetreatment);
                treatmentidOld = em.merge(treatmentidOld);
            }
            if (treatmentidNew != null && !treatmentidNew.equals(treatmentidOld)) {
                treatmentidNew.getDiseasetreatmentList().add(diseasetreatment);
                treatmentidNew = em.merge(treatmentidNew);
            }
            if (diseaseidOld != null && !diseaseidOld.equals(diseaseidNew)) {
                diseaseidOld.getDiseasetreatmentList().remove(diseasetreatment);
                diseaseidOld = em.merge(diseaseidOld);
            }
            if (diseaseidNew != null && !diseaseidNew.equals(diseaseidOld)) {
                diseaseidNew.getDiseasetreatmentList().add(diseasetreatment);
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
                Integer id = diseasetreatment.getId();
                if (findDiseasetreatment(id) == null) {
                    throw new NonexistentEntityException("The diseasetreatment with id " + id + " no longer exists.");
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
            Diseasetreatment diseasetreatment;
            try {
                diseasetreatment = em.getReference(Diseasetreatment.class, id);
                diseasetreatment.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diseasetreatment with id " + id + " no longer exists.", enfe);
            }
            Treatment treatmentid = diseasetreatment.getTreatmentid();
            if (treatmentid != null) {
                treatmentid.getDiseasetreatmentList().remove(diseasetreatment);
                treatmentid = em.merge(treatmentid);
            }
            Disease diseaseid = diseasetreatment.getDiseaseid();
            if (diseaseid != null) {
                diseaseid.getDiseasetreatmentList().remove(diseasetreatment);
                diseaseid = em.merge(diseaseid);
            }
            em.remove(diseasetreatment);
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

    public List<Diseasetreatment> findDiseasetreatmentEntities() {
        return findDiseasetreatmentEntities(true, -1, -1);
    }

    public List<Diseasetreatment> findDiseasetreatmentEntities(int maxResults, int firstResult) {
        return findDiseasetreatmentEntities(false, maxResults, firstResult);
    }

    private List<Diseasetreatment> findDiseasetreatmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diseasetreatment.class));
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

    public Diseasetreatment findDiseasetreatment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diseasetreatment.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiseasetreatmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diseasetreatment> rt = cq.from(Diseasetreatment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
