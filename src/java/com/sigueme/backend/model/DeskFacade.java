/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Convention;
import com.sigueme.backend.entities.Desk;
import com.sigueme.backend.entities.Element;
import com.sigueme.backend.entities.GroupCls;
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
public class DeskFacade extends AbstractFacade<Desk> implements DeskFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DeskFacade() {
        super(Desk.class);
    }

    @Override
    public Desk buscarPorSerialCode(String serialCode) {
        Desk puesto = null;
        try {
            Query query = em.createQuery("SELECT d FROM Desk d WHERE d.serialCode = :serialCode");
            query.setParameter("serialCode", serialCode);
            List<Desk> lista = query.getResultList();
            if (!lista.isEmpty()) {
                puesto = lista.get(0);
            }
        } catch (Exception e) {
            System.out.println("Error en el método:buscarPorSerialCode = " + e.getMessage());
        }
        return puesto;
    }

    @Override
    public List<String> buscarPorSerialCodeAutoComplete(String serialCode) {
        List<String> listaString = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT d FROM Desk d WHERE d.serialCode LIKE CONCAT(:serialCode,'%')");
            query.setParameter("serialCode", serialCode);
            List<Desk> lista = query.getResultList();

            for (int i = 0; i < lista.size(); i++) {
                listaString.add(lista.get(i).getSerialCode());
            }

        } catch (Exception e) {
            System.out.println("Error en el método:buscarPorSerialCodeAutoComplete = " + e.getMessage());
        }
        return listaString;
    }

    @Override
    public List<Desk> listarPuestosPorGrupo(List<Convention> conenciones) {
        List<Desk> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT d FROM Desk d WHERE d.conventionId IN :conenciones");
            query.setParameter("conenciones", conenciones);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método:listarPuestosPorProyecto = " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Desk> listarPuestosPorSerialCodeYCovenciones(String serialCode, List<Convention> covenciones) {
        List<Desk> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT d FROM Desk d WHERE d.serialCode LIKE CONCAT('%',:serialCode,'%') AND d.conventionId IN :covenciones ");
            query.setParameter("covenciones", covenciones);
            query.setParameter("serialCode", serialCode);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el método:listarPuestosPorSerialCodeYCovenciones = " + e.getMessage());
        }
        return lista;
    }
}
