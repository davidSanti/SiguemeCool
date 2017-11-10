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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupCls.findAll", query = "SELECT g FROM GroupCls g")
    , @NamedQuery(name = "GroupCls.findByGroupId", query = "SELECT g FROM GroupCls g WHERE g.groupId = :groupId")
    , @NamedQuery(name = "GroupCls.findByGroupName", query = "SELECT g FROM GroupCls g WHERE g.groupName = :groupName")})
public class GroupCls implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "group_id")
    private Integer groupId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "group_name")
    private String groupName;

    @Basic(optional = false)
    @NotNull
    @Column(name = "average_aht")
    private int averageAht;

    public GroupCls() {
    }

    public GroupCls(Integer groupId) {
        this.groupId = groupId;
    }

    public GroupCls(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getAverageAht() {
        return averageAht;
    }

    public void setAverageAht(int averageAht) {
        this.averageAht = averageAht;
    } 
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupCls)) {
            return false;
        }
        GroupCls other = (GroupCls) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.GroupCls[ groupId=" + groupId + " ]";
    }

}
