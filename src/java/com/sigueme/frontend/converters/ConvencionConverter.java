/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Convention;
import com.sigueme.backend.model.ConventionFacadeLocal;
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
@FacesConverter (value = "convencionConverter")
@ViewScoped
public class ConvencionConverter implements Converter{
    @EJB
    private ConventionFacadeLocal conventionFacadeLocal;

    public ConvencionConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Convention> convenciones = this.conventionFacadeLocal.findAll();
        for (Convention objeto : convenciones) {
            if (objeto.getConventionId() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Convention) objeto).getConventionId().toString();
        } else {
            return "";
        }
    }
}
