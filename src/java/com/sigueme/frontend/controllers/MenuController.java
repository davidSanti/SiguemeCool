/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Permission;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.PermissionFacadeLocal;
import com.sigueme.backend.model.PermissionRoleFacadeLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author RobayoDa
 */
@Named(value = "menuController")
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    private PermissionRoleFacadeLocal permissionRoleFacadeLocal;
    @EJB
    private PermissionFacadeLocal permissionFacadeLocal;

    private MenuModel model;
    private User usuario;
    private List<Permission> permisos;

    public MenuController() {
    }

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        usuario = new User();
        permisos = new ArrayList<>();
        this.listarPermisos();
        this.establecerPermisos();
    }

    public MenuModel getModel() {
        if (model.getElements().size() == 0) {
            listarPermisos();
            establecerPermisos();
        }
        return model;
    }

    //éste método lista todos los permisos que posee un usuario dependiendo de su rol (variable capturada al iniciar sesión)
    //en caso de que no se halla inicializado la variable "rol" se redirige al usuario al index (interfaz de login)
    public void listarPermisos() {
        FacesContext context = FacesContext.getCurrentInstance();
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        Role rol = retornarRolEnSesion();
        if (rol != null) {
            permisos = permissionRoleFacadeLocal.listarPermisosPorRol(rol);
        }
        if (permisos.isEmpty() || permisos == null) {
            try {
                context.getExternalContext().redirect(path + "/faces/index.xhtml");
                cerrarSesion();
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El usuario no tiene permisos"));

            } catch (IOException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //
    public void establecerPermisos() {
        for (Permission menuItem : permisos) {
            if (menuItem.getDependency() == null) {
                if (esPadre(menuItem)) {
                    DefaultMenuItem item = new DefaultMenuItem(menuItem.getDescription());
                    item.setUrl("/faces/" + menuItem.getUrl());
                    model.addElement(item);
                } else {
                    DefaultSubMenu firstSubmenu = new DefaultSubMenu(menuItem.getDescription());
                    for (Permission subMenuItem : permisos) {
                        Permission subPermiso = subMenuItem.getDependency();
                        if (subPermiso != null) {
                            if (menuItem.getPermissionId() == subPermiso.getPermissionId()) {
                                DefaultMenuItem item = new DefaultMenuItem(subMenuItem.getDescription());
                                item.setUrl("/faces/" + subMenuItem.getUrl());
                                firstSubmenu.addElement(item);
                            }
                        }
                    }
                    model.addElement(firstSubmenu);
                }
            }
        }
    }

    //éste método devuelve el tipo de item que es un permiso (menú --> padre || submenú --> hijo) buscando las posibles dependencias (subitems)
    //que tenga, es decir, si hay algún registro que su dependencia sea ese permiso
    public boolean esPadre(Permission permiso) {
        List<Permission> dependencias = permissionFacadeLocal.buscarDependenciasPorPermiso(permiso);
        boolean bandera = dependencias.isEmpty();
        return bandera;
    }

    //éste método verifica que exista un usuario en sesión, si es así debe validar que tenga permisos a esa página
    //en caso conrario se debe redirigir al index
    public void verificarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        try {
            User usuario = (User) context.getExternalContext().getSessionMap().get("usuario");
            String url = context.getExternalContext().getRequestPathInfo();
            if (usuario == null) {
                context.getExternalContext().redirect(path + "/faces/index.xhtml");
            } else if (!tienePermiso(url)) {
                context.getExternalContext().redirect(path + "/faces/pages/system/home.xhtml");
            }
        } catch (Exception e) {
        }
    }

    //este método verifica que un usuario tenga permisos para ingresar a un página, a partir de la ulr de la misma
    //De no validar ésto cualquier usuario que tenga una url válida puede ingresar abruptamente a cualquier página
    public boolean tienePermiso(String url) {
        if (url.equals("/pages/system/home.xhtml")) {
            return true;
        }
        for (Permission p : permisos) {
            if (p.getUrl() != null && p.getUrl().endsWith(url.replaceFirst("/", ""))) {
                return true;
            }
        }
        return false;
    }

    //éste método devuelve el nombre del usuario que se encuentra actualmente en sesión.
    //ésto lo vemos reflejado en el nombre que aparece en la parte derecha de la barra de navegación
    public String retornarNombreUsuario() {
        String nombre = retornarUsuarioEnSesion().getFirstName();
        return nombre;
    }

    public User retornarUsuarioEnSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();

        User usuarioEnSesion = (User) sesion.getAttribute("usuario");
        return usuarioEnSesion;
    }

    public Role retornarRolEnSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        Role rolEnSesion = (Role) sesion.getAttribute("rol");
        return rolEnSesion;
    }

    public String cerrarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

}
