/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Symptoms;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Diseasesymptoms;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class SymptomsJpaController extends BaseJpaController {

//    public SymptomsJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
    @Resource
    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;

    @Override
    public EntityManager getEntityManager() {
        return super.getEntityManager();
    }

    public void create(Symptoms symptoms) throws RollbackFailureException, Exception {
        if (symptoms.getDiseasesymptomsList() == null) {
            symptoms.setDiseasesymptomsList(new ArrayList<Diseasesymptoms>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Diseasesymptoms> attachedDiseasesymptomsList = new ArrayList<Diseasesymptoms>();
            for (Diseasesymptoms diseasesymptomsListDiseasesymptomsToAttach : symptoms.getDiseasesymptomsList()) {
                diseasesymptomsListDiseasesymptomsToAttach = em.getReference(diseasesymptomsListDiseasesymptomsToAttach.getClass(), diseasesymptomsListDiseasesymptomsToAttach.getId());
                attachedDiseasesymptomsList.add(diseasesymptomsListDiseasesymptomsToAttach);
            }
            symptoms.setDiseasesymptomsList(attachedDiseasesymptomsList);
            em.persist(symptoms);
            for (Diseasesymptoms diseasesymptomsListDiseasesymptoms : symptoms.getDiseasesymptomsList()) {
                Symptoms oldSymptomsidOfDiseasesymptomsListDiseasesymptoms = diseasesymptomsListDiseasesymptoms.getSymptomsid();
                diseasesymptomsListDiseasesymptoms.setSymptomsid(symptoms);
                diseasesymptomsListDiseasesymptoms = em.merge(diseasesymptomsListDiseasesymptoms);
                if (oldSymptomsidOfDiseasesymptomsListDiseasesymptoms != null) {
                    oldSymptomsidOfDiseasesymptomsListDiseasesymptoms.getDiseasesymptomsList().remove(diseasesymptomsListDiseasesymptoms);
                    oldSymptomsidOfDiseasesymptomsListDiseasesymptoms = em.merge(oldSymptomsidOfDiseasesymptomsListDiseasesymptoms);
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

    public void edit(Symptoms symptoms) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Symptoms persistentSymptoms = em.find(Symptoms.class, symptoms.getId());
            List<Diseasesymptoms> diseasesymptomsListOld = persistentSymptoms.getDiseasesymptomsList();
            List<Diseasesymptoms> diseasesymptomsListNew = symptoms.getDiseasesymptomsList();
            List<Diseasesymptoms> attachedDiseasesymptomsListNew = new ArrayList<Diseasesymptoms>();
            for (Diseasesymptoms diseasesymptomsListNewDiseasesymptomsToAttach : diseasesymptomsListNew) {
                diseasesymptomsListNewDiseasesymptomsToAttach = em.getReference(diseasesymptomsListNewDiseasesymptomsToAttach.getClass(), diseasesymptomsListNewDiseasesymptomsToAttach.getId());
                attachedDiseasesymptomsListNew.add(diseasesymptomsListNewDiseasesymptomsToAttach);
            }
            diseasesymptomsListNew = attachedDiseasesymptomsListNew;
            symptoms.setDiseasesymptomsList(diseasesymptomsListNew);
            symptoms = em.merge(symptoms);
            for (Diseasesymptoms diseasesymptomsListOldDiseasesymptoms : diseasesymptomsListOld) {
                if (!diseasesymptomsListNew.contains(diseasesymptomsListOldDiseasesymptoms)) {
                    diseasesymptomsListOldDiseasesymptoms.setSymptomsid(null);
                    diseasesymptomsListOldDiseasesymptoms = em.merge(diseasesymptomsListOldDiseasesymptoms);
                }
            }
            for (Diseasesymptoms diseasesymptomsListNewDiseasesymptoms : diseasesymptomsListNew) {
                if (!diseasesymptomsListOld.contains(diseasesymptomsListNewDiseasesymptoms)) {
                    Symptoms oldSymptomsidOfDiseasesymptomsListNewDiseasesymptoms = diseasesymptomsListNewDiseasesymptoms.getSymptomsid();
                    diseasesymptomsListNewDiseasesymptoms.setSymptomsid(symptoms);
                    diseasesymptomsListNewDiseasesymptoms = em.merge(diseasesymptomsListNewDiseasesymptoms);
                    if (oldSymptomsidOfDiseasesymptomsListNewDiseasesymptoms != null && !oldSymptomsidOfDiseasesymptomsListNewDiseasesymptoms.equals(symptoms)) {
                        oldSymptomsidOfDiseasesymptomsListNewDiseasesymptoms.getDiseasesymptomsList().remove(diseasesymptomsListNewDiseasesymptoms);
                        oldSymptomsidOfDiseasesymptomsListNewDiseasesymptoms = em.merge(oldSymptomsidOfDiseasesymptomsListNewDiseasesymptoms);
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
                Integer id = symptoms.getId();
                if (findSymptoms(id) == null) {
                    throw new NonexistentEntityException("The symptoms with id " + id + " no longer exists.");
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
            Symptoms symptoms;
            try {
                symptoms = em.getReference(Symptoms.class, id);
                symptoms.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The symptoms with id " + id + " no longer exists.", enfe);
            }
            List<Diseasesymptoms> diseasesymptomsList = symptoms.getDiseasesymptomsList();
            for (Diseasesymptoms diseasesymptomsListDiseasesymptoms : diseasesymptomsList) {
                diseasesymptomsListDiseasesymptoms.setSymptomsid(null);
                diseasesymptomsListDiseasesymptoms = em.merge(diseasesymptomsListDiseasesymptoms);
            }
            em.remove(symptoms);
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

    public List<Symptoms> findSymptomsEntities() {
        return findSymptomsEntities(true, -1, -1);
    }

    public List<Symptoms> findSymptomsEntities(int maxResults, int firstResult) {
        return findSymptomsEntities(false, maxResults, firstResult);
    }

    private List<Symptoms> findSymptomsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Symptoms.class));
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

    public Symptoms findSymptoms(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Symptoms.class, id);
        } finally {
            em.close();
        }
    }

    public int getSymptomsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Symptoms> rt = cq.from(Symptoms.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
