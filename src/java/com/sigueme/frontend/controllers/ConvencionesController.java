/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Convention;
import com.sigueme.backend.model.ConventionFacadeLocal;
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
@Named(value = "convencionesController")
@ViewScoped
public class ConvencionesController implements Serializable {
     
    @EJB
    private ConventionFacadeLocal convencionFacadeLocal;
    
    private Convention convencion;
    private List<Convention> listaConvenciones;
    
    @PostConstruct
    public void init(){
        convencion= new Convention();
        listaConvenciones= new ArrayList<>();
    }
    
    public ConvencionesController() {
    }

    public List<Convention> getListaConvenciones() {
        return listaConvenciones;
    }

    public void setListaConvenciones(List<Convention> listaConvenciones) {
        this.listaConvenciones = listaConvenciones;
    }

    
    public Convention getConvencion() {
        return convencion;
    }

    public void setConvencion(Convention convencion) {
        this.convencion = convencion;
    }
    
    
    
}
