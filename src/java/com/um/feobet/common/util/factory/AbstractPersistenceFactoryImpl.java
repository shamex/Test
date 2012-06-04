/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.common.util.factory;

/**
 *
 * @author sirajude
 */
public class AbstractPersistenceFactoryImpl implements AbstractPersistenceFactory {

    @Override
    public PersistenceEntityManagerFactory createPersistenceManagerFactory() {
        return new PersistenceEntityManagerFactoryImpl();
    }
}
