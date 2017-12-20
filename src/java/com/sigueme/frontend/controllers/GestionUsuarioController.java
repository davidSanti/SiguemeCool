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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author RobayoDa
 */
@Named(value = "gestionUsuarioController")
@ViewScoped
public class GestionUsuarioController {

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
//listaPermisos = new ArrayList<>();

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
        this.listaPermisosRol = permissionRoleFacadeLocal.findAll();
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

    public boolean verificarNombre(List<?> lista, String verificar) {
//        List<GroupCls> lista = groupFacadeLocal.findAll();
        boolean bandera = true;
        
        for (int i = 0; i <= lista.size(); i++) {
            lista.get(i).getClass().
//            if (item.getGroupName().equals(verificar)) {
//                if (!Objects.equals(item.getGroupId(), grupo.getGroupId())) {
//                    bandera = false;
//                }
//            }
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
                //Mensaje: Los datos del grupo se han actualizado correctamente   
            } else {
//                    Ya se encuentra registrado un Grupo con ese nombre
//                    , verifica y vulve a intentarlo
            }

        } catch (Exception e) {
            //Mensaje: Ha ocurrido un error al editar el grupo, intenta más tarde
        }

    }

//    public boolean verificarNombreGrupo(String nombre) {
//
//        String nombreOriginial = grupo.toLowerCase();
//        String nombre = nombreOriginial.strim(" ", "");
//
//        return verificarGrupo(2, nombre);
//
//    }
    public boolean verificarNombreRol(String Rol) {

//String nombreOriginial = 
        return true;
    }

}
