/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.ElementType;
import com.sigueme.backend.model.ElementTypeFacadeLocal;
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
@Named(value = "tiposElementosController")
@ViewScoped
public class TiposElementosController implements Serializable {
     
    @EJB
    private ElementTypeFacadeLocal tipoElementoFacadeLocal;
    
    private ElementType tipoElemento;
    private List<ElementType> listaTiposdeElementos;
    
    @PostConstruct
    public void init(){
        tipoElemento= new ElementType();
        listaTiposdeElementos = new ArrayList<>();
    }
    
    public TiposElementosController() {
    }

    public ElementType getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(ElementType tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public List<ElementType> getListaTiposdeElementos() {
        return listaTiposdeElementos;
    }

    public void setListaTiposdeElementos(List<ElementType> listaTiposdeElementos) {
        this.listaTiposdeElementos = listaTiposdeElementos;
    }
    
    
}
