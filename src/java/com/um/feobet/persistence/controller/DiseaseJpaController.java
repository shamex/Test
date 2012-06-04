/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Disease;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Diseasesymptoms;
import java.util.ArrayList;
import java.util.List;
import com.um.feobet.persistence.entity.Diseasecause;
import com.um.feobet.persistence.entity.Diseasetreatment;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class DiseaseJpaController implements Serializable {

    public DiseaseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Disease disease) throws RollbackFailureException, Exception {
        if (disease.getDiseasesymptomsList() == null) {
            disease.setDiseasesymptomsList(new ArrayList<Diseasesymptoms>());
        }
        if (disease.getDiseasecauseList() == null) {
            disease.setDiseasecauseList(new ArrayList<Diseasecause>());
        }
        if (disease.getDiseasetreatmentList() == null) {
            disease.setDiseasetreatmentList(new ArrayList<Diseasetreatment>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Diseasesymptoms> attachedDiseasesymptomsList = new ArrayList<Diseasesymptoms>();
            for (Diseasesymptoms diseasesymptomsListDiseasesymptomsToAttach : disease.getDiseasesymptomsList()) {
                diseasesymptomsListDiseasesymptomsToAttach = em.getReference(diseasesymptomsListDiseasesymptomsToAttach.getClass(), diseasesymptomsListDiseasesymptomsToAttach.getId());
                attachedDiseasesymptomsList.add(diseasesymptomsListDiseasesymptomsToAttach);
            }
            disease.setDiseasesymptomsList(attachedDiseasesymptomsList);
            List<Diseasecause> attachedDiseasecauseList = new ArrayList<Diseasecause>();
            for (Diseasecause diseasecauseListDiseasecauseToAttach : disease.getDiseasecauseList()) {
                diseasecauseListDiseasecauseToAttach = em.getReference(diseasecauseListDiseasecauseToAttach.getClass(), diseasecauseListDiseasecauseToAttach.getId());
                attachedDiseasecauseList.add(diseasecauseListDiseasecauseToAttach);
            }
            disease.setDiseasecauseList(attachedDiseasecauseList);
            List<Diseasetreatment> attachedDiseasetreatmentList = new ArrayList<Diseasetreatment>();
            for (Diseasetreatment diseasetreatmentListDiseasetreatmentToAttach : disease.getDiseasetreatmentList()) {
                diseasetreatmentListDiseasetreatmentToAttach = em.getReference(diseasetreatmentListDiseasetreatmentToAttach.getClass(), diseasetreatmentListDiseasetreatmentToAttach.getId());
                attachedDiseasetreatmentList.add(diseasetreatmentListDiseasetreatmentToAttach);
            }
            disease.setDiseasetreatmentList(attachedDiseasetreatmentList);
            em.persist(disease);
            for (Diseasesymptoms diseasesymptomsListDiseasesymptoms : disease.getDiseasesymptomsList()) {
                Disease oldDiseaseidOfDiseasesymptomsListDiseasesymptoms = diseasesymptomsListDiseasesymptoms.getDiseaseid();
                diseasesymptomsListDiseasesymptoms.setDiseaseid(disease);
                diseasesymptomsListDiseasesymptoms = em.merge(diseasesymptomsListDiseasesymptoms);
                if (oldDiseaseidOfDiseasesymptomsListDiseasesymptoms != null) {
                    oldDiseaseidOfDiseasesymptomsListDiseasesymptoms.getDiseasesymptomsList().remove(diseasesymptomsListDiseasesymptoms);
                    oldDiseaseidOfDiseasesymptomsListDiseasesymptoms = em.merge(oldDiseaseidOfDiseasesymptomsListDiseasesymptoms);
                }
            }
            for (Diseasecause diseasecauseListDiseasecause : disease.getDiseasecauseList()) {
                Disease oldDiseaseidOfDiseasecauseListDiseasecause = diseasecauseListDiseasecause.getDiseaseid();
                diseasecauseListDiseasecause.setDiseaseid(disease);
                diseasecauseListDiseasecause = em.merge(diseasecauseListDiseasecause);
                if (oldDiseaseidOfDiseasecauseListDiseasecause != null) {
                    oldDiseaseidOfDiseasecauseListDiseasecause.getDiseasecauseList().remove(diseasecauseListDiseasecause);
                    oldDiseaseidOfDiseasecauseListDiseasecause = em.merge(oldDiseaseidOfDiseasecauseListDiseasecause);
                }
            }
            for (Diseasetreatment diseasetreatmentListDiseasetreatment : disease.getDiseasetreatmentList()) {
                Disease oldDiseaseidOfDiseasetreatmentListDiseasetreatment = diseasetreatmentListDiseasetreatment.getDiseaseid();
                diseasetreatmentListDiseasetreatment.setDiseaseid(disease);
                diseasetreatmentListDiseasetreatment = em.merge(diseasetreatmentListDiseasetreatment);
                if (oldDiseaseidOfDiseasetreatmentListDiseasetreatment != null) {
                    oldDiseaseidOfDiseasetreatmentListDiseasetreatment.getDiseasetreatmentList().remove(diseasetreatmentListDiseasetreatment);
                    oldDiseaseidOfDiseasetreatmentListDiseasetreatment = em.merge(oldDiseaseidOfDiseasetreatmentListDiseasetreatment);
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

    public void edit(Disease disease) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Disease persistentDisease = em.find(Disease.class, disease.getId());
            List<Diseasesymptoms> diseasesymptomsListOld = persistentDisease.getDiseasesymptomsList();
            List<Diseasesymptoms> diseasesymptomsListNew = disease.getDiseasesymptomsList();
            List<Diseasecause> diseasecauseListOld = persistentDisease.getDiseasecauseList();
            List<Diseasecause> diseasecauseListNew = disease.getDiseasecauseList();
            List<Diseasetreatment> diseasetreatmentListOld = persistentDisease.getDiseasetreatmentList();
            List<Diseasetreatment> diseasetreatmentListNew = disease.getDiseasetreatmentList();
            List<Diseasesymptoms> attachedDiseasesymptomsListNew = new ArrayList<Diseasesymptoms>();
            for (Diseasesymptoms diseasesymptomsListNewDiseasesymptomsToAttach : diseasesymptomsListNew) {
                diseasesymptomsListNewDiseasesymptomsToAttach = em.getReference(diseasesymptomsListNewDiseasesymptomsToAttach.getClass(), diseasesymptomsListNewDiseasesymptomsToAttach.getId());
                attachedDiseasesymptomsListNew.add(diseasesymptomsListNewDiseasesymptomsToAttach);
            }
            diseasesymptomsListNew = attachedDiseasesymptomsListNew;
            disease.setDiseasesymptomsList(diseasesymptomsListNew);
            List<Diseasecause> attachedDiseasecauseListNew = new ArrayList<Diseasecause>();
            for (Diseasecause diseasecauseListNewDiseasecauseToAttach : diseasecauseListNew) {
                diseasecauseListNewDiseasecauseToAttach = em.getReference(diseasecauseListNewDiseasecauseToAttach.getClass(), diseasecauseListNewDiseasecauseToAttach.getId());
                attachedDiseasecauseListNew.add(diseasecauseListNewDiseasecauseToAttach);
            }
            diseasecauseListNew = attachedDiseasecauseListNew;
            disease.setDiseasecauseList(diseasecauseListNew);
            List<Diseasetreatment> attachedDiseasetreatmentListNew = new ArrayList<Diseasetreatment>();
            for (Diseasetreatment diseasetreatmentListNewDiseasetreatmentToAttach : diseasetreatmentListNew) {
                diseasetreatmentListNewDiseasetreatmentToAttach = em.getReference(diseasetreatmentListNewDiseasetreatmentToAttach.getClass(), diseasetreatmentListNewDiseasetreatmentToAttach.getId());
                attachedDiseasetreatmentListNew.add(diseasetreatmentListNewDiseasetreatmentToAttach);
            }
            diseasetreatmentListNew = attachedDiseasetreatmentListNew;
            disease.setDiseasetreatmentList(diseasetreatmentListNew);
            disease = em.merge(disease);
            for (Diseasesymptoms diseasesymptomsListOldDiseasesymptoms : diseasesymptomsListOld) {
                if (!diseasesymptomsListNew.contains(diseasesymptomsListOldDiseasesymptoms)) {
                    diseasesymptomsListOldDiseasesymptoms.setDiseaseid(null);
                    diseasesymptomsListOldDiseasesymptoms = em.merge(diseasesymptomsListOldDiseasesymptoms);
                }
            }
            for (Diseasesymptoms diseasesymptomsListNewDiseasesymptoms : diseasesymptomsListNew) {
                if (!diseasesymptomsListOld.contains(diseasesymptomsListNewDiseasesymptoms)) {
                    Disease oldDiseaseidOfDiseasesymptomsListNewDiseasesymptoms = diseasesymptomsListNewDiseasesymptoms.getDiseaseid();
                    diseasesymptomsListNewDiseasesymptoms.setDiseaseid(disease);
                    diseasesymptomsListNewDiseasesymptoms = em.merge(diseasesymptomsListNewDiseasesymptoms);
                    if (oldDiseaseidOfDiseasesymptomsListNewDiseasesymptoms != null && !oldDiseaseidOfDiseasesymptomsListNewDiseasesymptoms.equals(disease)) {
                        oldDiseaseidOfDiseasesymptomsListNewDiseasesymptoms.getDiseasesymptomsList().remove(diseasesymptomsListNewDiseasesymptoms);
                        oldDiseaseidOfDiseasesymptomsListNewDiseasesymptoms = em.merge(oldDiseaseidOfDiseasesymptomsListNewDiseasesymptoms);
                    }
                }
            }
            for (Diseasecause diseasecauseListOldDiseasecause : diseasecauseListOld) {
                if (!diseasecauseListNew.contains(diseasecauseListOldDiseasecause)) {
                    diseasecauseListOldDiseasecause.setDiseaseid(null);
                    diseasecauseListOldDiseasecause = em.merge(diseasecauseListOldDiseasecause);
                }
            }
            for (Diseasecause diseasecauseListNewDiseasecause : diseasecauseListNew) {
                if (!diseasecauseListOld.contains(diseasecauseListNewDiseasecause)) {
                    Disease oldDiseaseidOfDiseasecauseListNewDiseasecause = diseasecauseListNewDiseasecause.getDiseaseid();
                    diseasecauseListNewDiseasecause.setDiseaseid(disease);
                    diseasecauseListNewDiseasecause = em.merge(diseasecauseListNewDiseasecause);
                    if (oldDiseaseidOfDiseasecauseListNewDiseasecause != null && !oldDiseaseidOfDiseasecauseListNewDiseasecause.equals(disease)) {
                        oldDiseaseidOfDiseasecauseListNewDiseasecause.getDiseasecauseList().remove(diseasecauseListNewDiseasecause);
                        oldDiseaseidOfDiseasecauseListNewDiseasecause = em.merge(oldDiseaseidOfDiseasecauseListNewDiseasecause);
                    }
                }
            }
            for (Diseasetreatment diseasetreatmentListOldDiseasetreatment : diseasetreatmentListOld) {
                if (!diseasetreatmentListNew.contains(diseasetreatmentListOldDiseasetreatment)) {
                    diseasetreatmentListOldDiseasetreatment.setDiseaseid(null);
                    diseasetreatmentListOldDiseasetreatment = em.merge(diseasetreatmentListOldDiseasetreatment);
                }
            }
            for (Diseasetreatment diseasetreatmentListNewDiseasetreatment : diseasetreatmentListNew) {
                if (!diseasetreatmentListOld.contains(diseasetreatmentListNewDiseasetreatment)) {
                    Disease oldDiseaseidOfDiseasetreatmentListNewDiseasetreatment = diseasetreatmentListNewDiseasetreatment.getDiseaseid();
                    diseasetreatmentListNewDiseasetreatment.setDiseaseid(disease);
                    diseasetreatmentListNewDiseasetreatment = em.merge(diseasetreatmentListNewDiseasetreatment);
                    if (oldDiseaseidOfDiseasetreatmentListNewDiseasetreatment != null && !oldDiseaseidOfDiseasetreatmentListNewDiseasetreatment.equals(disease)) {
                        oldDiseaseidOfDiseasetreatmentListNewDiseasetreatment.getDiseasetreatmentList().remove(diseasetreatmentListNewDiseasetreatment);
                        oldDiseaseidOfDiseasetreatmentListNewDiseasetreatment = em.merge(oldDiseaseidOfDiseasetreatmentListNewDiseasetreatment);
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
                Integer id = disease.getId();
                if (findDisease(id) == null) {
                    throw new NonexistentEntityException("The disease with id " + id + " no longer exists.");
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
            Disease disease;
            try {
                disease = em.getReference(Disease.class, id);
                disease.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The disease with id " + id + " no longer exists.", enfe);
            }
            List<Diseasesymptoms> diseasesymptomsList = disease.getDiseasesymptomsList();
            for (Diseasesymptoms diseasesymptomsListDiseasesymptoms : diseasesymptomsList) {
                diseasesymptomsListDiseasesymptoms.setDiseaseid(null);
                diseasesymptomsListDiseasesymptoms = em.merge(diseasesymptomsListDiseasesymptoms);
            }
            List<Diseasecause> diseasecauseList = disease.getDiseasecauseList();
            for (Diseasecause diseasecauseListDiseasecause : diseasecauseList) {
                diseasecauseListDiseasecause.setDiseaseid(null);
                diseasecauseListDiseasecause = em.merge(diseasecauseListDiseasecause);
            }
            List<Diseasetreatment> diseasetreatmentList = disease.getDiseasetreatmentList();
            for (Diseasetreatment diseasetreatmentListDiseasetreatment : diseasetreatmentList) {
                diseasetreatmentListDiseasetreatment.setDiseaseid(null);
                diseasetreatmentListDiseasetreatment = em.merge(diseasetreatmentListDiseasetreatment);
            }
            em.remove(disease);
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

    public List<Disease> findDiseaseEntities() {
        return findDiseaseEntities(true, -1, -1);
    }

    public List<Disease> findDiseaseEntities(int maxResults, int firstResult) {
        return findDiseaseEntities(false, maxResults, firstResult);
    }

    private List<Disease> findDiseaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Disease.class));
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

    public Disease findDisease(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Disease.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiseaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Disease> rt = cq.from(Disease.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
