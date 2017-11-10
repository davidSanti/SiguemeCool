/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

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
 * @author RobayoDa
 */
@Entity
@Table(name = "manage_adherence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ManageAdherence.findAll", query = "SELECT m FROM ManageAdherence m")
    , @NamedQuery(name = "ManageAdherence.findByManageAderenceId", query = "SELECT m FROM ManageAdherence m WHERE m.manageAderenceId = :manageAderenceId")
    , @NamedQuery(name = "ManageAdherence.findByTimeAdherence", query = "SELECT m FROM ManageAdherence m WHERE m.timeAdherence = :timeAdherence")})
public class ManageAdherence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "manage_aderence_id")
    private Integer manageAderenceId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_adherence")
    private int timeAdherence;
    
    @JoinColumn(name = "adherence_id", referencedColumnName = "adherence_id")
    @ManyToOne(optional = false)
    private Adherence adherenceId;
    
    @JoinColumn(name = "guideline_id", referencedColumnName = "guideline_id")
    @ManyToOne(optional = false)
    private Guideline guidelineId;

    public ManageAdherence() {
    }

    public ManageAdherence(Integer manageAderenceId) {
        this.manageAderenceId = manageAderenceId;
    }

    public ManageAdherence(Integer manageAderenceId, int timeAdherence) {
        this.manageAderenceId = manageAderenceId;
        this.timeAdherence = timeAdherence;
    }

    public Integer getManageAderenceId() {
        return manageAderenceId;
    }

    public void setManageAderenceId(Integer manageAderenceId) {
        this.manageAderenceId = manageAderenceId;
    }

    public int getTimeAdherence() {
        return timeAdherence;
    }

    public void setTimeAdherence(int timeAdherence) {
        this.timeAdherence = timeAdherence;
    }

    public Adherence getAdherenceId() {
        return adherenceId;
    }

    public void setAdherenceId(Adherence adherenceId) {
        this.adherenceId = adherenceId;
    }

    public Guideline getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(Guideline guidelineId) {
        this.guidelineId = guidelineId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (manageAderenceId != null ? manageAderenceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManageAdherence)) {
            return false;
        }
        ManageAdherence other = (ManageAdherence) object;
        if ((this.manageAderenceId == null && other.manageAderenceId != null) || (this.manageAderenceId != null && !this.manageAderenceId.equals(other.manageAderenceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.ManageAdherence[ manageAderenceId=" + manageAderenceId + " ]";
    }
    
}
