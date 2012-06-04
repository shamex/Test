/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.um.feobet.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sirajude
 */
@Entity
@Table(name = "disease", catalog = "feobet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disease.findAll", query = "SELECT d FROM Disease d"),
    @NamedQuery(name = "Disease.findById", query = "SELECT d FROM Disease d WHERE d.id = :id"),
    @NamedQuery(name = "Disease.findByCreatedate", query = "SELECT d FROM Disease d WHERE d.createdate = :createdate"),
    @NamedQuery(name = "Disease.findByUpdatedate", query = "SELECT d FROM Disease d WHERE d.updatedate = :updatedate"),
    @NamedQuery(name = "Disease.findByDeletedate", query = "SELECT d FROM Disease d WHERE d.deletedate = :deletedate"),
    @NamedQuery(name = "Disease.findByDisabled", query = "SELECT d FROM Disease d WHERE d.disabled = :disabled"),
    @NamedQuery(name = "Disease.findByDeleted", query = "SELECT d FROM Disease d WHERE d.deleted = :deleted")})
public class Disease implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "name", length = 65535)
    private String name;
    @Column(name = "createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @Column(name = "updatedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedate;
    @Column(name = "deletedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedate;
    @Column(name = "disabled")
    private Boolean disabled;
    @Column(name = "deleted")
    private Boolean deleted;
    @Lob
    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;
    @OneToMany(mappedBy = "diseaseid")
    private List<Diseasesymptoms> diseasesymptomsList;
    @OneToMany(mappedBy = "diseaseid")
    private List<Diseasecause> diseasecauseList;
    @OneToMany(mappedBy = "diseaseid")
    private List<Diseasetreatment> diseasetreatmentList;

    public Disease() {
    }

    public Disease(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Date getDeletedate() {
        return deletedate;
    }

    public void setDeletedate(Date deletedate) {
        this.deletedate = deletedate;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Diseasesymptoms> getDiseasesymptomsList() {
        return diseasesymptomsList;
    }

    public void setDiseasesymptomsList(List<Diseasesymptoms> diseasesymptomsList) {
        this.diseasesymptomsList = diseasesymptomsList;
    }

    @XmlTransient
    public List<Diseasecause> getDiseasecauseList() {
        return diseasecauseList;
    }

    public void setDiseasecauseList(List<Diseasecause> diseasecauseList) {
        this.diseasecauseList = diseasecauseList;
    }

    @XmlTransient
    public List<Diseasetreatment> getDiseasetreatmentList() {
        return diseasetreatmentList;
    }

    public void setDiseasetreatmentList(List<Diseasetreatment> diseasetreatmentList) {
        this.diseasetreatmentList = diseasetreatmentList;
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
        if (!(object instanceof Disease)) {
            return false;
        }
        Disease other = (Disease) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.um.feobet.persistence.entity.Disease[ id=" + id + " ]";
    }
    
}
