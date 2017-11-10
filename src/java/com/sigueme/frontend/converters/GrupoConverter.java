/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.model.GroupClsFacadeLocal;
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
@FacesConverter (value = "grupoConverter")
@ViewScoped
public class GrupoConverter implements Converter  {
     @EJB
    private GroupClsFacadeLocal groupFacadeLocal;

    public GrupoConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<GroupCls> grupos = this.groupFacadeLocal.findAll();
        for (GroupCls objeto : grupos) {
            if (objeto.getGroupId() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((GroupCls) objeto).getGroupId().toString();
        } else {
            return "";
        }
    }
    
}
