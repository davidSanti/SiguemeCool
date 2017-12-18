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
import java.security.acl.Group;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface CourseFacadeLocal {

    void create(Course course);

    void edit(Course course);

    void remove(Course course);

    Course find(Object id);

    List<Course> findAll();

    List<Course> findRange(int[] range);

    int count();

    List<Course> listarCursos();

    List<Course> listarCursosPorEstado(boolean estado);

    List<UserByCourse> filtrarUsuariosPorGrupo(List<GroupCls> listaGrupos, Course curso, List<User> listaParaFiltrar);

    boolean validarEvidenciaUsuariosCurso(Course curso);

    void vencerCursos();

    List<UserByCourse> filtrarMisCursosPorNombre(String nombre, User usuario);

    List<UserByCourse> filtrarMisCursosPorFechas(Date fechaInicio, Date fechaFin, User usuario);

    List<Course> filtrarPorNombre(String nombre, List<Integer> cursosPorFiltrar);

    List<Course> filtrarPorFechas(Date fechaInicio, Date fechaFin, List<Integer> cursosPorFiltrar);

    List<GroupCls> listarGrupos(Course curso);
}
