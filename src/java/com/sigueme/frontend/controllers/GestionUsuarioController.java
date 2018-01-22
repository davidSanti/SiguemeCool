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
import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.GroupClsFacadeLocal;
import com.sigueme.backend.model.PermissionFacadeLocal;
import com.sigueme.backend.model.PermissionRoleFacadeLocal;
import com.sigueme.backend.model.RoleFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author RobayoDa
 */
@Named(value = "gestionUsuarioController")
@ViewScoped
public class GestionUsuarioController implements Serializable {

    @EJB
    private UserFacadeLocal userFacadeLocal;
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
    private List<Permission> listaPermisosRegistrar;
    private List<Permission> listaPermisosConUrl;
    private List<Permission> listaAsignarPermisos;
    private List<PermissionRole> listaPermisosRol;

    private GroupCls grupo;
    private Role rol;
    private Permission permiso;
    private PermissionRole permisoRol;
    private boolean validacionDependencia = false; //Se creo esta variable para validar si un permiso tiene dependencia DEBE TENER URL
    private boolean validarBotonEditar = false;
    private int nuevoRolUsuario;

    public GestionUsuarioController() {
    }

    @PostConstruct
    public void init() {
        listaPermisosRol = new ArrayList<>();
        listaPermisos = new ArrayList<>();
        listarGrupos();
        listarRoles();
        listarPermisos();
        grupo = new GroupCls();
        rol = new Role();
        permiso = new Permission();
        permisoRol = new PermissionRole();
        nuevoRolUsuario = 0;
        listarDependecia(null);
        listaPermisosRegistrar = permissionFacadeLocal.listarPermisosSinDependencia();
    }

    public void listarGrupos() {
        listaGrupos = new ArrayList<>();
        this.listaGrupos = groupFacadeLocal.findAll();
    }

    public void listarRoles() {
        listaRoles = new ArrayList<>();
        this.listaRoles = roleFacadeLocal.findAll();
    }

    public List<Permission> listarPermisos() {
        return listaPermisos = permissionFacadeLocal.findAll();
    }

