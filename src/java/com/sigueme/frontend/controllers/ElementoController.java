/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Element;
import com.sigueme.backend.entities.ElementType;
import com.sigueme.backend.model.ElementFacadeLocal;
import com.sigueme.backend.model.ElementTypeFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author ZamudioL
 */
@Named(value = "elementoController")
@ViewScoped
public class ElementoController implements Serializable {

    @EJB
    private ElementFacadeLocal elementFacadeLocal;
    @EJB
    private ElementTypeFacadeLocal elementTypeFacadeLocal;

    private Element elemento;

    private List<Element> elementosLista;
    private List<ElementType> tipoElementosLista;

    public ElementoController() {
    }

    @PostConstruct
    public void init() {
        elemento = new Element();
        listarElementos();
        listarTiposElemento();
    }

    public List<Element> getElementosLista() {
        return elementosLista;
    }

    public void setElementosLista(List<Element> elementosLista) {
        this.elementosLista = elementosLista;
    }

    public Element getElemento() {
        return elemento;
    }

    public void setElemento(Element elemento) {
        this.elemento = elemento;
    }

    public List<ElementType> getTipoElementosLista() {
        return tipoElementosLista;
    }

    public void setTipoElementosLista(List<ElementType> tipoElementosLista) {
        this.tipoElementosLista = tipoElementosLista;
    }

    public void listarElementos() {
        elementosLista = new ArrayList<>();
        elementosLista = elementFacadeLocal.findAll();
    }

    public void listarTiposElemento() {
        tipoElementosLista = new ArrayList<>();
        tipoElementosLista = elementTypeFacadeLocal.findAll();
    }

    public void registrarElemento() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            elementFacadeLocal.create(elemento);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                    "El elemento se ha registrado correctamente"));
            //cerraModal

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al registrar el elemento, inténtalo más tarde"));
        }
    }

    public void editarElemento(Element element) {
        this.elemento = element;
    }

    public void editarElemento() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            elementFacadeLocal.edit(elemento);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                    "El elemento se ha modificado correctamente"));
            //cerraModal

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al modificar el elemento, inténtalo más tarde"));
        }

    }

    public void eliminarElemento(Element element) {
        this.elemento = element;
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            elementFacadeLocal.remove(elemento);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                    "El elemento se ha eliminado correctamente"));
            listarElementos();

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al modificar el elemento, inténtalo más tarde"));
        }

    }

    public void asignarMarcaSegunTipoElemento(AjaxBehaviorEvent event) {
        Object o = event.getComponent().getAttributes().get("value");
        if (o instanceof ElementType) {
            int itemId = ((ElementType) o).getElementTypeId();
            if (itemId == 1 || itemId == 2) {
                this.elemento.setBrand("Dell");
            } else if (itemId == 3) {
                this.elemento.setBrand("Avaya");
            }
        }
    }
}
