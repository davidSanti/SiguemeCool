/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author RobayoDa
 */
@Stateless
public class UserByCourseFacade extends AbstractFacade<UserByCourse> implements UserByCourseFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserByCourseFacade() {
        super(UserByCourse.class);
    }

    @Override
    public List<UserByCourse> listarUsuariosPorCurso(Course curso) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT uc FROM UserByCourse uc WHERE uc.courseId = :curso ");
            query.setParameter("curso", curso);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo listarUsuariosPorCurso= " + ex.getMessage());
        }
        return lista;
    }
}
