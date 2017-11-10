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
@Table(name = "aht")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aht.findAll", query = "SELECT a FROM Aht a")
    , @NamedQuery(name = "Aht.findByAhtId", query = "SELECT a FROM Aht a WHERE a.ahtId = :ahtId")
    , @NamedQuery(name = "Aht.findByAhtDate", query = "SELECT a FROM Aht a WHERE a.ahtDate = :ahtDate")
    , @NamedQuery(name = "Aht.findByContactsHandled", query = "SELECT a FROM Aht a WHERE a.contactsHandled = :contactsHandled")
    , @NamedQuery(name = "Aht.findByAht", query = "SELECT a FROM Aht a WHERE a.aht = :aht")})
public class Aht implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aht_id")
    private Integer ahtId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "aht_date")
    @Temporal(TemporalType.DATE)
    private Date ahtDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "contacts_handled")
    private int contactsHandled;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "aht")
    private int aht;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;

    public Aht() {
    }

    public Aht(Integer ahtId) {
        this.ahtId = ahtId;
    }

    public Aht(Integer ahtId, Date ahtDate, int contactsHandled, int aht) {
        this.ahtId = ahtId;
        this.ahtDate = ahtDate;
        this.contactsHandled = contactsHandled;
        this.aht = aht;
    }

    public Integer getAhtId() {
        return ahtId;
    }

    public void setAhtId(Integer ahtId) {
        this.ahtId = ahtId;
    }

    public Date getAhtDate() {
        return ahtDate;
    }

    public void setAhtDate(Date ahtDate) {
        this.ahtDate = ahtDate;
    }

    public int getContactsHandled() {
        return contactsHandled;
    }

    public void setContactsHandled(int contactsHandled) {
        this.contactsHandled = contactsHandled;
    }

    public int getAht() {
        return aht;
    }

    public void setAht(int aht) {
        this.aht = aht;
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
        hash += (ahtId != null ? ahtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aht)) {
            return false;
        }
        Aht other = (Aht) object;
        if ((this.ahtId == null && other.ahtId != null) || (this.ahtId != null && !this.ahtId.equals(other.ahtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Aht[ ahtId=" + ahtId + " ]";
    }
    
}
