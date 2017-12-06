/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
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
public class UserFacade extends AbstractFacade<User> implements UserFacadeLocal {

    @PersistenceContext(unitName = "SiguemePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    @Override
    public User iniciarSesion(User user) {
        User usuario = null;
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.identification = :cedula AND u.userPassword = :clave");
            query.setParameter("cedula", user.getIdentification());
            query.setParameter("clave", user.getUserPassword());
            //query.setParameter("estado", UserStatus.ACTIVE);

            List<User> usuariosLista = query.getResultList();
            if (!usuariosLista.isEmpty()) {
                usuario = usuariosLista.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return usuario;
    }

    @Override
    public List<User> filtrarUsuariosPorGrupo(List<GroupCls> listaGrupos, List<User> usuariosExcluidos) {
        List<User> lista = new ArrayList<>();
        try {
            Query query;
            if (usuariosExcluidos.isEmpty()) {
                query = em.createQuery("SELECT u FROM User u WHERE u.groupId IN :grupos", User.class);
            } else {
                query = em.createQuery("SELECT DISTINCT u FROM UserByCourse uc JOIN uc.userId u WHERE u.groupId IN :grupos AND u NOT IN :usuariosExcluidos", User.class);
                query.setParameter("usuariosExcluidos", usuariosExcluidos);
            }
            query.setParameter("grupos", listaGrupos);
            lista = query.getResultList();

        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorGrupo= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> filtrarUsuariosPorRol(List<Role> listaRoles, List<User> usuariosExcluidos) {
        List<User> lista = new ArrayList<>();
        try {
            Query query;
            if (usuariosExcluidos.isEmpty()) {
                query = em.createQuery("SELECT u FROM User u WHERE u.roleId IN :roles");
            } else {
                query = em.createQuery("SELECT DISTINCT u FROM UserByCourse uc JOIN uc.userId u WHERE u.roleId IN :roles AND u NOT IN :usuariosExcluidos");
                query.setParameter("usuariosExcluidos", usuariosExcluidos);
            }
            query.setParameter("roles", listaRoles);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorRol= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> filtrarUsuariosPorRolYGrupos(List<Role> listaRoles, List<GroupCls> listaGrupos, List<User> usuariosExcluidos) {
        List<User> lista = new ArrayList<>();
        try {
            Query query;
            if (usuariosExcluidos.isEmpty()) {
                query = em.createQuery("SELECT u FROM User u WHERE u.groupId IN :grupos AND u.roleId IN :roles");

            } else {
                query = em.createQuery("SELECT DISTINCT u FROM UserByCourse uc JOIN uc.userId u WHERE u.groupId IN :grupos AND u.roleId IN :roles AND u NOT IN :usuariosExcluidos");
                query.setParameter("usuariosExcluidos", usuariosExcluidos);
            }
            query.setParameter("grupos", listaGrupos);
            query.setParameter("roles", listaRoles);
            lista = query.getResultList();

        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorRolYGrupos= " + ex.getMessage());
        }
        return lista;
    }
}
