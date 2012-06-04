/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.action;

import com.um.feobet.persistence.controller.SymptomsJpaController;
import com.um.feobet.persistence.entity.Symptoms;
import java.util.List;

/**
 *
 * @author sirajude
 */
public class TestBean {

    /** Creates a new instance of TestBean */
    public TestBean() {
    }

    public List<Symptoms> getSymptoms() {
        SymptomsJpaController symptomsJpaController = new SymptomsJpaController();
        return symptomsJpaController.findSymptomsEntities();
    }
}
