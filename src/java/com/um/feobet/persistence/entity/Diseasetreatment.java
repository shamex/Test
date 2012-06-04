/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sirajude
 */
@Entity
@Table(name = "diseasetreatment", catalog = "feobet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Diseasetreatment.findAll", query = "SELECT d FROM Diseasetreatment d"),
    @NamedQuery(name = "Diseasetreatment.findById", query = "SELECT d FROM Diseasetreatment d WHERE d.id = :id")})
public class Diseasetreatment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "treatmentid", referencedColumnName = "id")
    @ManyToOne
    private Treatment treatmentid;
    @JoinColumn(name = "diseaseid", referencedColumnName = "id")
    @ManyToOne
    private Disease diseaseid;

    public Diseasetreatment() {
    }

    public Diseasetreatment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Treatment getTreatmentid() {
        return treatmentid;
    }

    public void setTreatmentid(Treatment treatmentid) {
        this.treatmentid = treatmentid;
    }

    public Disease getDiseaseid() {
        return diseaseid;
    }

    public void setDiseaseid(Disease diseaseid) {
        this.diseaseid = diseaseid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Diseasetreatment)) {
            return false;
        }
        Diseasetreatment other = (Diseasetreatment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.um.feobet.persistence.entity.Diseasetreatment[ id=" + id + " ]";
    }
    
}
