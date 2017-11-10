/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.UserFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author RobayoDa
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private UserFacadeLocal userFacadeLocal;
    private User user;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public LoginController() {
    }

    //éste método se encarga autenticar un usuario dependiendo del número de cédula y la contraseña proporcionada en la interfaz
    //Si encuentra un registro que concuerde incializa unas variaboles de sesion y lo redirige al home del sistema
    public String autenticarUsuario() {
        String redireccion = null;
        User usuarioValidado = null;
        try {
            //String pass = Crypto.Encriptar(this.usuario.getClave());
            //this.usuario.setClave(pass);
            usuarioValidado = userFacadeLocal.iniciarSesion(this.user);
            if (usuarioValidado != null) {
                HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                sesion.setAttribute("usuario", usuarioValidado);
                sesion.setAttribute("rol", usuarioValidado.getRoleId());
                //redireccion = "pages/sigueme/sigueme?faces-redirect=true";
                redireccion = "pages/system/home.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        "growlLogin", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "datos incorrectos"));
                RequestContext req = RequestContext.getCurrentInstance();
                req.reset("login");
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    "growlLogin", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error: ", "Error al iniciar sesión"));
            System.out.println("Error:: " + e.getMessage());
        }
        return redireccion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
