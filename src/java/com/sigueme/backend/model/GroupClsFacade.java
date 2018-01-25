/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Course;
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
public class GroupClsFacade extends AbstractFacade<GroupCls> implements GroupClsFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupClsFacade() {
        super(GroupCls.class);
    }

    @Override
    public List<GroupCls> listarGrupos() {
        List<GroupCls> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT g FROM GroupCls g WHERE g.groupId <> 1");
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo listarGrupos= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<GroupCls> findAll() {
        List<GroupCls> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT g FROM GroupCls g ORDER BY g.groupId ASC");
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo findAll Grupos= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<GroupCls> listarGruposPorPersonasDelCurso(Course curso) {
        List<GroupCls> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT DISTINCT g FROM UserByCourse uc INNER JOIN uc.userId u INNER JOIN u.groupId g WHERE uc.courseId = :curso");
            query.setParameter("curso", curso);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro en el método filtrarGruposPorPersonasDelCurso=" + e.getMessage());
        }
        return lista;
    }
 }
