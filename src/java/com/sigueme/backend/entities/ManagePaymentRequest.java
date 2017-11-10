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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "manage_payment_request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ManagePaymentRequest.findAll", query = "SELECT m FROM ManagePaymentRequest m")
    , @NamedQuery(name = "ManagePaymentRequest.findByManagePaymentId", query = "SELECT m FROM ManagePaymentRequest m WHERE m.managePaymentId = :managePaymentId")
    , @NamedQuery(name = "ManagePaymentRequest.findByPaymentDate", query = "SELECT m FROM ManagePaymentRequest m WHERE m.paymentDate = :paymentDate")
    , @NamedQuery(name = "ManagePaymentRequest.findByGuideline", query = "SELECT m FROM ManagePaymentRequest m WHERE m.guideline = :guideline")
    , @NamedQuery(name = "ManagePaymentRequest.findByPaymentHour", query = "SELECT m FROM ManagePaymentRequest m WHERE m.paymentHour = :paymentHour")
    , @NamedQuery(name = "ManagePaymentRequest.findByPaymentStatus", query = "SELECT m FROM ManagePaymentRequest m WHERE m.paymentStatus = :paymentStatus")
    , @NamedQuery(name = "ManagePaymentRequest.findByDescription", query = "SELECT m FROM ManagePaymentRequest m WHERE m.description = :description")})
public class ManagePaymentRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "manage_payment_id")
    private Integer managePaymentId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "guideline")
    private boolean guideline;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_hour")
    @Temporal(TemporalType.TIME)
    private Date paymentHour;
    
    @Column(name = "payment_status")
    private Boolean paymentStatus;
    
    @Size(max = 300)
    @Column(name = "description")
    private String description;
    
    @JoinColumn(name = "rate_by_user_id", referencedColumnName = "taxi_rate_by_user_id")
    @ManyToOne(optional = false)
    private TaxiRateByUser rateByUserId;

    public ManagePaymentRequest() {
    }

    public ManagePaymentRequest(Integer managePaymentId) {
        this.managePaymentId = managePaymentId;
    }

    public ManagePaymentRequest(Integer managePaymentId, Date paymentDate, boolean guideline, Date paymentHour) {
        this.managePaymentId = managePaymentId;
        this.paymentDate = paymentDate;
        this.guideline = guideline;
        this.paymentHour = paymentHour;
    }

    public Integer getManagePaymentId() {
        return managePaymentId;
    }

    public void setManagePaymentId(Integer managePaymentId) {
        this.managePaymentId = managePaymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean getGuideline() {
        return guideline;
    }

    public void setGuideline(boolean guideline) {
        this.guideline = guideline;
    }

    public Date getPaymentHour() {
        return paymentHour;
    }

    public void setPaymentHour(Date paymentHour) {
        this.paymentHour = paymentHour;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaxiRateByUser getRateByUserId() {
        return rateByUserId;
    }

    public void setRateByUserId(TaxiRateByUser rateByUserId) {
        this.rateByUserId = rateByUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (managePaymentId != null ? managePaymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManagePaymentRequest)) {
            return false;
        }
        ManagePaymentRequest other = (ManagePaymentRequest) object;
        if ((this.managePaymentId == null && other.managePaymentId != null) || (this.managePaymentId != null && !this.managePaymentId.equals(other.managePaymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.ManagePaymentRequest[ managePaymentId=" + managePaymentId + " ]";
    }
    
}
