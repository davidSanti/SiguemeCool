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
}
