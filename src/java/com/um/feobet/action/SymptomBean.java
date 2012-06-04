/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.action;

import com.um.feobet.persistence.controller.SymptomsJpaController;
import com.um.feobet.persistence.entity.Symptoms;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sirajude
 */
public class SymptomBean {
    private SymptomsJpaController symptomsJpaController;
    private List<Symptoms> symptomsList;
    
    /** Creates a new instance of SymptomBean */
    public SymptomBean() {
        symptomsJpaController = new SymptomsJpaController();
        symptomsList.addAll(symptomsJpaController.findSymptomsEntities());
    }
    
//    public List<Symptoms> symptomsList(){
//        List<Symptoms> list = new ArrayList<Symptoms>();
//        
//        list.addAll(symptomsJpaController.findSymptomsEntities());
//        
//        return list;
//    }

    public List<Symptoms> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(List<Symptoms> symptomsList) {
        this.symptomsList = symptomsList;
    }
}
