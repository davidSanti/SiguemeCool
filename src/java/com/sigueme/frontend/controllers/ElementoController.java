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
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

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
    private boolean banderaNombrePc;

    public ElementoController() {
    }

    @PostConstruct
    public void init() {
        elemento = new Element();
        listarElementos();
        listarTiposElemento();
        banderaNombrePc = false;
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

    public boolean isBanderaNombrePc() {
        return banderaNombrePc;
    }

    public void setBanderaNombrePc(boolean banderaNombrePc) {
        this.banderaNombrePc = banderaNombrePc;
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
            //En la primer validación se verifica el serial code
            if (verificarIdentificacionElemento(1)) {
                //En la segunda validación se verifica la placa inventario
                if (verificarIdentificacionElemento(2)) {
                    elementFacadeLocal.create(elemento);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "El elemento se ha registrado correctamente"));
                    ocultarModal(1);
                    listarElementos();

                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                            "Ya se encuentra registrado un elemento con el mismo placa inventario"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra registrado un elemento con el mismo serial code"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    "Ha ocurrido un error al registrar el elemento, inténtalo más tarde"));
        }
    }

    public void editarElemento(Element element) {
        this.elemento = element;
        verificarTipoElemento();
    }

    public void editarElemento() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //En la primer validación se verifica el serial code
            if (verificarIdentificacionElemento(1)) {
                //En la segunda validación se verifica la placa inventario
                if (verificarIdentificacionElemento(2)) {
                    elementFacadeLocal.edit(elemento);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "El elemento se ha modificado correctamente"));
                    ocultarModal(3);
                    listarElementos();

                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                            "Ya se encuentra registrado un elemento con el mismo placa inventario"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "Ya se encuentra registrado un elemento con el mismo serial code"));
            }
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
                banderaNombrePc = true;
            } else if (itemId == 3) {
                this.elemento.setBrand("Avaya");
                banderaNombrePc = false;
            }
        }
        verificarTipoElemento();
    }

    public boolean verificarIdentificacionElemento(int opcion) {
        List<Element> lista = new ArrayList<>();
        boolean bandera = false;
        if (opcion == 1) {
            lista = elementFacadeLocal.verificarIdentification(elemento.getSerialCode(), 1);
        } else if (opcion == 2) {
            lista = elementFacadeLocal.verificarIdentification(elemento.getInventoryPlaque(), 2);
        }
        if (!lista.isEmpty()) {
            for (Element element : lista) {
                if (Objects.equals(element.getElementId(), this.elemento.getElementId())) {
                    bandera = true;
                    break;
                }
            }
        } else {
            bandera = true;
        }
        return bandera;
    }

    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registrarElementos').hide()");
                formulario = "formRegistrar:gridRegistrar";
                elemento = new Element();
                banderaNombrePc = false;
                break;
            case 2:
                req.execute("PF('modalVerElemento').hide()");
                formulario = "formVerElemento:gridVerElemento";
                elemento = new Element();
                break;
            case 3:
                req.execute("PF('editarElementos').hide()");
                formulario = "formEditar:gridEditar";
                elemento = new Element();
                banderaNombrePc = false;
                break;
            default:
                break;
        }
        req.reset(formulario);
    }

    public boolean verificarTipoElemento() {
        if (elemento.getElementId() != null) {
            int tipoElementoId = elemento.getTypeId().getElementTypeId();
            boolean bandera = (tipoElementoId == 1 || tipoElementoId == 2);
            if (bandera) {
                banderaNombrePc = true;
            }
            return bandera;
        } else {
            return false;
        }
    }
}
