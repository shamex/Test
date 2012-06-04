/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.action;

import javax.annotation.ManagedBean;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.panel.Panel;

/**
 *
 * @author sirajude
 */
@ManagedBean(value = "main")
public class Main {

    private String name;
    private String label;
    private String message;
    private Panel panel;

    /** Creates a new instance of Main */
    public Main() {
        name = null;
        label = null;
        message = null;
        loadPanel();
    }

    private void loadPanel() {
        panel = new Panel();
        HtmlForm form = new HtmlForm();
        HtmlOutputLabel outputLabel = new HtmlOutputLabel();
        final InputText text = new InputText();
        outputLabel.setValue("What is your name ?");
        text.setValue("");
        text.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                 System.out.println("Show name : " + showName());
            }
        });        
        
        form.getChildren().add(outputLabel);
        form.getChildren().add(text);
        panel.getChildren().add(form);
    }

    public String showName() {
        System.out.println("Name is Empty ? : " + (name.equals("") || name == null));

        if (!name.equals("") || name != null || name.length() != 0) {
            label = "My name is " + name;
        }

        return null;
    }

    public String reset() {
        name = "";

        return null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the panel
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}
