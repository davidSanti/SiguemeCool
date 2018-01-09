/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserStatus;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ZamudioL
 */
@Stateless
public class UserStatusFacade extends AbstractFacade<UserStatus> implements UserStatusFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserStatusFacade() {
        super(UserStatus.class);
    }

    @Override
    public List<UserStatus> listarEstados() {
        List<UserStatus> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT us FROM UserStatus us WHERE us.userStatusId NOT IN (2,3,4)");
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo filtrarPorTodosLosCampos= " + e.getMessage());
        }
        return lista;
    }

}
