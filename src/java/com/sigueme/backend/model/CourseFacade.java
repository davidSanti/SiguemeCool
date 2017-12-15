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
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

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
    public List<UserByCourse> filtrarUsuariosPorGrupo(List<GroupCls> listaGrupos, Course curso, List<User> listaParaFiltrar) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT DISTINCT us FROM UserByCourse us JOIN us.userId u WHERE u.groupId IN :grupos AND us.courseId = :curso AND u IN :usuarios AND u.userStatusId.userStatusId <> :estadoUsuario", UserByCourse.class);
            query.setParameter("grupos", listaGrupos);
            query.setParameter("curso", curso);
            query.setParameter("usuarios", listaParaFiltrar);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorGrupo= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public boolean validarEvidenciaUsuariosCurso(Course curso) {
        List<UserByCourse> lista = new ArrayList<>();
        boolean bandera = false;
        try {
            Query query = em.createQuery("SELECT uc FROM UserByCourse uc JOIN uc.userId u WHERE uc.courseId = :curso and uc.attached IS NOT NULL AND u.userStatusId.userStatusId <> :estadoUsuario", UserByCourse.class);
            query.setParameter("curso", curso);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();

            bandera = lista.isEmpty();

        } catch (Exception e) {
            System.out.println("Error en el metodo ValidarEvidenciaUsuariosCurso = " + e.getMessage());
        }
        return bandera;
    }

    @Override
    public void vencerCursos() {
        try {
            Query query = em.createQuery("UPDATE Course c SET c.couseStatus = FALSE WHERE c.finishDate <= CURRENT_DATE");
            query.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en el metodo ValidarEvidenciaUsuariosCurso = " + e.getMessage());
        }
    }

    @Override
    public List<UserByCourse> filtrarMisCursosPorNombre(String nombre, User usuario) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT uc FROM UserByCourse uc JOIN uc.courseId c WHERE c.courseName LIKE CONCAT('%',:nombre,'%') AND uc.userId = :usuario ORDER BY uc.userByCourseId DESC");
            query.setParameter("nombre", nombre);
            query.setParameter("usuario", usuario);
            lista = query.getResultList();

        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarPorNombre = " + ex.getMessage());
        }
        return lista;

    }

    @Override
    public List<UserByCourse> filtrarMisCursosPorFechas(Date fechaInicio, Date fechaFin, User usuario) {
        List<UserByCourse> lista = new ArrayList<>();
        try {
            Query query;
            if (fechaInicio != null) {
                if (fechaFin != null) {
                    query = em.createQuery("SELECT uc FROM UserByCourse uc JOIN uc.courseId c WHERE c.startDate >= :fechaInicio AND c.finishDate <= :fechaFin AND uc.userId = :usuario ORDER BY uc.userByCourseId DESC");
                    query.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
                    query.setParameter("fechaFin", fechaFin, TemporalType.DATE);
                } else {
                    query = em.createQuery("SELECT uc FROM UserByCourse uc JOIN uc.courseId c WHERE c.startDate >= :fechaInicio AND uc.userId = :usuario  ORDER BY uc.userByCourseId DESC");
                    query.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
                }
            } else {
                query = em.createQuery("SELECT uc FROM UserByCourse uc JOIN uc.courseId c WHERE c.finishDate >= :fechaFin  AND uc.userId = :usuario ORDER BY uc.userByCourseId DESC");
                query.setParameter("fechaFin", fechaFin, TemporalType.DATE);
            }
            query.setParameter("usuario", usuario);
//            query.setParameter("cursosPorFiltrar", cursosPorFiltrar);

            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarPorFecha = " + ex.getMessage());
        }
        return lista;

    }

    @Override
    public List<Course> filtrarPorNombre(String nombre, List<Integer> cursosPorFiltrar) {
        List<Course> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM Course c  WHERE c.courseName LIKE CONCAT('%',:nombre,'%') AND c.courseId IN :cursosPorFiltrar ORDER BY c.courseId DESC");
            query.setParameter("nombre", nombre);
            query.setParameter("cursosPorFiltrar", cursosPorFiltrar);
            lista = query.getResultList();

        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarPorNombre = " + ex.getMessage());
        }
        return lista;

    }

    @Override
    public List<Course> filtrarPorFechas(Date fechaInicio, Date fechaFin, List<Integer> cursosPorFiltrar) {
        List<Course> lista = new ArrayList<>();
        try {
            Query query;
            if (fechaInicio != null) {
                if (fechaFin != null) {
                    query = em.createQuery("SELECT c FROM Course c WHERE c.startDate >= :fechaInicio AND c.finishDate <= :fechaFin AND c.courseId IN :cursosPorFiltrar ORDER BY c.courseId DESC");
                    query.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
                    query.setParameter("fechaFin", fechaFin, TemporalType.DATE);
                    System.out.println("las dos");
                } else {
                    query = em.createQuery("SELECT c FROM Course c WHERE c.startDate >= :fechaInicio AND c.courseId IN :cursosPorFiltrar  ORDER BY c.courseId DESC");
                    query.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
                    System.out.println("solo inicio");

                }
            } else {
                query = em.createQuery("SELECT c FROM Course c WHERE c.finishDate >= :fechaFin AND c.courseId IN :cursosPorFiltrar ORDER BY c.courseId DESC");
                query.setParameter("fechaFin", fechaFin, TemporalType.DATE);
                System.out.println("solo fin");
            }
            query.setParameter("cursosPorFiltrar", cursosPorFiltrar);

            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarPorFecha = " + ex.getMessage());
        }
        return lista;

    }

}
