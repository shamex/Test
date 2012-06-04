/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.action;

import com.um.feobet.common.ui.util.Gender;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author sirajude
 */
@ManagedBean(value = "userInfo")
public class UserInfo {

    private List<Gender> genders;
    private Gender gender;
    private String age;
    private String message;
    private boolean errorFlag;
    private String genderValue;

    /** Creates a new instance of UserInfo */
    public UserInfo() {
        loadGenders();
    }

    private void loadGenders() {
        setGenders(new ArrayList<Gender>());

        setGender(new Gender("Male", "male"));
        getGenders().add(getGender());

        setGender(new Gender("Female", "female"));
        getGenders().add(getGender());
    }

    public String next() {

        System.out.println("Gender : " + genderValue);

        return null;
    }

    /**
     * @return the genders
     */
    public List<Gender> getGenders() {
        return genders;
    }

    /**
     * @param genders the genders to set
     */
    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
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

    public void validateAge(FacesContext context, UIComponent component, Object object) {
        System.out.println("Component : " + component);
        System.out.println("Object : " + object);

        String pattern = "d{1,2}";

        System.out.println("!String.valueOf(object).matches(pattern) : " + (!String.valueOf(object).matches(pattern)));
        
        if (!String.valueOf(object).matches(pattern)) {
            System.out.println("Invalid Age");
            message = "Invalid Age";
            context.addMessage("txtAge", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Age", null));
            errorFlag = Boolean.TRUE;
        }
    }

    public void convertGender(FacesContext context, UIComponent component, Object object) {
        System.out.println("Component : " + component);
        System.out.println("Object : " + object);

        String pattern = "d{1,3}";

        if (!String.valueOf(object).matches(pattern)) {
        }
    }

    /**
     * @return the genderValue
     */
    public String getGenderValue() {
        return genderValue;
    }

    /**
     * @param genderValue the genderValue to set
     */
    public void setGenderValue(String genderValue) {
        this.genderValue = genderValue;
    }

    public boolean isErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }
}