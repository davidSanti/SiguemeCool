/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.UserStatus;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}
