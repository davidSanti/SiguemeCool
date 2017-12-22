/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.GroupCls;
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
public class RoleFacade extends AbstractFacade<Role> implements RoleFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleFacade() {
        super(Role.class);
    }
    
    @Override
    public List<Role> findAll() {
        List<Role> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT r FROM Role r ORDER BY r.roleId ASC");
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo findAll Roles= " + ex.getMessage());
        }
        return lista;
    }
}
