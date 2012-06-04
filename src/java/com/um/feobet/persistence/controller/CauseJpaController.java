/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import com.um.feobet.persistence.controller.exceptions.NonexistentEntityException;
import com.um.feobet.persistence.controller.exceptions.RollbackFailureException;
import com.um.feobet.persistence.entity.Cause;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.um.feobet.persistence.entity.Diseasecause;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.UserTransaction;

/**
 *
 * @author sirajude
 */
public class CauseJpaController implements Serializable {

    public CauseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cause cause) throws RollbackFailureException, Exception {
        if (cause.getDiseasecauseList() == null) {
            cause.setDiseasecauseList(new ArrayList<Diseasecause>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Diseasecause> attachedDiseasecauseList = new ArrayList<Diseasecause>();
            for (Diseasecause diseasecauseListDiseasecauseToAttach : cause.getDiseasecauseList()) {
                diseasecauseListDiseasecauseToAttach = em.getReference(diseasecauseListDiseasecauseToAttach.getClass(), diseasecauseListDiseasecauseToAttach.getId());
                attachedDiseasecauseList.add(diseasecauseListDiseasecauseToAttach);
            }
            cause.setDiseasecauseList(attachedDiseasecauseList);
            em.persist(cause);
            for (Diseasecause diseasecauseListDiseasecause : cause.getDiseasecauseList()) {
                Cause oldCauseidOfDiseasecauseListDiseasecause = diseasecauseListDiseasecause.getCauseid();
                diseasecauseListDiseasecause.setCauseid(cause);
                diseasecauseListDiseasecause = em.merge(diseasecauseListDiseasecause);
                if (oldCauseidOfDiseasecauseListDiseasecause != null) {
                    oldCauseidOfDiseasecauseListDiseasecause.getDiseasecauseList().remove(diseasecauseListDiseasecause);
                    oldCauseidOfDiseasecauseListDiseasecause = em.merge(oldCauseidOfDiseasecauseListDiseasecause);
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

    public void edit(Cause cause) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cause persistentCause = em.find(Cause.class, cause.getId());
            List<Diseasecause> diseasecauseListOld = persistentCause.getDiseasecauseList();
            List<Diseasecause> diseasecauseListNew = cause.getDiseasecauseList();
            List<Diseasecause> attachedDiseasecauseListNew = new ArrayList<Diseasecause>();
            for (Diseasecause diseasecauseListNewDiseasecauseToAttach : diseasecauseListNew) {
                diseasecauseListNewDiseasecauseToAttach = em.getReference(diseasecauseListNewDiseasecauseToAttach.getClass(), diseasecauseListNewDiseasecauseToAttach.getId());
                attachedDiseasecauseListNew.add(diseasecauseListNewDiseasecauseToAttach);
            }
            diseasecauseListNew = attachedDiseasecauseListNew;
            cause.setDiseasecauseList(diseasecauseListNew);
            cause = em.merge(cause);
            for (Diseasecause diseasecauseListOldDiseasecause : diseasecauseListOld) {
                if (!diseasecauseListNew.contains(diseasecauseListOldDiseasecause)) {
                    diseasecauseListOldDiseasecause.setCauseid(null);
                    diseasecauseListOldDiseasecause = em.merge(diseasecauseListOldDiseasecause);
                }
            }
            for (Diseasecause diseasecauseListNewDiseasecause : diseasecauseListNew) {
                if (!diseasecauseListOld.contains(diseasecauseListNewDiseasecause)) {
                    Cause oldCauseidOfDiseasecauseListNewDiseasecause = diseasecauseListNewDiseasecause.getCauseid();
                    diseasecauseListNewDiseasecause.setCauseid(cause);
                    diseasecauseListNewDiseasecause = em.merge(diseasecauseListNewDiseasecause);
                    if (oldCauseidOfDiseasecauseListNewDiseasecause != null && !oldCauseidOfDiseasecauseListNewDiseasecause.equals(cause)) {
                        oldCauseidOfDiseasecauseListNewDiseasecause.getDiseasecauseList().remove(diseasecauseListNewDiseasecause);
                        oldCauseidOfDiseasecauseListNewDiseasecause = em.merge(oldCauseidOfDiseasecauseListNewDiseasecause);
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
                Integer id = cause.getId();
                if (findCause(id) == null) {
                    throw new NonexistentEntityException("The cause with id " + id + " no longer exists.");
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
            Cause cause;
            try {
                cause = em.getReference(Cause.class, id);
                cause.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cause with id " + id + " no longer exists.", enfe);
            }
            List<Diseasecause> diseasecauseList = cause.getDiseasecauseList();
            for (Diseasecause diseasecauseListDiseasecause : diseasecauseList) {
                diseasecauseListDiseasecause.setCauseid(null);
                diseasecauseListDiseasecause = em.merge(diseasecauseListDiseasecause);
            }
            em.remove(cause);
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

    public List<Cause> findCauseEntities() {
        return findCauseEntities(true, -1, -1);
    }

    public List<Cause> findCauseEntities(int maxResults, int firstResult) {
        return findCauseEntities(false, maxResults, firstResult);
    }

    private List<Cause> findCauseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cause.class));
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

    public Cause findCause(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cause.class, id);
        } finally {
            em.close();
        }
    }

    public int getCauseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cause> rt = cq.from(Cause.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
