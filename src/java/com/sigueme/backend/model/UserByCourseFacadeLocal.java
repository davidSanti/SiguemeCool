/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface UserByCourseFacadeLocal {

    void create(UserByCourse userByCourse);

    void edit(UserByCourse userByCourse);

    void remove(UserByCourse userByCourse);

    UserByCourse find(Object id);

    List<UserByCourse> findAll();

    List<UserByCourse> findRange(int[] range);

    int count();

    List<UserByCourse> listarUsuariosPorCurso(Course curso);
    
    List<UserByCourse> listarMisCursos(User user);
}
