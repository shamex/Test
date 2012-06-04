/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.common.util.factory;

/**
 *
 * @author sirajude
 */
abstract public interface AbstractPersistenceFactory {
    abstract public PersistenceEntityManagerFactory createPersistenceManagerFactory();
}
