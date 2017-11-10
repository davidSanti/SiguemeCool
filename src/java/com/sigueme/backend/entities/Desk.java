/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "desks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Desk.findAll", query = "SELECT d FROM Desk d")
    , @NamedQuery(name = "Desk.findByDeskId", query = "SELECT d FROM Desk d WHERE d.deskId = :deskId")
    , @NamedQuery(name = "Desk.findBySerialCode", query = "SELECT d FROM Desk d WHERE d.serialCode = :serialCode")
    , @NamedQuery(name = "Desk.findByDeskLocation", query = "SELECT d FROM Desk d WHERE d.deskLocation = :deskLocation")})
public class Desk implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "desk_id")
    private Integer deskId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "serial_code")
    private String serialCode;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "desk_location")
    private String deskLocation;
    
    @JoinTable(name = "elements_by_desk", joinColumns = {
        @JoinColumn(name = "desk_id", referencedColumnName = "desk_id")}, inverseJoinColumns = {
        @JoinColumn(name = "element_id", referencedColumnName = "element_id")})
    @ManyToMany
    private List<Element> elements;
    
    @JoinTable(name = "desks_by_group", joinColumns = {
        @JoinColumn(name = "desk_id", referencedColumnName = "desk_id")}, inverseJoinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "group_id")})
    @ManyToMany
    private List<GroupCls> groupCls;

    public Desk() {
    }

    public Desk(Integer deskId) {
        this.deskId = deskId;
    }

    public Desk(Integer deskId, String serialCode, String deskLocation) {
        this.deskId = deskId;
        this.serialCode = serialCode;
        this.deskLocation = deskLocation;
    }

    public Integer getDeskId() {
        return deskId;
    }

    public void setDeskId(Integer deskId) {
        this.deskId = deskId;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getDeskLocation() {
        return deskLocation;
    }

    public void setDeskLocation(String deskLocation) {
        this.deskLocation = deskLocation;
    }

    @XmlTransient
    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<GroupCls> getGroupCls() {
        return groupCls;
    }

    public void setGroupCls(List<GroupCls> groupCls) {
        this.groupCls = groupCls;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deskId != null ? deskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Desk)) {
            return false;
        }
        Desk other = (Desk) object;
        if ((this.deskId == null && other.deskId != null) || (this.deskId != null && !this.deskId.equals(other.deskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Desk[ deskId=" + deskId + " ]";
    }
    
}
