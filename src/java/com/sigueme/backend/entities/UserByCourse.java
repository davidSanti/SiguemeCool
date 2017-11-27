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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RobayoDa
 */
@Entity
@Table(name = "users_by_course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserByCourse.findAll", query = "SELECT u FROM UserByCourse u")
    , @NamedQuery(name = "UserByCourse.findByUserByCourseId", query = "SELECT u FROM UserByCourse u WHERE u.userByCourseId = :userByCourseId")
    , @NamedQuery(name = "UserByCourse.findByAttached", query = "SELECT u FROM UserByCourse u WHERE u.attached = :attached")
    , @NamedQuery(name = "UserByCourse.findByDescription", query = "SELECT u FROM UserByCourse u WHERE u.description = :description")
    , @NamedQuery(name = "UserByCourse.findByGrade", query = "SELECT u FROM UserByCourse u WHERE u.grade = :grade")})
public class UserByCourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_by_course_id")
    private Integer userByCourseId;
    
    @Size(max = 100)
    @Column(name = "attached")
    private String attached;
    
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    
    @Column(name = "grade")
    private Boolean grade;
    
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @ManyToOne(optional = false)
    private Course courseId;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;

    public UserByCourse() {
    }

    public UserByCourse(Integer userByCourseId) {
        this.userByCourseId = userByCourseId;
    }

    public Integer getUserByCourseId() {
        return userByCourseId;
    }

    public void setUserByCourseId(Integer userByCourseId) {
        this.userByCourseId = userByCourseId;
    }

    public String getAttached() {
        return attached;
    }

    public void setAttached(String attached) {
        this.attached = attached;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getGrade() {
        return grade;
    }

    public void setGrade(Boolean grade) {
        this.grade = grade;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
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
        hash += (userByCourseId != null ? userByCourseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserByCourse)) {
            return false;
        }
        UserByCourse other = (UserByCourse) object;
        if ((this.userByCourseId == null && other.userByCourseId != null) || (this.userByCourseId != null && !this.userByCourseId.equals(other.userByCourseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.UserByCourse[ userByCourseId=" + userByCourseId + " ]";
    }
    
}
