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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "conventions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Convention.findAll", query = "SELECT c FROM Convention c")
    , @NamedQuery(name = "Convention.findByConventionId", query = "SELECT c FROM Convention c WHERE c.conventionId = :conventionId")
    , @NamedQuery(name = "Convention.findByDescription", query = "SELECT c FROM Convention c WHERE c.description = :description")})
public class Convention implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "convention_id")
    private Integer conventionId;
    @Size(max = 70)
    @Column(name = "description")
    private String description;

    public Convention() {
    }

    public Convention(Integer conventionId) {
        this.conventionId = conventionId;
    }

    public Integer getConventionId() {
        return conventionId;
    }

    public void setConventionId(Integer conventionId) {
        this.conventionId = conventionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conventionId != null ? conventionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convention)) {
            return false;
        }
        Convention other = (Convention) object;
        if ((this.conventionId == null && other.conventionId != null) || (this.conventionId != null && !this.conventionId.equals(other.conventionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Convention[ conventionId=" + conventionId + " ]";
    }
    
}
