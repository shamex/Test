/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Treatment;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Treatmentsynonym;
import java.util.ArrayList;
import java.util.List;
import com.um.feobet.persistence.entity.Diseasetreatment;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class TreatmentJpaController implements Serializable {

    public TreatmentJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Treatment treatment) throws RollbackFailureException, Exception {
        if (treatment.getTreatmentsynonymList() == null) {
            treatment.setTreatmentsynonymList(new ArrayList<Treatmentsynonym>());
        }
        if (treatment.getDiseasetreatmentList() == null) {
            treatment.setDiseasetreatmentList(new ArrayList<Diseasetreatment>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Treatmentsynonym> attachedTreatmentsynonymList = new ArrayList<Treatmentsynonym>();
            for (Treatmentsynonym treatmentsynonymListTreatmentsynonymToAttach : treatment.getTreatmentsynonymList()) {
                treatmentsynonymListTreatmentsynonymToAttach = em.getReference(treatmentsynonymListTreatmentsynonymToAttach.getClass(), treatmentsynonymListTreatmentsynonymToAttach.getId());
                attachedTreatmentsynonymList.add(treatmentsynonymListTreatmentsynonymToAttach);
            }
            treatment.setTreatmentsynonymList(attachedTreatmentsynonymList);
            List<Diseasetreatment> attachedDiseasetreatmentList = new ArrayList<Diseasetreatment>();
            for (Diseasetreatment diseasetreatmentListDiseasetreatmentToAttach : treatment.getDiseasetreatmentList()) {
                diseasetreatmentListDiseasetreatmentToAttach = em.getReference(diseasetreatmentListDiseasetreatmentToAttach.getClass(), diseasetreatmentListDiseasetreatmentToAttach.getId());
                attachedDiseasetreatmentList.add(diseasetreatmentListDiseasetreatmentToAttach);
            }
            treatment.setDiseasetreatmentList(attachedDiseasetreatmentList);
            em.persist(treatment);
            for (Treatmentsynonym treatmentsynonymListTreatmentsynonym : treatment.getTreatmentsynonymList()) {
                Treatment oldTreatmentidOfTreatmentsynonymListTreatmentsynonym = treatmentsynonymListTreatmentsynonym.getTreatmentid();
                treatmentsynonymListTreatmentsynonym.setTreatmentid(treatment);
                treatmentsynonymListTreatmentsynonym = em.merge(treatmentsynonymListTreatmentsynonym);
                if (oldTreatmentidOfTreatmentsynonymListTreatmentsynonym != null) {
                    oldTreatmentidOfTreatmentsynonymListTreatmentsynonym.getTreatmentsynonymList().remove(treatmentsynonymListTreatmentsynonym);
                    oldTreatmentidOfTreatmentsynonymListTreatmentsynonym = em.merge(oldTreatmentidOfTreatmentsynonymListTreatmentsynonym);
                }
            }
            for (Diseasetreatment diseasetreatmentListDiseasetreatment : treatment.getDiseasetreatmentList()) {
                Treatment oldTreatmentidOfDiseasetreatmentListDiseasetreatment = diseasetreatmentListDiseasetreatment.getTreatmentid();
                diseasetreatmentListDiseasetreatment.setTreatmentid(treatment);
                diseasetreatmentListDiseasetreatment = em.merge(diseasetreatmentListDiseasetreatment);
                if (oldTreatmentidOfDiseasetreatmentListDiseasetreatment != null) {
                    oldTreatmentidOfDiseasetreatmentListDiseasetreatment.getDiseasetreatmentList().remove(diseasetreatmentListDiseasetreatment);
                    oldTreatmentidOfDiseasetreatmentListDiseasetreatment = em.merge(oldTreatmentidOfDiseasetreatmentListDiseasetreatment);
                }
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

    public void edit(Treatment treatment) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Treatment persistentTreatment = em.find(Treatment.class, treatment.getId());
            List<Treatmentsynonym> treatmentsynonymListOld = persistentTreatment.getTreatmentsynonymList();
            List<Treatmentsynonym> treatmentsynonymListNew = treatment.getTreatmentsynonymList();
            List<Diseasetreatment> diseasetreatmentListOld = persistentTreatment.getDiseasetreatmentList();
            List<Diseasetreatment> diseasetreatmentListNew = treatment.getDiseasetreatmentList();
            List<Treatmentsynonym> attachedTreatmentsynonymListNew = new ArrayList<Treatmentsynonym>();
            for (Treatmentsynonym treatmentsynonymListNewTreatmentsynonymToAttach : treatmentsynonymListNew) {
                treatmentsynonymListNewTreatmentsynonymToAttach = em.getReference(treatmentsynonymListNewTreatmentsynonymToAttach.getClass(), treatmentsynonymListNewTreatmentsynonymToAttach.getId());
                attachedTreatmentsynonymListNew.add(treatmentsynonymListNewTreatmentsynonymToAttach);
            }
            treatmentsynonymListNew = attachedTreatmentsynonymListNew;
            treatment.setTreatmentsynonymList(treatmentsynonymListNew);
            List<Diseasetreatment> attachedDiseasetreatmentListNew = new ArrayList<Diseasetreatment>();
            for (Diseasetreatment diseasetreatmentListNewDiseasetreatmentToAttach : diseasetreatmentListNew) {
                diseasetreatmentListNewDiseasetreatmentToAttach = em.getReference(diseasetreatmentListNewDiseasetreatmentToAttach.getClass(), diseasetreatmentListNewDiseasetreatmentToAttach.getId());
                attachedDiseasetreatmentListNew.add(diseasetreatmentListNewDiseasetreatmentToAttach);
            }
            diseasetreatmentListNew = attachedDiseasetreatmentListNew;
            treatment.setDiseasetreatmentList(diseasetreatmentListNew);
            treatment = em.merge(treatment);
            for (Treatmentsynonym treatmentsynonymListOldTreatmentsynonym : treatmentsynonymListOld) {
                if (!treatmentsynonymListNew.contains(treatmentsynonymListOldTreatmentsynonym)) {
                    treatmentsynonymListOldTreatmentsynonym.setTreatmentid(null);
                    treatmentsynonymListOldTreatmentsynonym = em.merge(treatmentsynonymListOldTreatmentsynonym);
                }
            }
            for (Treatmentsynonym treatmentsynonymListNewTreatmentsynonym : treatmentsynonymListNew) {
                if (!treatmentsynonymListOld.contains(treatmentsynonymListNewTreatmentsynonym)) {
                    Treatment oldTreatmentidOfTreatmentsynonymListNewTreatmentsynonym = treatmentsynonymListNewTreatmentsynonym.getTreatmentid();
                    treatmentsynonymListNewTreatmentsynonym.setTreatmentid(treatment);
                    treatmentsynonymListNewTreatmentsynonym = em.merge(treatmentsynonymListNewTreatmentsynonym);
                    if (oldTreatmentidOfTreatmentsynonymListNewTreatmentsynonym != null && !oldTreatmentidOfTreatmentsynonymListNewTreatmentsynonym.equals(treatment)) {
                        oldTreatmentidOfTreatmentsynonymListNewTreatmentsynonym.getTreatmentsynonymList().remove(treatmentsynonymListNewTreatmentsynonym);
                        oldTreatmentidOfTreatmentsynonymListNewTreatmentsynonym = em.merge(oldTreatmentidOfTreatmentsynonymListNewTreatmentsynonym);
                    }
                }
            }
            for (Diseasetreatment diseasetreatmentListOldDiseasetreatment : diseasetreatmentListOld) {
                if (!diseasetreatmentListNew.contains(diseasetreatmentListOldDiseasetreatment)) {
                    diseasetreatmentListOldDiseasetreatment.setTreatmentid(null);
                    diseasetreatmentListOldDiseasetreatment = em.merge(diseasetreatmentListOldDiseasetreatment);
                }
            }
            for (Diseasetreatment diseasetreatmentListNewDiseasetreatment : diseasetreatmentListNew) {
                if (!diseasetreatmentListOld.contains(diseasetreatmentListNewDiseasetreatment)) {
                    Treatment oldTreatmentidOfDiseasetreatmentListNewDiseasetreatment = diseasetreatmentListNewDiseasetreatment.getTreatmentid();
                    diseasetreatmentListNewDiseasetreatment.setTreatmentid(treatment);
                    diseasetreatmentListNewDiseasetreatment = em.merge(diseasetreatmentListNewDiseasetreatment);
                    if (oldTreatmentidOfDiseasetreatmentListNewDiseasetreatment != null && !oldTreatmentidOfDiseasetreatmentListNewDiseasetreatment.equals(treatment)) {
                        oldTreatmentidOfDiseasetreatmentListNewDiseasetreatment.getDiseasetreatmentList().remove(diseasetreatmentListNewDiseasetreatment);
                        oldTreatmentidOfDiseasetreatmentListNewDiseasetreatment = em.merge(oldTreatmentidOfDiseasetreatmentListNewDiseasetreatment);
                    }
                }
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
                Integer id = treatment.getId();
                if (findTreatment(id) == null) {
                    throw new NonexistentEntityException("The treatment with id " + id + " no longer exists.");
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
            Treatment treatment;
            try {
                treatment = em.getReference(Treatment.class, id);
                treatment.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The treatment with id " + id + " no longer exists.", enfe);
            }
            List<Treatmentsynonym> treatmentsynonymList = treatment.getTreatmentsynonymList();
            for (Treatmentsynonym treatmentsynonymListTreatmentsynonym : treatmentsynonymList) {
                treatmentsynonymListTreatmentsynonym.setTreatmentid(null);
                treatmentsynonymListTreatmentsynonym = em.merge(treatmentsynonymListTreatmentsynonym);
            }
            List<Diseasetreatment> diseasetreatmentList = treatment.getDiseasetreatmentList();
            for (Diseasetreatment diseasetreatmentListDiseasetreatment : diseasetreatmentList) {
                diseasetreatmentListDiseasetreatment.setTreatmentid(null);
                diseasetreatmentListDiseasetreatment = em.merge(diseasetreatmentListDiseasetreatment);
            }
            em.remove(treatment);
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

    public List<Treatment> findTreatmentEntities() {
        return findTreatmentEntities(true, -1, -1);
    }

    public List<Treatment> findTreatmentEntities(int maxResults, int firstResult) {
        return findTreatmentEntities(false, maxResults, firstResult);
    }

    private List<Treatment> findTreatmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Treatment.class));
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

    public Treatment findTreatment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Treatment.class, id);
        } finally {
            em.close();
        }
    }

    public int getTreatmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Treatment> rt = cq.from(Treatment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
