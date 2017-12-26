/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Element;
import com.sigueme.backend.model.ElementFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

    private Element elemento;

    private List<Element> elementosLista;

    public ElementoController() {
    }

    @PostConstruct
    public void init() {
        elemento = new Element();
       listarElemetos();

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
    
    public void listarElemetos(){
        elementosLista = new ArrayList<>();
        elementosLista=elementFacadeLocal.findAll();
    }

}
