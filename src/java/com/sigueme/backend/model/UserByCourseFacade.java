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

    @Override
    public List<UserByCourse> listarMisCursos(User user) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT uc FROM UserByCourse uc WHERE uc.userId = :user  ORDER BY uc.userByCourseId DESC");
            query.setParameter("user", user);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo listarMisCursos= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<UserByCourse> filtrarMisCursosPorCValificacion(User user, boolean grade, String option) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query = null;

            switch (option) {
                case "calificado":
                    query = em.createQuery("SELECT uc FROM UserByCourse uc WHERE uc.userId = :user AND uc.grade = :grade ORDER BY uc.userByCourseId DESC");
                    query.setParameter("grade", grade);
                    break;
                case "pendiente":
                    query = em.createQuery("SELECT uc FROM UserByCourse uc WHERE uc.userId = :user AND uc.grade IS NULL AND uc.attached IS NOT NULL ORDER BY uc.userByCourseId DESC");
                    break;
                case "sin_Evidencia":
                    //En este método no se necesita la calificacion "grade" pero utilizamos el tipo booleano que se pasa para
                    // filtrar por el estado del curso para determinar si el curso está vencido y sin evidencia o solo sin evidencia
                    query = em.createQuery("SELECT uc FROM UserByCourse uc WHERE uc.userId = :user AND uc.grade IS NULL AND uc.attached IS NULL AND uc.courseId.couseStatus = :status ORDER BY uc.userByCourseId DESC");
                    query.setParameter("status", grade);
                    break;
                default:
                    break;
            }
            query.setParameter("user", user);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarMisCursosPorCValificacion= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> listarUsuariosSinEvidencia(Course curso) {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM UserByCourse uc JOIN uc.userId u WHERE uc.courseId = :curso AND uc.attached IS NULL AND uc.description IS NULL ORDER BY u.groupId ASC");
            query.setParameter("curso", curso);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo listarUsuariosSinEvidencia= " + e.getMessage());
        }
        return lista;
    }
}
