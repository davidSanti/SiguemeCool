/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.GroupCls;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface GroupClsFacadeLocal {

    void create(GroupCls groupCls);

    void edit(GroupCls groupCls);

    void remove(GroupCls groupCls);

    GroupCls find(Object id);

    List<GroupCls> findAll();

    List<GroupCls> findRange(int[] range);

    int count();

    List<GroupCls> listarGrupos();

    List<GroupCls> listarGruposPorPersonasDelCurso(Course curso);
}
