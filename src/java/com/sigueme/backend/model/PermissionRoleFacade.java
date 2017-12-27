/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Permission;
import com.sigueme.backend.entities.PermissionRole;
import com.sigueme.backend.entities.Role;
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
public class PermissionRoleFacade extends AbstractFacade<PermissionRole> implements PermissionRoleFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermissionRoleFacade() {
        super(PermissionRole.class);
    }

    @Override
    public List<Permission> listarPermisosPorRol(Role rol) {
        List<Permission> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT p FROM PermissionRole pr JOIN pr.permissionId p WHERE pr.roleId = :rol ORDER BY p.permissionId ASC", Permission.class);
            query.setParameter("rol", rol);

            lista = query.getResultList();                        
        } catch (Exception e) {
            System.out.println("Error en el método: listarPermisosPorRol =" + e.getMessage());
        }
        return lista;

    }

}
