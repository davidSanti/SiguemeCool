/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Convention;
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
public class ConventionFacade extends AbstractFacade<Convention> implements ConventionFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConventionFacade() {
        super(Convention.class);
    }

    @Override
    public List<Convention> findAll() {
        List<Convention> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM Convention c ORDER BY c.conventionId ASC");
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en el método: findAll Convenciones");
        }
        return lista;
    }
}
