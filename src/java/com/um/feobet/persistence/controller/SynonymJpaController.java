/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Synonym;
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
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class SynonymJpaController implements Serializable {

    public SynonymJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Synonym synonym) throws RollbackFailureException, Exception {
        if (synonym.getTreatmentsynonymList() == null) {
            synonym.setTreatmentsynonymList(new ArrayList<Treatmentsynonym>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Treatmentsynonym> attachedTreatmentsynonymList = new ArrayList<Treatmentsynonym>();
            for (Treatmentsynonym treatmentsynonymListTreatmentsynonymToAttach : synonym.getTreatmentsynonymList()) {
                treatmentsynonymListTreatmentsynonymToAttach = em.getReference(treatmentsynonymListTreatmentsynonymToAttach.getClass(), treatmentsynonymListTreatmentsynonymToAttach.getId());
                attachedTreatmentsynonymList.add(treatmentsynonymListTreatmentsynonymToAttach);
            }
            synonym.setTreatmentsynonymList(attachedTreatmentsynonymList);
            em.persist(synonym);
            for (Treatmentsynonym treatmentsynonymListTreatmentsynonym : synonym.getTreatmentsynonymList()) {
                Synonym oldSynonymidOfTreatmentsynonymListTreatmentsynonym = treatmentsynonymListTreatmentsynonym.getSynonymid();
                treatmentsynonymListTreatmentsynonym.setSynonymid(synonym);
                treatmentsynonymListTreatmentsynonym = em.merge(treatmentsynonymListTreatmentsynonym);
                if (oldSynonymidOfTreatmentsynonymListTreatmentsynonym != null) {
                    oldSynonymidOfTreatmentsynonymListTreatmentsynonym.getTreatmentsynonymList().remove(treatmentsynonymListTreatmentsynonym);
                    oldSynonymidOfTreatmentsynonymListTreatmentsynonym = em.merge(oldSynonymidOfTreatmentsynonymListTreatmentsynonym);
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

    public void edit(Synonym synonym) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Synonym persistentSynonym = em.find(Synonym.class, synonym.getId());
            List<Treatmentsynonym> treatmentsynonymListOld = persistentSynonym.getTreatmentsynonymList();
            List<Treatmentsynonym> treatmentsynonymListNew = synonym.getTreatmentsynonymList();
            List<Treatmentsynonym> attachedTreatmentsynonymListNew = new ArrayList<Treatmentsynonym>();
            for (Treatmentsynonym treatmentsynonymListNewTreatmentsynonymToAttach : treatmentsynonymListNew) {
                treatmentsynonymListNewTreatmentsynonymToAttach = em.getReference(treatmentsynonymListNewTreatmentsynonymToAttach.getClass(), treatmentsynonymListNewTreatmentsynonymToAttach.getId());
                attachedTreatmentsynonymListNew.add(treatmentsynonymListNewTreatmentsynonymToAttach);
            }
            treatmentsynonymListNew = attachedTreatmentsynonymListNew;
            synonym.setTreatmentsynonymList(treatmentsynonymListNew);
            synonym = em.merge(synonym);
            for (Treatmentsynonym treatmentsynonymListOldTreatmentsynonym : treatmentsynonymListOld) {
                if (!treatmentsynonymListNew.contains(treatmentsynonymListOldTreatmentsynonym)) {
                    treatmentsynonymListOldTreatmentsynonym.setSynonymid(null);
                    treatmentsynonymListOldTreatmentsynonym = em.merge(treatmentsynonymListOldTreatmentsynonym);
                }
            }
            for (Treatmentsynonym treatmentsynonymListNewTreatmentsynonym : treatmentsynonymListNew) {
                if (!treatmentsynonymListOld.contains(treatmentsynonymListNewTreatmentsynonym)) {
                    Synonym oldSynonymidOfTreatmentsynonymListNewTreatmentsynonym = treatmentsynonymListNewTreatmentsynonym.getSynonymid();
                    treatmentsynonymListNewTreatmentsynonym.setSynonymid(synonym);
                    treatmentsynonymListNewTreatmentsynonym = em.merge(treatmentsynonymListNewTreatmentsynonym);
                    if (oldSynonymidOfTreatmentsynonymListNewTreatmentsynonym != null && !oldSynonymidOfTreatmentsynonymListNewTreatmentsynonym.equals(synonym)) {
                        oldSynonymidOfTreatmentsynonymListNewTreatmentsynonym.getTreatmentsynonymList().remove(treatmentsynonymListNewTreatmentsynonym);
                        oldSynonymidOfTreatmentsynonymListNewTreatmentsynonym = em.merge(oldSynonymidOfTreatmentsynonymListNewTreatmentsynonym);
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
                Integer id = synonym.getId();
                if (findSynonym(id) == null) {
                    throw new NonexistentEntityException("The synonym with id " + id + " no longer exists.");
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
            Synonym synonym;
            try {
                synonym = em.getReference(Synonym.class, id);
                synonym.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The synonym with id " + id + " no longer exists.", enfe);
            }
            List<Treatmentsynonym> treatmentsynonymList = synonym.getTreatmentsynonymList();
            for (Treatmentsynonym treatmentsynonymListTreatmentsynonym : treatmentsynonymList) {
                treatmentsynonymListTreatmentsynonym.setSynonymid(null);
                treatmentsynonymListTreatmentsynonym = em.merge(treatmentsynonymListTreatmentsynonym);
            }
            em.remove(synonym);
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

    public List<Synonym> findSynonymEntities() {
        return findSynonymEntities(true, -1, -1);
    }

    public List<Synonym> findSynonymEntities(int maxResults, int firstResult) {
        return findSynonymEntities(false, maxResults, firstResult);
    }

    private List<Synonym> findSynonymEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Synonym.class));
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

    public Synonym findSynonym(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Synonym.class, id);
        } finally {
            em.close();
        }
    }

    public int getSynonymCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Synonym> rt = cq.from(Synonym.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
