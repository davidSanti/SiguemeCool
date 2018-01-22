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

//    public void ocultarModal(int opcion) {
//        RequestContext req = RequestContext.getCurrentInstance();
//        String formulario = null;
//        switch (opcion) {
//            case 1:
//                req.execute("PF('registrarTipoElemento').hide()");
//                formulario = "formTiposdeElemento:gridRegistrar";
//                init();
//                break;
//
//            default:
//                break;
//        }
//        req.reset(formulario);
//    }

//    public void registrarCurso() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        if () {
//            try {
//                this.tipoElemento.setElementTypeId(Integer.MIN_VALUE);
//                this.abrirModal(1);
//
//            } catch (Exception ex) {
//                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "No se pudo registrar"));
//            }
//        } else {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Verifique la fechas por favor"));
//
//        }
//    }

//    public void abrirModal(int opciones) {
//        RequestContext request = RequestContext.getCurrentInstance();
//        switch (opciones) {
//            case 1:
//                request.execute("PF('registrarTipoElemento').show()");
//                break;
//            default:
//                break;
//        }
//    }

}
