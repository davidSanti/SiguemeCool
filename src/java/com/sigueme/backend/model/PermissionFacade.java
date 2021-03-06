/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Permission;
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
public class PermissionFacade extends AbstractFacade<Permission> implements PermissionFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermissionFacade() {
        super(Permission.class);
    }

    @Override
    public List<Permission> buscarDependenciasPorPermiso(Permission permiso) {
        List<Permission> pe = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT pe FROM Permission pe WHERE pe.dependency =:permiso");
            query.setParameter("permiso", permiso);
            pe = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método buscarDependenciasPorPermiso Eror: " + e.getMessage());
        }
        return pe;
    }

    @Override
    public List<Permission> findAll() {
        List<Permission> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT p FROM Permission p ORDER BY p.permissionId ASC");
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo findAll Permisos= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Permission> listarPermisosSinDependencia() {
        List<Permission> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT p FROM Permission p  WHERE p.dependency IS NULL ORDER BY p.permissionId ASC");
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo listarPermisosSinDependencia= " + ex.getMessage());
        }
        return lista;
    }
}
