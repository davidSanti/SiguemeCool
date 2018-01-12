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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "elements")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Element.findAll", query = "SELECT e FROM Element e")
    , @NamedQuery(name = "Element.findByElementId", query = "SELECT e FROM Element e WHERE e.elementId = :elementId")
    , @NamedQuery(name = "Element.findBySerialCode", query = "SELECT e FROM Element e WHERE e.serialCode = :serialCode")
    , @NamedQuery(name = "Element.findByInventoryPlaque", query = "SELECT e FROM Element e WHERE e.inventoryPlaque = :inventoryPlaque")
    , @NamedQuery(name = "Element.findByBrand", query = "SELECT e FROM Element e WHERE e.brand = :brand")
    , @NamedQuery(name = "Element.findByModel", query = "SELECT e FROM Element e WHERE e.model = :model")
    , @NamedQuery(name = "Element.findByPcName", query = "SELECT e FROM Element e WHERE e.pcName = :pcName")
    , @NamedQuery(name = "Element.findByDescription", query = "SELECT e FROM Element e WHERE e.description = :description")})
public class Element implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "element_id")
    private Integer elementId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "serial_code")
    private String serialCode;

    @Size(max = 20)
    @Column(name = "inventory_plaque")
    private String inventoryPlaque;

    @Size(max = 50)
    @Column(name = "brand")
    private String brand;

    @Size(max = 50)
    @Column(name = "model")
    private String model;

    @Size(max = 17)
    @Column(name = "pc_name")
    private String pcName;

    @Size(max = 50)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "desk_id", referencedColumnName = "desk_id")
    @ManyToOne(optional = false)
    private Desk deskId;

    @JoinColumn(name = "type_id", referencedColumnName = "element_type_id")
    private ElementType typeId;

    public Element() {
    }

    public Element(Integer elementId) {
        this.elementId = elementId;
    }

    public Element(Integer elementId, String serialCode) {
        this.elementId = elementId;
        this.serialCode = serialCode;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getInventoryPlaque() {
        return inventoryPlaque;
    }

    public void setInventoryPlaque(String inventoryPlaque) {
        this.inventoryPlaque = inventoryPlaque;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ElementType getTypeId() {
        return typeId;
    }

    public void setTypeId(ElementType typeId) {
        this.typeId = typeId;
    }

    public Desk getDeskId() {
        return deskId;
    }

    public void setDeskId(Desk deskId) {
        this.deskId = deskId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (elementId != null ? elementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Element)) {
            return false;
        }
        Element other = (Element) object;
        if ((this.elementId == null && other.elementId != null) || (this.elementId != null && !this.elementId.equals(other.elementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Element[ elementId=" + elementId + " ]";
    }

}
