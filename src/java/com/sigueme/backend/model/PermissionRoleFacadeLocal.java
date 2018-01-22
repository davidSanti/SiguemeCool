/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Permission;
import com.sigueme.backend.entities.PermissionRole;
import com.sigueme.backend.entities.Role;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface PermissionRoleFacadeLocal {

    void create(PermissionRole permissionRol);

    void edit(PermissionRole permissionRol);

    void remove(PermissionRole permissionRol);

    PermissionRole find(Object id);

    List<PermissionRole> findAll();

    List<PermissionRole> findRange(int[] range);

    int count();

    List<Permission> listarPermisosPorRol(Role rol);

    void eliminarPermisosRol(Permission permisoEliminado, Role rolEliminado);

    List<PermissionRole> listarPermisosPorRol2(Role rol);
}
