/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "guidelines")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Guideline.findAll", query = "SELECT g FROM Guideline g")
    , @NamedQuery(name = "Guideline.findByGuidelineId", query = "SELECT g FROM Guideline g WHERE g.guidelineId = :guidelineId")
    , @NamedQuery(name = "Guideline.findByDescription", query = "SELECT g FROM Guideline g WHERE g.description = :description")
    , @NamedQuery(name = "Guideline.findByAbbrevation", query = "SELECT g FROM Guideline g WHERE g.abbrevation = :abbrevation")})
public class Guideline implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "guideline_id")
    private Integer guidelineId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "abbrevation")
    private String abbrevation;
    
    public Guideline() {
    }

    public Guideline(Integer guidelineId) {
        this.guidelineId = guidelineId;
    }

    public Guideline(Integer guidelineId, String description, String abbrevation) {
        this.guidelineId = guidelineId;
        this.description = description;
        this.abbrevation = abbrevation;
    }

    public Integer getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(Integer guidelineId) {
        this.guidelineId = guidelineId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbbrevation() {
        return abbrevation;
    }

    public void setAbbrevation(String abbrevation) {
        this.abbrevation = abbrevation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guidelineId != null ? guidelineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Guideline)) {
            return false;
        }
        Guideline other = (Guideline) object;
        if ((this.guidelineId == null && other.guidelineId != null) || (this.guidelineId != null && !this.guidelineId.equals(other.guidelineId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Guideline[ guidelineId=" + guidelineId + " ]";
    }
    
}
