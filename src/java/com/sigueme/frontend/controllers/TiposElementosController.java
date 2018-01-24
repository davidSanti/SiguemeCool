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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

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
    public void init() {
        tipoElemento = new ElementType();
        listaTiposdeElementos = new ArrayList<>();
        listarTiposdeElementos();
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

    public void listarTiposdeElementos() {
        listaTiposdeElementos = tipoElementoFacadeLocal.findAll();
    }

    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registroTipoElemento').hide()");
                formulario = ":formTipoElemento:gridTipoElemento";
                init();
                break;
            case 2:
                req.execute("PF('editarTipoElemento').hide()");
                formulario = ":formTipoDeElemento:gridTipoDeElemento";
//                init();
                break;
            case 3:
                req.execute("PF('tablaTipoElementos').hide()");
                break;
            default:
                break;
        }
        req.reset(formulario);
    }

    public void registrarTipoElemento() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            tipoElementoFacadeLocal.create(tipoElemento);
            ocultarModal(1);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "la tipo elemento se ha registrado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al registrar la tipo elemento, intentalo de nuevo"));
        }
    }

    public void editarYCerrar() {
        if (editartipoDeElemento()) {
            ocultarModal(2);
        }
    }

    public void editarTipoElemento(ElementType tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public boolean editartipoDeElemento() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean banderita = false;
        try {
            tipoElementoFacadeLocal.edit(tipoElemento);
            banderita = true;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El tipo elemento se ha modificado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al modificar la tipo elemento, intentalo de nuevo"));
        }
        return banderita;
    }
}
