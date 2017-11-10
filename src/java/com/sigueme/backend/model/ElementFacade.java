/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Element;
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
public class ElementFacade extends AbstractFacade<Element> implements ElementFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ElementFacade() {
        super(Element.class);
    }
    
    @Override
    public List<Element> listarElementosMsc(){
        List<Element> lista = new ArrayList<>();
        try {
                Query query = em.createQuery("SELECT e FROM Desk d LEFT JOIN d.elements e WHERE e.elementId IS NULL");
            System.out.println("tamaño" + query.getResultList());
        } catch (Exception e) {
            System.out.println("Error en el método listarElementosMsc = " + e.getMessage());
        }
        return lista;
    }
    
}
