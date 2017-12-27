/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.ElementType;
import com.sigueme.backend.model.ElementTypeFacadeLocal;
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
@FacesConverter(value = "tipoElementoConverter")
@ViewScoped
public class TipoElementoConverter implements Converter {

    @EJB
    private ElementTypeFacadeLocal elementTypeFacadeLocal;

    public TipoElementoConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<ElementType> tipos = this.elementTypeFacadeLocal.findAll();
        for (ElementType objeto : tipos) {
            if (objeto.getElementTypeId() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((ElementType) objeto).getElementTypeId().toString();
        } else {
            return "";
        }
    }
}
