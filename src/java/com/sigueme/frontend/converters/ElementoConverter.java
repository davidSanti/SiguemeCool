/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Element;
import com.sigueme.backend.model.ElementFacadeLocal;
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
@FacesConverter(value = "elementoConverter")
@ViewScoped
public class ElementoConverter implements Converter {
    @EJB
    private ElementFacadeLocal elementFacadeLocal;

    public ElementoConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Element> elementos = this.elementFacadeLocal.findAll();
        for (Element objeto : elementos) {
            if (objeto.getElementId() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Element) objeto).getElementId().toString();
        } else {
            return "";
        }
    }
}
