/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Convention;
import com.sigueme.backend.entities.Desk;
import com.sigueme.backend.model.ConventionFacadeLocal;
import com.sigueme.backend.model.DeskFacadeLocal;
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
@Named(value = "convencionesController")
@ViewScoped
public class ConvencionesController implements Serializable {

    @EJB
    private ConventionFacadeLocal convencionFacadeLocal;
    @EJB
    private DeskFacadeLocal deskFacadeLocal;

    private Convention convencion;
    private List<Convention> listaConvencion;
    private List<Desk> listaEscritorios;

    @PostConstruct
    public void init() {
        convencion = new Convention();
        listaConvencion = new ArrayList<>();
        listarConvenciones();
    }

    public ConvencionesController() {
    }

    public List<Convention> getListaConvenciones() {
        return listaConvencion;
    }

    public void setListaConvenciones(List<Convention> listaConvenciones) {
        this.listaConvencion = listaConvenciones;
    }

    public Convention getConvencion() {
        return convencion;
    }

    public void setConvencion(Convention convencion) {
        this.convencion = convencion;
    }

    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registroConvencion').hide()");
                formulario = ":formRegistrarConvencion:gridRegistrarConvencion";
                init();
                break;
            case 2:
                req.execute("PF('editarConvencion').hide()");
                formulario = ":formEditarConvencion:gridEditarConvencion";
                init();
                break;
            case 3:
                req.execute("PF('modalTablaConvencion').hide()");
                init();
                break;

            default:
                break;
        }
        req.reset(formulario);
    }

    public void registrarConvesiones() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            convencionFacadeLocal.create(convencion);
            ocultarModal(1);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "la convención se ha registrado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al registrar la convención, intentalo de nuevo"));
        }
    }

    public void editarYCerrar() {
        if (editarConvencion()) {
            ocultarModal(2);
        }
    }

    public void editarConvencion(Convention convencion) {
        this.convencion = convencion;
    }

    public boolean editarConvencion() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean banderita = false;
        try {
            convencionFacadeLocal.edit(this.convencion);
            banderita = true;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La convención se ha modificado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al modificar la convención, intentalo de nuevo"));
        }
        return banderita;
    }

    public boolean validarBotonEliminar(Convention convencion) {
        //Se realiza esto para impedir que se elimine la convención del MSC que es la número 1
        boolean bandera = convencion.getConventionId() == 1;
        return bandera;
    }

    public void eliminarConvencion(Convention convencion) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.convencion = convencion;
        try {
            listaEscritorios = convencionFacadeLocal.listarPuestosPorCovenciones(convencion);

            if (!listaEscritorios.isEmpty()) {
                asignarPuestosAlMsc();
            }
            convencionFacadeLocal.remove(this.convencion);

            listarConvenciones();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La convención se ha eliminado correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al eliminar la convención, intentalo de nuevo"));
        }

    }

    public void asignarPuestosAlMsc() {
        if (!listaEscritorios.isEmpty()) {
            try {
                Convention convencionMsc = convencionFacadeLocal.find(1);
                for (Desk item : listaEscritorios) {
                    item.setConventionId(convencionMsc);
                    deskFacadeLocal.edit(item);
                }

            } catch (Exception e) {
            }

        }
    }

    public void listarConvenciones() {
        listaConvencion = new ArrayList<>();
        listaConvencion = convencionFacadeLocal.findAll();
    }

}
