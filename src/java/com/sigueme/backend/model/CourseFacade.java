/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.GroupCls;
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
public class CourseFacade extends AbstractFacade<Course> implements CourseFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseFacade() {
        super(Course.class);
    }

    @Override
    public List<Course> listarCursos() {
        List<Course> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT cour FROM Course cour ORDER BY cour.courseId DESC");
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo listar Cursos = " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Course> listarCursosPorEstado(boolean estado) {
        List<Course> lista = new ArrayList<>();

        try {
            Query query = em.createNamedQuery("Course.findByCouseStatus", Course.class);
            query.setParameter("couseStatus", estado);
            lista = query.getResultList();

        } catch (Exception e) {
            System.out.println("Error en el metodo listarCursosPorEstado = " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<UserByCourse> filtrarUsuariosPorGrupo(List<GroupCls> listaGrupos, Course curso) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT us FROM UserByCourse us JOIN us.userId u WHERE u.groupId IN :grupos AND us.courseId = :curso", UserByCourse.class);
            query.setParameter("grupos", listaGrupos);
            query.setParameter("curso", curso);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorGrupo= " + ex.getMessage());
        }
        return lista;
    }
    
}
