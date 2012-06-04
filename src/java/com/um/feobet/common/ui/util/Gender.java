/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.common.ui.util;

import javax.faces.model.SelectItem;

/**
 *
 * @author sirajude
 */
public class Gender extends SelectItem {

    public Gender(String label, Object value) {
        super(value, label);
    }

    @Override
    public String getLabel() {
        return super.getLabel();
    }

    @Override
    public Object getValue() {
        return super.getValue();
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled();
    }

    @Override
    public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
    }
}
