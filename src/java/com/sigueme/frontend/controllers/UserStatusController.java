/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.UserStatus;
import com.sigueme.backend.model.UserStatusFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ZamudioL
 */
@Named(value = "userStatusController")
@ViewScoped
public class UserStatusController implements Serializable {

    @EJB
    private UserStatusFacadeLocal userStatusFacadeLocal;

    private UserStatus estadousuario;
    private List<UserStatus> listaEstadoUsuario;

    @PostConstruct
    public void init() {
        estadousuario = new UserStatus();
        listaEstadoUsuario = new ArrayList<>();
        listarEstadoUsuario();

    }

    public UserStatusController() {
    }

    public UserStatus getEstadousuario() {
        return estadousuario;
    }

    public void setEstadousuario(UserStatus estadousuario) {
        this.estadousuario = estadousuario;
    }

    public List<UserStatus> getListaEstadoUsuario() {
        return listaEstadoUsuario;
    }

    public void setListaEstadoUsuario(List<UserStatus> listaEstadoUsuario) {
        this.listaEstadoUsuario = listaEstadoUsuario;
    }

    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registroEstadousuario').hide()");
                formulario = ":formEstadousuario:gridEstadousuario";
                init();
                break;

            case 2:
                req.execute("PF('editarEstadousuario').hide()");
                formulario = ":formEditarEstadousuario:gridEditarEstadousuario";
                init();
                break;
            case 3:
                req.execute("PF('modalEstadoUsuario').hide()");
                init();
                break;

            default:
                break;
        }
        req.reset(formulario);
    }

    public void registrarEstadousuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            userStatusFacadeLocal.create(estadousuario);
            ocultarModal(1);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "la convención se ha registrado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al registrar la convención, intentalo de nuevo"));
        }
    }

    public void listarEstadoUsuario() {
        listaEstadoUsuario = new ArrayList<>();
        listaEstadoUsuario = userStatusFacadeLocal.findAll();
    }

    public void editarYCerrar() {
        if (editarEstadoUsuario()) {
            ocultarModal(2);
        }
    }

    public void editarEstadosUsuario(UserStatus estadoUsuario) {
        this.estadousuario = estadoUsuario;
    }

    public boolean editarEstadoUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean banderita = false;
        try {
            userStatusFacadeLocal.edit(estadousuario);
            banderita = true;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La convención se ha modificado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al modificar la convención, intentalo de nuevo"));
        }
        return banderita;
    }

    public void eliminarEstadoUsuario(UserStatus estadoUsuario) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.estadousuario = estadoUsuario;
        try {
            userStatusFacadeLocal.remove(estadoUsuario);
            listarEstadoUsuario();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La convención se ha eliminado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al eliminar la convención, intentalo de nuevo"));
        }

    }
}
