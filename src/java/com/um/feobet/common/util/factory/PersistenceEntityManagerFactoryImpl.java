/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.common.util.factory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author sirajude
 */
public class PersistenceEntityManagerFactoryImpl implements PersistenceEntityManagerFactory {

    private EntityManagerFactory emf = null;

    @Override
    public EntityManager createPersistenceEntityManagerfactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }

        return emf.createEntityManager();
    }
}