    /*Inicio método Módulo Grupos*/
    public void registrarGrupo() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (verificarNombreGrupo()) {
                groupFacadeLocal.create(grupo);
                listarGrupos();
                ocultarModal(1);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El grupo se ha registrado correctamente "));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra un Grupo registrado con ese nombre, verifica y vuelve a intentarlo"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al registrar el grupo, intenta más tarde"));
        }

    }

    public boolean verificarNombreGrupo() {
        boolean bandera = true;
        List<GroupCls> lista = groupFacadeLocal.findAll();
        String nombreOriginal = grupo.getGroupName().replaceAll(" ", "");
        nombreOriginal = nombreOriginal.toLowerCase();

        for (GroupCls item : lista) {
            String nombreItem = item.getGroupName().toLowerCase();
            nombreItem = nombreItem.replaceAll(" ", "");

            if (nombreItem.equals(nombreOriginal)) {
                if (!Objects.equals(item.getGroupId(), grupo.getGroupId())) {
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    public void editarGrupo(GroupCls grupo) {
        this.grupo = grupo;
    }

    public void eliminarGrupo(GroupCls grupo) {
        this.grupo = grupo;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (asignarUsuariosAlMSC()) {
                groupFacadeLocal.remove(this.grupo);
                listarGrupos();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "El grupo se han eliminado correctamente "));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Algunas personas no pudieron ser asignadas al MSC, intenta más tarde"));
            }
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al eliminar el grupo, intenta más tarde"));
        }
    }

    public boolean asignarUsuariosAlMSC() {
        boolean bandera = true;
        List<GroupCls> list = new ArrayList<>();
        list.add(grupo);
        try {

            List<User> lista = userFacadeLocal.filtrarUsuariosPorGrupo(list, new ArrayList<>());
            for (User user : lista) {
                System.out.println("uer" + user.getFirstName());
                //Se asigna el grupo del MSC que tiene el id de grupo 1
                user.setGroupId(groupFacadeLocal.find(1));
                userFacadeLocal.edit(user);
            }
        } catch (Exception e) {
            bandera = false;
        }
        return bandera;
    }

    public void editarGrupo() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            if (verificarNombreGrupo()) {
                groupFacadeLocal.edit(grupo);
                ocultarModal(2);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "Los datos del grupo se han actualizado correctamente "));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra registrado un Grupo con ese nombre, verifica y vuelve a intentarlo"));

            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al editar el grupo, intenta más tarde"));
        }
    }

    /*Fin método Módulo Grupos*/

 /*Inicio método Módulo Permiso*/
    public void registrarPermiso() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            if (verificarNombrePermiso()) {
                if (verificarUrlPermiso()) {
                    permissionFacadeLocal.create(permiso);
                    verificarDependenciaUrl();
                    ocultarModal(3);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "El permiso Se ha registrado correctamente"));
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                            "Ya se encuentra registrado un permiso con la misma url, verifica y vuelve a intentarlo"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra registrado un permiso con ese nombre, verifica y vuelve a intentarlo"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al Registrar el permiso, intenta más tarde"));
        }
    }

    public void editarPermiso(Permission permission) {
        this.permiso = permission;
        listarDependecia(permission);
        verificarDependencia();
    }

    public void listarDependecia(Permission permission) {
        listaPermisos = permissionFacadeLocal.listarPermisosSinDependencia();
        if (permission != null && permission.getPermissionId() != null) {
            listaPermisos.remove(permission);
        }
    }

    //Este método sirve para que los permisos "padres" es decir, que tengan submenu no tengan url(dado que solo los items deben tener url
    //esto es importante porque al momento de aisgnar permisos al rol solo debemos mostrarle los items "hijos" o unicos
    //y se le asgina el permiso hijo y el padre
    public void verificarDependenciaUrl() {
        Permission dependencia = permiso.getDependency();
        if (dependencia != null) {
            dependencia.setUrl(null);
            permissionFacadeLocal.edit(dependencia);
        }
    }

    public boolean verificarSiEsPadre() {
        return permissionFacadeLocal.buscarDependenciasPorPermiso(permiso).isEmpty();

    }

    public void editarPermiso() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            if (verificarNombrePermiso()) {
                if (verificarUrlPermiso()) {
                    permissionFacadeLocal.edit(permiso);
                    verificarDependenciaUrl();

                    ocultarModal(4);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "Los datos del permiso se han actualizado correctamente "));
                    System.out.println("todo marcha bien");
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                            "Ya se encuentra registrado un permiso con la misma url, verifica y vuelve a intentarlo"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra registrado un permiso con ese nombre, verifica y vuelve a intentarlo"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al editar el permiso, intenta más tarde"));
        }
    }

    public boolean verificarUrlPermiso() {
        boolean bandera = true;
        if (permiso.getUrl() != null && !permiso.getUrl().equals("")) {
            List<Permission> lista = permissionFacadeLocal.findAll();
            String nombreOriginal = permiso.getUrl().replaceAll(" ", "");
            nombreOriginal = nombreOriginal.toLowerCase();

            for (Permission item : lista) {
                if (item.getUrl() != null && !item.getUrl().equals("")) {
                    String nombreItem = item.getUrl().toLowerCase();
                    nombreItem = nombreItem.replaceAll(" ", "");

                    if (nombreItem.equals(nombreOriginal)) {
                        if (!Objects.equals(item.getPermissionId(), permiso.getPermissionId())) {
                            System.out.println("aaaaa entro");
                            bandera = false;
                        }
                    }
                }
            }
        }
        return bandera;
    }

    public boolean verificarNombrePermiso() {
        boolean bandera = true;
        List<Permission> lista = permissionFacadeLocal.findAll();
        String nombreOriginal = permiso.getDescription().replaceAll(" ", "");
        nombreOriginal = nombreOriginal.toLowerCase();

        for (Permission item : lista) {
            String nombreItem = item.getDescription().toLowerCase();
            nombreItem = nombreItem.replaceAll(" ", "");

            if (nombreItem.equals(nombreOriginal)) {
                if (!Objects.equals(item.getPermissionId(), permiso.getPermissionId())) {
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    public boolean verificarDependencia(ValueChangeEvent event) {
        System.out.println("estoy entradno");
        if (this.permiso.getDependency() != null) {
            validacionDependencia = false;
        } else {
            validacionDependencia = true;
        }
        return validacionDependencia;
    }

    public boolean verificarDependencia() {
        if (this.permiso.getDependency() != null) {
            validacionDependencia = false;
        } else {
            validacionDependencia = true;
        }
        return validacionDependencia;
    }

    public boolean verificarSiEliminarPermiso(Permission permiso) {
        boolean bandera = false;
        String url = FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo();
        listarPermisos();
        Permission permisoOriginal = null;
        for (Permission p : listaPermisos) {
            if (p.getUrl() != null && p.getUrl().equals(url.replaceFirst("/", ""))) {
                permisoOriginal = p;
                break;
            }
        }

        if (permisoOriginal != null && Objects.equals(permisoOriginal.getPermissionId(), permiso.getPermissionId())) {
            bandera = true;
        }
        return bandera;
    }

    public void eliminarPermiso(Permission permiso) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.permiso = permiso;
        boolean bandera = false;
        try {
            if (eliminarPermisosoRol()) {
                eliminarDependencias();
                permissionFacadeLocal.remove(permiso);
                bandera = true;
                //no muestra e msm pero bien perrito 
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "El permiso se ha eliminado correctamente"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ha ocurrido un error al eliminar el permiso, intenta más tarde"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "El permiso no pudo ser eliminado, intenta más tarde"));
        }
    }

    public boolean eliminarPermisosoRol() {
        boolean bandera = false;
        try {
            permissionRoleFacadeLocal.eliminarPermisosRol(permiso, new Role());
            bandera = true;
        } catch (Exception e) {
        }
        return bandera;
    }

    public void eliminarDependencias() {
        List<Permission> lista = permissionFacadeLocal.buscarDependenciasPorPermiso(permiso);
        for (Permission permission : lista) {
            permission.setDependency(null);
            permissionFacadeLocal.edit(permission);
        }
    }

    /*Fin método Módulo Permiso*/

 /*Inicio Módulo Rol*/
    public void registrarRol() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!listaPermisosRol.isEmpty()) {
                roleFacadeLocal.create(rol);
                for (PermissionRole permissionRole : listaPermisosRol) {
                    permissionRole.setRoleId(rol);
                    permissionRoleFacadeLocal.create(permissionRole);
                }
                ocultarModal(5);
                ocultarModal(6);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "Los permisos se asignaron correctamente"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "No puedes registrar un rol sin asignarle permisos"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al asignar los permisos, intenta más tarde"));
        }

    }

    public boolean verificarNombreRol() {
        boolean bandera = true;
        List<Role> lista = roleFacadeLocal.findAll();
        String nombreOriginal = rol.getDescription().replaceAll(" ", "");
        nombreOriginal = nombreOriginal.toLowerCase();

        for (Role item : lista) {
            String nombreItem = item.getDescription().toLowerCase();
            nombreItem = nombreItem.replaceAll(" ", "");

            if (nombreItem.equals(nombreOriginal)) {
                if (!Objects.equals(item.getRoleId(), rol.getRoleId())) {
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    public void abrirModalAsignarPerimsos() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (verificarNombreRol()) {
            if (permiso != null) {
                listarPermisosConDependencia();
                listaPermisosRol = new ArrayList<>();
            }
            abrirModal(1);
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                    "Ya exite un rol con el mismo nombre, verifica por favor"));
        }
    }

    public void listarPermisosConDependencia() {
        List<Permission> lista = permissionFacadeLocal.findAll();
        listaPermisosConUrl = new ArrayList<>();
        for (Permission item : lista) {
            if (item.getUrl() != null) {
                listaPermisosConUrl.add(item);
            }
        }
    }

    public void asignarPermisoRol() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!listaAsignarPermisos.isEmpty()) {
            for (Permission permisoAsignar : listaAsignarPermisos) {
                PermissionRole nuevoitem = new PermissionRole();
                nuevoitem.setPermissionId(permisoAsignar);
                listaPermisosRol.add(nuevoitem);
                listaPermisosConUrl.remove(permisoAsignar);
            }

        } else {
            //mensaje de que no hay items seleccionados
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                    "No has selccionados ningún item"));
        }
    }

    public void removerPermisoRol(PermissionRole PermisoRol) {
        listaPermisosConUrl.add(PermisoRol.getPermissionId());
        listaPermisosRol.remove(PermisoRol);
    }

    //Falta remover todos y el row edit
    public void hacer() {
        System.out.println("hacer");
    }

    public void editarRol(Role rol) {
        this.rol = rol;
        validarBotonEditar = true;
    }

    public void editarRol() {
        RequestContext req = RequestContext.getCurrentInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (verificarNombreRol()) {
                roleFacadeLocal.edit(rol);
                ocultarModal(5);
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya exite un rol con el mismo nombre, verifica por favor"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al editar el rol, intenta más tarde"));
        }
    }

    public void asignarPermisosPorRolAbrirModal() {
        listaPermisosRol = permissionRoleFacadeLocal.listarPermisosPorRol2(rol);
        listarPermisosConDependencia();
        listaPermisosConUrl.removeAll(permissionRoleFacadeLocal.listarPermisosPorRol(rol));

        abrirModal(1);
    }

    public void editarPermisoRolCerrarModal() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!listaPermisosRol.isEmpty()) {
            if (editarPermisoRol()) {
                ocultarModal(5);
                ocultarModal(6);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "Permiso editado correctamente"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ocurrió un error al asignar los permisos, intenta más tarde"));
            }

        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                    "No puedes tener un rol sin permisos"));
        }
    }

    public boolean editarPermisoRol() {
        boolean banderaEdicion = true;
        try {
            List<PermissionRole> listaOriginal = permissionRoleFacadeLocal.listarPermisosPorRol2(rol);
            //En esta primera parte se compara la lista actual de la tabla llamada "listaPermisosRol" vs la lista original
            for (PermissionRole permisoAgregar : listaPermisosRol) {
                boolean bandera = false;
                for (PermissionRole itemOriginal : listaOriginal) {
                    if (Objects.equals(itemOriginal.getPermissionId().getPermissionId(), permisoAgregar.getPermissionId().getPermissionId())) {
                        bandera = true;
                    }
                }
                //si el elemto no se encuentra en la lista origina se agrega 
                if (!bandera) {
                    permisoAgregar.setRoleId(rol);
                    permissionRoleFacadeLocal.create(permisoAgregar);
                } else {
                    permissionRoleFacadeLocal.edit(permisoAgregar);

                }
            }

            //En la segunda parte comparamos las listas para remover algún permmiso que se encuentre en la lsita original pero no en la actual
            for (int i = 0; i < listaOriginal.size(); i++) {
                boolean bandera = true;
                for (PermissionRole permisoRemover : listaPermisosRol) {
                    if (Objects.equals(listaOriginal.get(i).getPermissionId().getPermissionId(), permisoRemover.getPermissionId().getPermissionId())) {
                        bandera = false;
                    }
                }
                if (bandera) {
                    permissionRoleFacadeLocal.remove(listaOriginal.get(i));
                }
            }
        } catch (Exception e) {
            banderaEdicion = false;
        }
        return banderaEdicion;
    }

    public boolean verificarSiEliminar(Role rol) {
        if (Objects.equals(rol.getRoleId(), retornarRolEnSesion().getRoleId())) {
            return false;
        } else {
            return true;
        }
    }

    public Role retornarRolEnSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        Role rolEnSesion = (Role) sesion.getAttribute("rol");
        return rolEnSesion;
    }

    public void eliminarRol(Role role) {
        this.rol = role;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            List<Role> roles = new ArrayList<>();
            roles.add(rol);
            List<User> listaUsuarios = userFacadeLocal.filtrarUsuariosPorRol(roles, new ArrayList<>());

            if (listaUsuarios.isEmpty()) {
                //remover Permisos Rol
                permissionRoleFacadeLocal.eliminarPermisosRol(new Permission(), rol);

                roleFacadeLocal.remove(rol);
                listarRoles();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        "Rol eliminado correctamente"));
            } else {
                abrirModal(2);
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al eliminar el rol"));
        }
    }

    public void asignarNuevoRolUsuarios() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (nuevoRolUsuario > 0 && nuevoRolUsuario != rol.getRoleId()) {
            try {
                permissionRoleFacadeLocal.eliminarPermisosRol(new Permission(), rol);
                if (asignarNuevoRolUsuarios(roleFacadeLocal.find(nuevoRolUsuario))) {
                    roleFacadeLocal.remove(rol);
                    ocultarModal(7);
                    listarRoles();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "Rol eliminado correctamente"));
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "No todos los usuarios se asignaron al nuevo rol, intentalo de nuevo"));
                }

            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                        "Ha ocurrido un error al eliminar el rol"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                    "Debes selecionar un rol"));
        }
    }

    public boolean asignarNuevoRolUsuarios(Role nuevoRol) {
        boolean bandera = true;
        try {
            List<User> lista = userFacadeLocal.filtrarUsuariosPorRol(rol);
            for (User user : lista) {
                user.setRoleId(nuevoRol);
                userFacadeLocal.edit(user);
            }
        } catch (Exception e) {
            bandera = false;
        }
        return bandera;
    }

    /*Inicio Módulo Rol*/
    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registrarGrupo').hide()");
                formulario = "formRegistrarGrupo:gridRegistrarGrupo";
                break;
            case 2:
                req.execute("PF('editarGrupo').hide()");
                formulario = ":formEditarGrupo:gridEditarGrupo";
                listarGrupos();
                break;
            case 3:
                //Aqui el registrar
                req.execute("PF('registrarPermiso').hide()");
                formulario = "formRegistrarPermiso:gridRegistrarPermiso";
                permiso = new Permission();
                listarPermisos();
                break;
            case 4:
                req.execute("PF('editarPermiso').hide()");
                formulario = ":formEditarPermiso:gridEditarPermiso";
                listarPermisos();
                break;
            case 5:
                req.execute("PF('registrarRol').hide()");
                formulario = ":formRegistrarRol:gridRegistrarRol";
                rol = new Role();
                validarBotonEditar = false;
                break;
            case 6:
                req.execute("PF('asignarPermisos').hide()");
                formulario = ":formAsignarPermisos:gridAsignarPermisos";
                listaPermisosRol = new ArrayList<>();
                listaAsignarPermisos = new ArrayList<>();
                break;
            case 7:
                req.execute("PF('confirmarAsignacionRol').hide()");
                break;
            default:
                break;
        }
        req.reset(formulario);
    }

    public void abrirModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();

        switch (opcion) {
            case 1:
                req.execute("PF('asignarPermisos').show()");
                break;
            case 2:
                req.execute("PF('confirmarAsignacionRol').show()");
                break;
            default:
                break;
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

    public boolean isValidacionDependencia() {
        return validacionDependencia;
    }

    public void setValidacionDependencia(boolean validacionDependencia) {
        this.validacionDependencia = validacionDependencia;
    }

    public List<Permission> getListaAsignarPermisos() {
        return listaAsignarPermisos;
    }

    public void setListaAsignarPermisos(List<Permission> listaAsignarPermisos) {
        this.listaAsignarPermisos = listaAsignarPermisos;
    }

    public List<Permission> getListaPermisosConUrl() {
        return listaPermisosConUrl;
    }

    public void setListaPermisosConUrl(List<Permission> listaPermisosConUrl) {
        this.listaPermisosConUrl = listaPermisosConUrl;
    }

    public boolean isValidarBotonEditar() {
        return validarBotonEditar;
    }

    public void setValidarBotonEditar(boolean validarBotonEditar) {
        this.validarBotonEditar = validarBotonEditar;
    }

    public int getNuevoRolUsuario() {
        return nuevoRolUsuario;
    }

    public void setNuevoRolUsuario(int nuevoRolUsuario) {
        this.nuevoRolUsuario = nuevoRolUsuario;
    }

    public List<Permission> getListaPermisosRegistrar() {
        return listaPermisosRegistrar;
    }

    public void setListaPermisosRegistrar(List<Permission> listaPermisosRegistrar) {
        this.listaPermisosRegistrar = listaPermisosRegistrar;
    }

}
