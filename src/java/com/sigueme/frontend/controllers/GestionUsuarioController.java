/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Permission;
import com.sigueme.backend.entities.PermissionRole;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.model.GroupClsFacadeLocal;
import com.sigueme.backend.model.PermissionFacadeLocal;
import com.sigueme.backend.model.PermissionRoleFacadeLocal;
import com.sigueme.backend.model.RoleFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author RobayoDa
 */
@Named(value = "gestionUsuarioController")
@ViewScoped
public class GestionUsuarioController implements  Serializable{

    @EJB
    private GroupClsFacadeLocal groupFacadeLocal;
    @EJB
    private RoleFacadeLocal roleFacadeLocal;
    @EJB
    private PermissionFacadeLocal permissionFacadeLocal;
    @EJB
    private PermissionRoleFacadeLocal permissionRoleFacadeLocal;

    private List<GroupCls> listaGrupos;
    private List<Role> listaRoles;
    private List<Permission> listaPermisos;
    private List<PermissionRole> listaPermisosRol;

    private GroupCls grupo;
    private Role rol;
    private Permission permiso;
    private PermissionRole permisoRol;

    public GestionUsuarioController() {
    }

    @PostConstruct
    public void init() {

        listarGrupos();
        listarRoles();
        listarPermisos();

        grupo = new GroupCls();
        rol = new Role();
        permiso = new Permission();
        permisoRol = new PermissionRole();

    }

    public void listarGrupos() {
        listaGrupos = new ArrayList<>();
        this.listaGrupos = groupFacadeLocal.findAll();
    }

    public void listarRoles() {
        listaRoles = new ArrayList<>();
        this.listaRoles = roleFacadeLocal.findAll();
    }

    public void listarPermisos() {
        listaPermisosRol = new ArrayList<>();
        listaPermisos = new ArrayList<>();
        this.listaPermisosRol = permissionRoleFacadeLocal.findAll();
        this.listaPermisos = permissionFacadeLocal.findAll();
    }

    public void registrarGrupo() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (verificarNombreGrupo(grupo.getGroupName())) {
                groupFacadeLocal.create(grupo);
                //Mensaje: El grupo se ha registrado correctamente   
            } else {
                //Mensaje: El "algo" ya se encuentra registrado en otro Grupo, verifica y vulve a intentarlo
            }

        } catch (Exception e) {
            //Mensaje: Ha ocurrido un error al registrar el grupo, intenta más tarde
        }

    }

    public boolean verificarNombreGrupo(String verificar) {
        List<GroupCls> lista = groupFacadeLocal.findAll();
        boolean bandera = true;

        for (GroupCls item : lista) {
            if (item.getGroupName().equals(verificar)) {
                if (!Objects.equals(item.getGroupId(), grupo.getGroupId())) {
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    public boolean verificarNombreGrupo(List<GroupCls> lista) {
        boolean bandera = true;

        String nombreOriginal = grupo.getGroupName().replaceAll(" ", "");
        nombreOriginal = nombreOriginal.toLowerCase();

        System.out.println("nombre:" + nombreOriginal);
        for (GroupCls item : lista) {
            String nombreItem = item.getGroupName().toLowerCase();
            nombreItem = nombreItem.replaceAll(" ", "");
            System.out.println("nombre:" + nombreItem);

            if (nombreItem.equals(nombreOriginal)) {
                if (!Objects.equals(item.getGroupId(), grupo.getGroupId())) {
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    public void registrarRole() {

    }

    public boolean verificarNombreRol(List<Role> lista) {
        boolean bandera = true;

        String nombreOriginal = rol.getDescription().replaceAll(" ", "");
        nombreOriginal = nombreOriginal.toLowerCase();

        System.out.println("nombre:" + nombreOriginal);
        for (Role item : lista) {
            String nombreItem = item.getDescription().toLowerCase();
            nombreItem = nombreItem.replaceAll(" ", "");
            System.out.println("nombre:" + nombreItem);

            if (nombreItem.equals(nombreOriginal)) {
                if (!Objects.equals(item.getRoleId(), rol.getRoleId())) {
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    public void editarGrupo(GroupCls grupo) {
        this.grupo = grupo;
    }

    public void editarGrupo() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            if (verificarNombreGrupo(grupo.getGroupName())) {
                groupFacadeLocal.edit(grupo);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "Los datos del grupo se han actualizado correctamente "));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra registrado un Grupo con ese nombre, verifica y vulve a intentarlo"));

            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al editar el grupo, intenta más tarde"));
        }

    }

    //Getter y setter
    public List<GroupCls> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<GroupCls> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public List<Role> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Role> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Permission> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<Permission> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public List<PermissionRole> getListaPermisosRol() {
        return listaPermisosRol;
    }

    public void setListaPermisosRol(List<PermissionRole> listaPermisosRol) {
        this.listaPermisosRol = listaPermisosRol;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public Permission getPermiso() {
        return permiso;
    }

    public void setPermiso(Permission permiso) {
        this.permiso = permiso;
    }

    public PermissionRole getPermisoRol() {
        return permisoRol;
    }

    public void setPermisoRol(PermissionRole permisoRol) {
        this.permisoRol = permisoRol;
    }

    public GroupCls getGrupo() {
        return grupo;
    }

    public void setGrupo(GroupCls grupo) {
        this.grupo = grupo;
    }
    
}
