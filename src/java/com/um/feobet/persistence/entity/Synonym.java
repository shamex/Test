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
@Table(name = "synonym", catalog = "feobet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Synonym.findAll", query = "SELECT s FROM Synonym s"),
    @NamedQuery(name = "Synonym.findById", query = "SELECT s FROM Synonym s WHERE s.id = :id"),
    @NamedQuery(name = "Synonym.findByCreatedate", query = "SELECT s FROM Synonym s WHERE s.createdate = :createdate"),
    @NamedQuery(name = "Synonym.findByUpdatedate", query = "SELECT s FROM Synonym s WHERE s.updatedate = :updatedate"),
    @NamedQuery(name = "Synonym.findByDeletedate", query = "SELECT s FROM Synonym s WHERE s.deletedate = :deletedate"),
    @NamedQuery(name = "Synonym.findByDisabled", query = "SELECT s FROM Synonym s WHERE s.disabled = :disabled"),
    @NamedQuery(name = "Synonym.findByDeleted", query = "SELECT s FROM Synonym s WHERE s.deleted = :deleted")})
public class Synonym implements Serializable {
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
    @OneToMany(mappedBy = "synonymid")
    private List<Treatmentsynonym> treatmentsynonymList;

    public Synonym() {
    }

    public Synonym(Integer id) {
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
    public List<Treatmentsynonym> getTreatmentsynonymList() {
        return treatmentsynonymList;
    }

    public void setTreatmentsynonymList(List<Treatmentsynonym> treatmentsynonymList) {
        this.treatmentsynonymList = treatmentsynonymList;
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
        if (!(object instanceof Synonym)) {
            return false;
        }
        Synonym other = (Synonym) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.um.feobet.persistence.entity.Synonym[ id=" + id + " ]";
    }
    
}
