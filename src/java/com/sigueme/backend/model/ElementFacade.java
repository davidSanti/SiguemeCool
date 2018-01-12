/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Desk;
import com.sigueme.backend.entities.Element;
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
    public List<Element> findAll() {
        List<Element> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM Element e ORDER BY e.elementId ASC");
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método findAll = " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Element> listarElementosMsc() {
        List<Element> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM Element e WHERE e.deskId IS NULL");
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método listarElementosMsc = " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Element> verificarIdentification(String identification, int option) {
        List<Element> lista = new ArrayList<>();
        try {
            Query query = null;
            if (option == 1) {
                query = em.createQuery("SELECT e FROM Element e WHERE e.serialCode = :identification");
            } else if (option == 2) {
                query = em.createQuery("SELECT e FROM Element e WHERE e.inventoryPlaque = :identification");
            }
            query.setParameter("identification", identification);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método verificarIdentification = " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Element> listarElementosPorPuesto(Desk puesto) {
        List<Element> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM Element e WHERE e.deskId = :puesto");
            query.setParameter("puesto", puesto);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método verificarIdentification = " + e.getMessage());
        }
        return lista;
    }
    
//    @Override
//    public List<Element> filtrarElmentosPorTipo(List<ElementType> listaTipo) {
//        List<Element> lista = new ArrayList<>();
//        try {
//            Query query = em.createQuery("SELECT e FROM Element e WHERE e.typeId IN :tipo ORDER BY e.typeId ASC");
//            query.setParameter("tipo", listaTipo);
//            lista = query.getResultList();
//        } catch (Exception e) {
//            System.out.println("Error en el método filtrarElmentosPorTipo = " + e.getMessage());
//        }
//        return lista;
//    }
    
    @Override
    public List<Element> filtrarElmentosPorTipo(List<ElementType> listaTipo, String multicriterio) {
        List<Element> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM Element e WHERE e.typeId IN :tipo AND (e.serialCode LIKE CONCAT('%',:criterio,'%') OR e.inventoryPlaque LIKE CONCAT('%',:criterio,'%')) ORDER BY e.typeId ASC");
            query.setParameter("tipo", listaTipo);
            query.setParameter("criterio", multicriterio);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método filtrarElmentosPorTipo = " + e.getMessage());
        }
        return lista;
    }

}
