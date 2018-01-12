/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.ElementType;
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
public class ElementTypeFacade extends AbstractFacade<ElementType> implements ElementTypeFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ElementTypeFacade() {
        super(ElementType.class);
    }
    
    @Override 
    public List<ElementType> findAll(){
        List<ElementType> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT et FROM ElementType et ORDER BY et.elementTypeId ASC");
            lista = query.getResultList();                    
        } catch (Exception e) {
            System.out.println("Error en el método findAll de elements Type");
        }
        return lista;
    }
}
