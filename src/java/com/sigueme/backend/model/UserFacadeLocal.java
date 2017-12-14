/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface UserFacadeLocal {

    void create(User user);

    void edit(User user);

    void remove(User user);

    User find(Object id);

    List<User> findAll();

    List<User> findRange(int[] range);

    int count();

    User iniciarSesion(User user);

    List<User> filtrarUsuariosPorGrupo(List<GroupCls> listaGrupos, List<User> usuariosExcluidos);

    List<User> filtrarUsuariosPorRol(List<Role> listaRoles, List<User> usuariosExcluidos);

    List<User> filtrarUsuariosPorRolYGrupos(List<Role> listaRoles, List<GroupCls> listaGrupos, List<User> usuariosExcluidos);

    List<User> buscarPersonaPorNombre(String nombre);
    
    List<User> filtrarPorTodosLosCampos(String parametro);
    
    List<User> buscarPorIdentificacion(String identificacion);
    
    List<User> listarUsuariosSiteManager();
}
