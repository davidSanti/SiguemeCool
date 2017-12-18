/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Element;
import com.sigueme.backend.entities.UserStatus;
import com.sigueme.backend.model.UserStatusFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author RobayoDa
 */
@FacesConverter(value = "estadoUsuarioConverter")
@ViewScoped
public class EstadoUsuarioConverter implements Converter{
    @EJB
    private UserStatusFacadeLocal userStatusFacadeLocal;

    public EstadoUsuarioConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<UserStatus> estados = this.userStatusFacadeLocal.findAll();
        for (UserStatus objeto : estados) {
            if (objeto.getUserStatusId() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((UserStatus) objeto).getUserStatusId().toString();
        } else {
            return "";
        }
    }
}
