/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "adherence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adherence.findAll", query = "SELECT a FROM Adherence a")
    , @NamedQuery(name = "Adherence.findByAdherenceId", query = "SELECT a FROM Adherence a WHERE a.adherenceId = :adherenceId")
    , @NamedQuery(name = "Adherence.findByAdherenceType", query = "SELECT a FROM Adherence a WHERE a.adherenceType = :adherenceType")
    , @NamedQuery(name = "Adherence.findByAdherenceDate", query = "SELECT a FROM Adherence a WHERE a.adherenceDate = :adherenceDate")
    , @NamedQuery(name = "Adherence.findByStartHour", query = "SELECT a FROM Adherence a WHERE a.startHour = :startHour")
    , @NamedQuery(name = "Adherence.findByFinishHour", query = "SELECT a FROM Adherence a WHERE a.finishHour = :finishHour")})
public class Adherence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "adherence_id")
    private Integer adherenceId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "adherence_type")
    private boolean adherenceType;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "adherence_date")
    @Temporal(TemporalType.DATE)
    private Date adherenceDate;
    
    @Column(name = "start_hour")
    @Temporal(TemporalType.TIME)
    private Date startHour;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "finish_hour")
    @Temporal(TemporalType.TIME)
    private Date finishHour;
    
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id")
    @ManyToOne(optional = false)
    private Activity activityId;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;

    public Adherence() {
    }

    public Adherence(Integer adherenceId) {
        this.adherenceId = adherenceId;
    }

    public Adherence(Integer adherenceId, boolean adherenceType, Date adherenceDate, Date startHour, Date finishHour) {
        this.adherenceId = adherenceId;
        this.adherenceType = adherenceType;
        this.adherenceDate = adherenceDate;
        this.startHour = startHour;
        this.finishHour = finishHour;
    }

    public Integer getAdherenceId() {
        return adherenceId;
    }

    public void setAdherenceId(Integer adherenceId) {
        this.adherenceId = adherenceId;
    }

    public boolean getAdherenceType() {
        return adherenceType;
    }

    public void setAdherenceType(boolean adherenceType) {
        this.adherenceType = adherenceType;
    }

    public Date getAdherenceDate() {
        return adherenceDate;
    }

    public void setAdherenceDate(Date adherenceDate) {
        this.adherenceDate = adherenceDate;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public Date getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(Date finishHour) {
        this.finishHour = finishHour;
    }

    public Activity getActivityId() {
        return activityId;
    }

    public void setActivityId(Activity activityId) {
        this.activityId = activityId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

     @Override
    public int hashCode() {
        int hash = 0;
        hash += (adherenceId != null ? adherenceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adherence)) {
            return false;
        }
        Adherence other = (Adherence) object;
        if ((this.adherenceId == null && other.adherenceId != null) || (this.adherenceId != null && !this.adherenceId.equals(other.adherenceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Adherence[ adherenceId=" + adherenceId + " ]";
    }
    
}
