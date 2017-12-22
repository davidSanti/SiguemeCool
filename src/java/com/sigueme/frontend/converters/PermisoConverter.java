/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Permission;
import com.sigueme.backend.model.PermissionFacadeLocal;
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
@FacesConverter(value = "permisoConverter")
@ViewScoped
public class PermisoConverter implements Converter{
    @EJB
    private PermissionFacadeLocal permissionFacadeLocal;

    public PermisoConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Permission> estados = this.permissionFacadeLocal.findAll();
        for (Permission objeto : estados) {
            if (objeto.getPermissionId()== Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Permission) objeto).getPermissionId().toString();
        } else {
            return "";
        }
    }
}
