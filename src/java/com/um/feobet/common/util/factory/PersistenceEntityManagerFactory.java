/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.common.util.factory;

import javax.persistence.EntityManager;

/**
 *
 * @author sirajude
 */
abstract public interface PersistenceEntityManagerFactory {
    abstract public EntityManager createPersistenceEntityManagerfactory();
    public static final String PERSISTENCE_UNIT = "";
}
