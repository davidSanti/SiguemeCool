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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "taxi_rate_by_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxiRateByUser.findAll", query = "SELECT t FROM TaxiRateByUser t")
    , @NamedQuery(name = "TaxiRateByUser.findByTaxiRateByUserId", query = "SELECT t FROM TaxiRateByUser t WHERE t.taxiRateByUserId = :taxiRateByUserId")
    , @NamedQuery(name = "TaxiRateByUser.findByRequestRate", query = "SELECT t FROM TaxiRateByUser t WHERE t.requestRate = :requestRate")
    , @NamedQuery(name = "TaxiRateByUser.findByStablisedRate", query = "SELECT t FROM TaxiRateByUser t WHERE t.stablisedRate = :stablisedRate")
    , @NamedQuery(name = "TaxiRateByUser.findByStatus", query = "SELECT t FROM TaxiRateByUser t WHERE t.status = :status")})
public class TaxiRateByUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "taxi_rate_by_user_id")
    private Integer taxiRateByUserId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "request_rate")
    private double requestRate;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stablised_rate")
    private Double stablisedRate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;

    public TaxiRateByUser() {
    }

    public TaxiRateByUser(Integer taxiRateByUserId) {
        this.taxiRateByUserId = taxiRateByUserId;
    }

    public TaxiRateByUser(Integer taxiRateByUserId, double requestRate, boolean status) {
        this.taxiRateByUserId = taxiRateByUserId;
        this.requestRate = requestRate;
        this.status = status;
    }

    public Integer getTaxiRateByUserId() {
        return taxiRateByUserId;
    }

    public void setTaxiRateByUserId(Integer taxiRateByUserId) {
        this.taxiRateByUserId = taxiRateByUserId;
    }

    public double getRequestRate() {
        return requestRate;
    }

    public void setRequestRate(double requestRate) {
        this.requestRate = requestRate;
    }

    public Double getStablisedRate() {
        return stablisedRate;
    }

    public void setStablisedRate(Double stablisedRate) {
        this.stablisedRate = stablisedRate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        hash += (taxiRateByUserId != null ? taxiRateByUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxiRateByUser)) {
            return false;
        }
        TaxiRateByUser other = (TaxiRateByUser) object;
        if ((this.taxiRateByUserId == null && other.taxiRateByUserId != null) || (this.taxiRateByUserId != null && !this.taxiRateByUserId.equals(other.taxiRateByUserId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.TaxiRateByUser[ taxiRateByUserId=" + taxiRateByUserId + " ]";
    }
    
}
