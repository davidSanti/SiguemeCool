/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.UserFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author ZamudioL
 */
@FacesConverter (value = "usuarioConverter")
@ViewScoped
public class UsuarioConverter implements Converter {
     @EJB
    private UserFacadeLocal usuarioFacadeLocal;

    public UsuarioConverter() {
    }

     @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<User> usuarios = this.usuarioFacadeLocal.findAll();
        for (User objeto : usuarios) {
            if (objeto.getUserId()== Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

     @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((User) objeto).getUserId().toString();
        } else {
            return "";
        }
    }
    
}
