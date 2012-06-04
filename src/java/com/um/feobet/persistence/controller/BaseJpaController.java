/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.controller;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author sirajude
 */
public class BaseJpaController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("FEOBETWebPU");

        return emf.createEntityManager();
    }
}
