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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "permissions_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermissionRole.findAll", query = "SELECT p FROM PermissionRole p")
    , @NamedQuery(name = "PermissionRole.findByPermissionRoleId", query = "SELECT p FROM PermissionRole p WHERE p.permissionRoleId = :permissionRoleId")
    , @NamedQuery(name = "PermissionRole.findByOpCreate", query = "SELECT p FROM PermissionRole p WHERE p.opCreate = :opCreate")
    , @NamedQuery(name = "PermissionRole.findByOpEdit", query = "SELECT p FROM PermissionRole p WHERE p.opEdit = :opEdit")
    , @NamedQuery(name = "PermissionRole.findByOpDelete", query = "SELECT p FROM PermissionRole p WHERE p.opDelete = :opDelete")
    , @NamedQuery(name = "PermissionRole.findByOpOther", query = "SELECT p FROM PermissionRole p WHERE p.opOther = :opOther")})
public class PermissionRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "permission_role_id")
    private Integer permissionRoleId;
    
    @Column(name = "op_create")
    private Boolean opCreate;
    
    @Column(name = "op_edit")
    private Boolean opEdit;
    
    @Column(name = "op_delete")
    private Boolean opDelete;
    
    @Column(name = "op_other")
    private Boolean opOther;
    
    @JoinColumn(name = "permission_id", referencedColumnName = "permission_id")
    @ManyToOne(optional = false)
    private Permission permissionId;
    
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role roleId;

    public PermissionRole() {
    }

    public PermissionRole(Integer permissionRoleId) {
        this.permissionRoleId = permissionRoleId;
    }

    public Integer getPermissionRoleId() {
        return permissionRoleId;
    }

    public void setPermissionRoleId(Integer permissionRoleId) {
        this.permissionRoleId = permissionRoleId;
    }

    public Boolean getOpCreate() {
        return opCreate;
    }

    public void setOpCreate(Boolean opCreate) {
        this.opCreate = opCreate;
    }

    public Boolean getOpEdit() {
        return opEdit;
    }

    public void setOpEdit(Boolean opEdit) {
        this.opEdit = opEdit;
    }

    public Boolean getOpDelete() {
        return opDelete;
    }

    public void setOpDelete(Boolean opDelete) {
        this.opDelete = opDelete;
    }

    public Boolean getOpOther() {
        return opOther;
    }

    public void setOpOther(Boolean opOther) {
        this.opOther = opOther;
    }

    public Permission getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Permission permissionId) {
        this.permissionId = permissionId;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permissionRoleId != null ? permissionRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermissionRole)) {
            return false;
        }
        PermissionRole other = (PermissionRole) object;
        if ((this.permissionRoleId == null && other.permissionRoleId != null) || (this.permissionRoleId != null && !this.permissionRoleId.equals(other.permissionRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.PermissionRole[ permissionRoleId=" + permissionRoleId + " ]";
    }
    
}
