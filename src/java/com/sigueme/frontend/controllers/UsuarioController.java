/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.GroupClsFacadeLocal;
import com.sigueme.backend.model.RoleFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Santi
 */
@Named(value = "usuarioController")
@ViewScoped
public class UsuarioController implements Serializable {

    @EJB
    private UserFacadeLocal userFacadeLocal;
    @EJB
    private GroupClsFacadeLocal groupFacadeLocal;
    @EJB
    private RoleFacadeLocal roleFacadeLocal;

    private List<User> listaUsuarios;
    private List<GroupCls> listaGrupo;
    private List<Role> listaRoles;
    private User usuario;

    private String parametroBusqueda;

    @PostConstruct
    public void init() {
        usuario = new User();
        listaGrupo = new ArrayList<>();
        listaRoles = new ArrayList<>();
        listarUsuarios();
    }

    public UsuarioController() {
    }

    public void listarUsuarios() {
//        listaUsuarios = new ArrayList<>();
        listaUsuarios = userFacadeLocal.findAll();
    }

    public void filtrarUsuarios() {
        List<User> listaVacia = new ArrayList<>();
        if (!listaGrupo.isEmpty()) {

            if (!listaRoles.isEmpty()) {
                listaUsuarios = userFacadeLocal.filtrarUsuariosPorRolYGrupos(listaRoles, listaGrupo, listaVacia);
            } else {
                listaUsuarios = userFacadeLocal.filtrarUsuariosPorGrupo(listaGrupo, listaVacia);
            }
        } else {
            if (!listaRoles.isEmpty()) {
                listaUsuarios = userFacadeLocal.filtrarUsuariosPorRol(listaRoles, listaVacia);
            } else {
                listaUsuarios = new ArrayList<>();
                listarUsuarios();
            }
        }
    }

    public void filtrarTodosLosCampos() {
        listaUsuarios = new ArrayList<>();
        listaUsuarios = userFacadeLocal.filtrarPorTodosLosCampos(parametroBusqueda);
    }

    public void limpiarFiltro(){
        parametroBusqueda = "";
        listarUsuarios();
        listaGrupo = new ArrayList<>();
        listaRoles = new ArrayList<>();
    }
    
    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('verUsuario').hide()");
                formulario = "formVerUsuario:gridVerUsuario";
                break;
            case 2:
                req.execute("PF('editarUsuario').hide()");
                formulario = "formEditarUsuario:gridEditarUsuario";
                break;
            default:
                break;
        }

        req.reset(formulario);
    }

    public void editarUsuario(User user) {
        this.usuario = user;
    }

    public void editarUsuario() {

    }

    public List<GroupCls> listarGrupos() {
        return groupFacadeLocal.findAll();
    }

    public List<Role> listarRoles() {
        return roleFacadeLocal.findAll();
    }

    //Método Getter y Setter
    public List<User> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<User> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<GroupCls> getListaGrupo() {
        return listaGrupo;
    }

    public void setListaGrupo(List<GroupCls> listaGrupo) {
        this.listaGrupo = listaGrupo;
    }

    public List<Role> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Role> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public String getParametroBusqueda() {
        return parametroBusqueda;
    }

    public void setParametroBusqueda(String parametroBusqueda) {
        this.parametroBusqueda = parametroBusqueda;
    }
    
    

}
