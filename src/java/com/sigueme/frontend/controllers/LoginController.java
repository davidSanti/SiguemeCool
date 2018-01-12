/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.UserFacadeLocal;
import com.sigueme.backend.model.UserStatusFacadeLocal;
import com.sigueme.backend.utilities.Crypto;
import com.sigueme.backend.utilities.Mail;
import com.sigueme.backend.utilities.PasswordGenerator;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ViewScoped
public class LoginController implements Serializable {

    @EJB
    private UserFacadeLocal userFacadeLocal;
    @EJB
    private UserStatusFacadeLocal userStatusFacadeLocal;
    private User user;

    private String identificacionRecuperarClave;
    private String correoRecuperarClave;

    @PostConstruct
    public void init() {
        user = new User();
        identificacionRecuperarClave = "";
        correoRecuperarClave = "";
    }

    public LoginController() {
    }

    //éste método se encarga autenticar un usuario dependiendo del número de cédula y la contraseña proporcionada en la interfaz
    //Si encuentra un registro que concuerde incializa unas variaboles de sesion y lo redirige al home del sistema
    public String autenticarUsuario() {
        String redireccion = null;
        User usuarioValidado = null;
        try {
            String claveEncriptada = Crypto.Encriptar(this.user.getUserPassword());
            this.user.setUserPassword(claveEncriptada);
            usuarioValidado = userFacadeLocal.iniciarSesion(this.user);
            if (usuarioValidado != null) {
                HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                sesion.setAttribute("usuario", usuarioValidado);
                sesion.setAttribute("rol", usuarioValidado.getRoleId());
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

    public void recuperarClave() {
        try {
            System.out.println("correoRecuperarClave" + correoRecuperarClave);
            User usuario = userFacadeLocal.verificarCorreoEIdentificacion(identificacionRecuperarClave, correoRecuperarClave);
            if (usuario != null) {
                String claveGenerada = PasswordGenerator.getPassword();
                usuario.setUserPassword(Crypto.Encriptar(claveGenerada));
                //el estado es para qeu le pida cambio de contraseña cuando entre al sistema
                usuario.setUserStatusId(userStatusFacadeLocal.find(4));
                userFacadeLocal.edit(usuario);
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Contraseña restaurada correctamente"));

                System.out.println("" + claveGenerada);
                //enviar correo
//                enviarCorreo(usuario, claveGenerada);
                ocultarModal(1);
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: ", "Los datos del usuario no son correcos, intente nuevamente"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error: ", "Ha ocurrido un error al intentar recupear tu clave"));
            System.out.println("Error:: " + e.getMessage());
        }
    }

    public void enviarCorreo(User usuario, String nuevaClave) {
        List<User> lista = new ArrayList<>();
        lista.add(usuario);
        try {
            Mail.send(lista, "Cambio de Contraseña", "Tu contraseña se ha restaurado correctamente,"
                    + "\nIntenta ingresar al sistema con la siguiente clave: " + nuevaClave);
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Correo enviado exitosamente"));

        } catch (UnsupportedEncodingException ex) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error: ", "El correo no se ha podido enviar "));
        }
    }

    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('recuperarClave').hide()");
                formulario = "formRecuperarClave:gridRecuperarClave";
                init();
                break;
            default:
                break;
        }
        req.update(formulario);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIdentificacionRecuperarClave() {
        return identificacionRecuperarClave;
    }

    public void setIdentificacionRecuperarClave(String identificacionRecuperarClave) {
        this.identificacionRecuperarClave = identificacionRecuperarClave;
    }

    public String getCorreoRecuperarClave() {
        return correoRecuperarClave;
    }

    public void setCorreoRecuperarClave(String correoRecuperarClave) {
        this.correoRecuperarClave = correoRecuperarClave;
    }

}
