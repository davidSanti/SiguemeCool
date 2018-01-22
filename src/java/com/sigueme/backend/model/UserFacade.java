/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
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
            Query query = em.createQuery("SELECT u FROM User u WHERE (u.identification = :cedula OR u.peopleSoft = :cedula) AND u.userPassword = :clave AND u.userStatusId.userStatusId <> :estadoUsuario");
            query.setParameter("cedula", user.getIdentification());
            query.setParameter("clave", user.getUserPassword());
            query.setParameter("estadoUsuario", 2);

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
    public List<User> filtrarUsuariosPorGrupo(List<GroupCls> listaGrupos, List<Integer> usuariosExcluidos) {
        List<User> lista = new ArrayList<>();
        try {
            Query query;
            if (usuariosExcluidos.isEmpty()) {
                query = em.createQuery("SELECT u FROM User u WHERE u.groupId IN :grupos AND u.userStatusId.userStatusId <> :estadoUsuario", User.class);
            } else {
//                query = em.createQuery("SELECT DISTINCT u FROM UserByCourse uc JOIN uc.userId u WHERE u.groupId IN :grupos AND u NOT IN :usuariosExcluidos AND u.userStatusId.userStatusId <> :estadoUsuario", User.class);
                query = em.createQuery("SELECT u FROM User u WHERE u.groupId IN :grupos AND u.userId NOT IN :usuariosExcluidos AND u.userStatusId.userStatusId <> :estadoUsuario", User.class);
                query.setParameter("usuariosExcluidos", usuariosExcluidos);
            }
            query.setParameter("grupos", listaGrupos);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();

        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorGrupo= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> filtrarUsuariosPorRol(List<Role> listaRoles, List<Integer> usuariosExcluidos) {
        List<User> lista = new ArrayList<>();
        try {
            Query query;
            if (usuariosExcluidos.isEmpty()) {
                query = em.createQuery("SELECT u FROM User u WHERE u.roleId IN :roles AND u.userStatusId.userStatusId <> :estadoUsuario");
            } else {
                query = em.createQuery("SELECT DISTINCT u FROM UserByCourse uc JOIN uc.userId u WHERE u.roleId IN :roles AND u NOT IN :usuariosExcluidos AND u.userStatusId.userStatusId <> :estadoUsuario");
                query.setParameter("usuariosExcluidos", usuariosExcluidos);
            }
            query.setParameter("roles", listaRoles);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorRol= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> filtrarUsuariosPorRol(Role rol) {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.roleId = :rol");
            query.setParameter("rol", rol);
            lista = query.getResultList();
        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorRol= " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> filtrarUsuariosPorRolYGrupos(List<Role> listaRoles, List<GroupCls> listaGrupos, List<Integer> usuariosExcluidos) {
        List<User> lista = new ArrayList<>();
        try {
            Query query;
            if (usuariosExcluidos.isEmpty()) {
                query = em.createQuery("SELECT u FROM User u WHERE u.groupId IN :grupos AND u.roleId IN :roles AND u.userStatusId.userStatusId <> :estadoUsuario");

            } else {
                query = em.createQuery("SELECT DISTINCT u FROM UserByCourse uc JOIN uc.userId u WHERE u.groupId IN :grupos AND u.roleId IN :roles AND u NOT IN :usuariosExcluidos AND u.userStatusId.userStatusId <> :estadoUsuario");
                query.setParameter("usuariosExcluidos", usuariosExcluidos);
            }
            query.setParameter("grupos", listaGrupos);
            query.setParameter("roles", listaRoles);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();

        } catch (Exception ex) {
            System.out.println("Error en el metodo filtrarUsuariosPorRolYGrupos= " + ex.getMessage());
        }
        return lista;
    }

    //Este método no se está utilizando, cuadno se utilice elimianr comentario
    @Override
    public List<User> buscarPersonaPorNombre(String nombre) {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.firstName LIKE CONCAT('%',:nombre,'%') OR u.lastName LIKE CONCAT('%',:nombre,'%') AND u.userStatusId.userStatusId <> :estadoUsuario");
            query.setParameter("nombre", nombre);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo buscarPersonaPorNombre= " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> filtrarPorTodosLosCampos(String parametro) {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.peopleSoft LIKE CONCAT('%',:parametro,'%') OR u.firstName LIKE CONCAT('%',:parametro,'%') OR u.lastName LIKE CONCAT('%',:parametro,'%') OR u.email LIKE CONCAT('%',:parametro,'%') AND u.userStatusId.userStatusId <> :estadoUsuario");
            query.setParameter("parametro", parametro);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo filtrarPorTodosLosCampos= " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> buscarPorIdentificacion(String identificacion) {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.identification = :identificacion OR u.peopleSoft = :identificacion AND u.userStatusId.userStatusId <> :estadoUsuario");
            query.setParameter("identificacion", identificacion);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo buscarPorIdentificacion= " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> listarUsuariosSiteManager() {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.roleId.roleId = :rol AND u.userStatusId.userStatusId <> :estadoUsuario");
            query.setParameter("rol", 1);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo listarUsuariosSiteManager= " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> listarUsuarios() {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u ORDER BY u.userId DESC");
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo listarUsuarios= " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<User> listarSupervisorPorGrupo(GroupCls grupo) {
        List<User> lista = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.groupId = :grupo AND u.userStatusId.userStatusId <> :estadoUsuario ORDER BY u.userId DESC");
            query.setParameter("grupo", grupo);
            query.setParameter("estadoUsuario", 2);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error en el metodo buscarPorCedula= " + e.getMessage());
        }
        return lista;
    }

    @Override
    public User verificarCorreoEIdentificacion(String identificacion, String correo) {
        List<User> lista = new ArrayList();
        User user = null;
        try {
            Query quey = em.createQuery("SELECT u FROM User u WHERE (u.identification = :identificacion OR u.peopleSoft = :identificacion) AND u.email = :correo AND u.userStatusId.userStatusId <> :estadoUsuario", User.class);
            quey.setParameter("identificacion", identificacion);
            quey.setParameter("correo", correo);
            quey.setParameter("estadoUsuario", 2);
            lista = quey.getResultList();
            if (!lista.isEmpty()) {
                user = lista.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return user;
    }
}
