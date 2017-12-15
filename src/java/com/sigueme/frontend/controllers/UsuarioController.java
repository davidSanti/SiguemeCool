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
import com.sigueme.backend.model.UserByCourseFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import com.sigueme.backend.model.UserStatusFacadeLocal;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    @EJB
    private UserByCourseFacadeLocal userByCourseFacadeLocal;
    @EJB
    private UserStatusFacadeLocal userStatusFacadeLocal;

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
        listaUsuarios = userFacadeLocal.listarUsuarios();
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

    public void limpiarFiltro() {
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
                req.execute("PF('registrarUsuario').hide()");
                formulario = "formRegistrarUsuario:gridRegistrarUsuario";
                break;
            case 3:
                req.execute("PF('modificarCuenta').hide()");
                formulario = "formModificarCuenta:gridModificarCuenta";
                break;
            default:
                break;
        }
        init();
        req.reset(formulario);
    }

    public void registrarUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (verificarIdentificacion()) {
                if (verificarRol()) {
                    //Aqui se deberá invocar al método qe genera un password aleatorio y enviará la clave solo al email espeficado del usuario nuevo
                    usuario.setUserPassword(usuario.getIdentification());
                    userFacadeLocal.create(usuario);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuario registrado correctamente"));
                    ocultarModal(2);
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", " Ya existe un usuario con Rol de Site Manager no puedes registrar otro"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Verifica la cédula o el people Soft por que ya existe un usuario con esa identificación"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "El usuario no se pudo registrar"));
        }
    }

    public boolean verificarIdentificacion() {
        List<User> lista = new ArrayList<>();
        lista = userFacadeLocal.buscarPorIdentificacion(usuario.getIdentification());
        boolean validarCedula = lista.isEmpty();

        lista = userFacadeLocal.buscarPorIdentificacion(usuario.getPeopleSoft());
        boolean validarPeopleSoft = lista.isEmpty();

        boolean bandera = validarCedula && validarPeopleSoft;
        return bandera;
    }

    public boolean verificarIdentificacion(String identificacion) {
        List<User> lista = new ArrayList<>();
        lista = userFacadeLocal.buscarPorIdentificacion(identificacion);
        boolean validarIdentificacion = lista.size() <= 1;
        return validarIdentificacion;
    }

    //Solo debería existe un Site Manager, por lo cosniguiente no se debe registrar otro usuario con el mismo rol
    public boolean verificarRol() {
        List<User> lista = userFacadeLocal.listarUsuariosSiteManager();
        boolean bandera = false;
        System.out.println("lis" + lista.size());
        if (lista.isEmpty()) {
            bandera = true;
        } else {
            for (User item : lista) {
                if (Objects.equals(item.getUserId(), this.usuario.getUserId())) {
                    bandera = true;
                    break;
                }
            }
        }
        return bandera;
    }

    public void editarUsuario(User user) {
        this.usuario = user;
    }

    public void editarUsuario() {
        //Pregntar si se pueden registrar más de un OM
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validacion = false;
        try {
            boolean validarRol = true;
            if (usuario.getRoleId().getRoleId() == 1) {
                validarRol = verificarRol();
            }
            if (validarRol) {
                //Validar Cedula
                if (verificarIdentificacion(usuario.getIdentification())) {
                    //Validar People Soft
                    if (verificarIdentificacion(usuario.getPeopleSoft())) {
                        validacion = true;
                        userFacadeLocal.edit(usuario);
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Los datos del usuarios han sido actualizados correctamente "));
                        ocultarModal(1);
                    }
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", " Ya existe un usuario con Rol de Site Manager no puede existir otro"));
            }

            if (!validacion) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Verifica la cédula o el people Soft por que ya existe un usuario con esa identificación"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Los datos no se actualizaron, inténtalo más tarde"));
        }
    }

    public void eliminarUsuario(User user) {
        this.usuario = user;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (finalizarTodosLosProcesos()) {
                user.setUserStatusId(userStatusFacadeLocal.find(2));
                userFacadeLocal.edit(user);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Los datos del usuarios han sido eliminados correctamente "));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Los datos  del usuario no se eliminaron"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Los datos no se eliminaron, inténtalo más tarde"));
        }
    }

    public boolean finalizarTodosLosProcesos() {
        //Aqui se referenciarían los métodos que elimian los procesos del usuario como los datos de la adherencia, aht, taxis, entre otros.
        return eliminarCursosDelUsuario();
    }

    public boolean eliminarCursosDelUsuario() {
        boolean bandera = userByCourseFacadeLocal.eliminarCursosDelUsuario(this.usuario);
        return bandera;
    }

    public List<GroupCls> listarGrupos() {
        return groupFacadeLocal.findAll();
    }

    public List<Role> listarRoles() {
        return roleFacadeLocal.findAll();
    }

    public User retornarUsuarioEnSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();

        User usuarioEnSesion = (User) sesion.getAttribute("usuario");
        return usuarioEnSesion;
    }

    public void asignarUsuario() {
        this.usuario = retornarUsuarioEnSesion();
    }

    public void modificarUsuario() {
        //Pregntar si se pueden registrar más de un OM
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validacion = false;
        try {
            //Validar Cedula
            if (verificarIdentificacion(usuario.getIdentification())) {
                //Validar People Soft
                if (verificarIdentificacion(usuario.getPeopleSoft())) {
                    validacion = true;
                    userFacadeLocal.edit(usuario);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Tus datos se modificaron correctamente "));
                    ocultarModal(3);
                }
            }

            if (!validacion) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Verifica la cédula o el people Soft por que ya existe un usuario con esa identificación"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Los datos no se actualizaron, inténtalo más tarde"));
        }
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
