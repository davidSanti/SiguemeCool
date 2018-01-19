/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Convention;
import com.sigueme.backend.entities.Desk;
import com.sigueme.backend.entities.Element;
import com.sigueme.backend.entities.ElementType;
import com.sigueme.backend.model.ConventionFacadeLocal;
import com.sigueme.backend.model.DeskFacadeLocal;
import com.sigueme.backend.model.ElementFacadeLocal;
import com.sigueme.backend.model.ElementTypeFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author RobayoDa
 */
@Named(value = "inventarioController")
@ViewScoped
public class InventarioController implements Serializable {

    @EJB
    private ConventionFacadeLocal conventionFacadeLocal;
    @EJB
    private DeskFacadeLocal deskFacadeLocal;
    @EJB
    private ElementFacadeLocal elementFacadeLocal;
    @EJB
    private ElementTypeFacadeLocal elementTypeFacadeLocal;

    private Desk puesto;
    private Element elemento;
    private ElementType tipoElemento;

    private List<Convention> convenciones;
    private List<Desk> puestos;
    private List<Element> elementosPorPuestos;

    private List<Element> elementosPorPuestosOrigen;
    private List<Element> elementosPorPuestosDestino;
    private List<Element> elementosMsc;
    private List<ElementType> tipoElementos;

    private String serialCodeOrigen;
    private String serialCodeDestino;

    private Desk puestoOrigen;
    private Desk puestoDestino;
    private String serialCodeFilter;

    public InventarioController() {
    }

    @PostConstruct
    public void init() {
        puesto = new Desk();
        elemento = new Element();

        convenciones = new ArrayList<>();
        elementosPorPuestos = new ArrayList<>();
        elementosPorPuestosOrigen = new ArrayList<>();
        elementosPorPuestosDestino = new ArrayList<>();

        puestos = new ArrayList<>();
        listarPuestos();
        tipoElementos = new ArrayList<>();

        serialCodeOrigen = "";
        serialCodeDestino = "";
        puestoOrigen = new Desk();
        puestoDestino = new Desk();

        serialCodeFilter = "";
        listarElementosMSC();
    }

    public void listarPuestos() {
        puestos = deskFacadeLocal.findAll();
    }

    //Este método se ejecuta cuando el selectChecboxMenu se cierra en la interfaz de course.xhtml, con la finalidad de que al cambiar los items de la lista
    //se actualice el listado con los puestos asociados a dicho proyecto  
    public void filtrarPuesto() {
        serialCodeFilter = serialCodeFilter.toUpperCase();
        if (convenciones.size() > 0) {
            puestos = deskFacadeLocal.listarPuestosPorSerialCodeYCovenciones(serialCodeFilter, convenciones);

        } else if (!serialCodeFilter.equals("")) {
            puestos = deskFacadeLocal.listarPuestosPorSerialCodeYCovenciones(serialCodeFilter, listarCovenciones());
        } else {
            listarPuestos();
        }
    }

    //Con este método pretendemos validar que el registro se realice de manera exitosa para porsteriormente cerrar el modal de registro
    public void registrarPuestoCerrarModal() {
        if (this.registrarPuesto()) {
            cerrarModal(1);
            puesto = new Desk();
        }
    }

    //Este método tiene que validar varias cosas antes de registrar el puesto.
    //verifica los grupos asociados a un puesto,verifica que el serial code no esté repetido
    //si todo sale correctamnete realiza el registro y retorna "true" para poder cerrar el modal
    public boolean registrarPuesto() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean banderaRegistro = false;
        puesto.setSerialCode(retornarSerialCode(puesto.getSerialCode()));

        String serialCode = puesto.getSerialCode();
        Desk puestoSerial = deskFacadeLocal.buscarPorSerialCode(serialCode);
        boolean bandera = puestoSerial == null;
        if (bandera) {
            try {
//                this.deskFacadeLocal.create(puesto);
                banderaRegistro = true;
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Registro exitoso"));
            } catch (Exception e) {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ha ocurrido un error al registrar el puesto"));

            }
        } else {
            // Ya existe un puesto con ese serial code
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya existe un puesto con ese serial code"));
        }
        return banderaRegistro;
    }

//éste método asigna el puesto que le enviamos desde la interfaz a la variable global que intanciamos arriba "puesto" 
//y asgina la lsita de elemtos asociados a ese puesto.
    public void editarPuesto(Desk desk, boolean opcion) {
        this.puesto = desk;
        if (opcion) {
            listarElementosPorPuesto();
        }
    }

    public void listarElementosPorPuesto() {
        elementosPorPuestos = new ArrayList<>();
        elementosPorPuestos = elementFacadeLocal.listarElementosPorPuesto(puesto);
    }

    //Este método valida que la edición se haya efectuado correctamente y cierra el modal
    public void editarCerrarModal() {
        if (this.editarPuesto()) {
            cerrarModal(5);
            puesto = new Desk();
        }
    }

    //Este método verifica los grupos asociados a un puesto, verifica su serial code y returna "true" si todo salió correctamente
    public boolean editarPuesto() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean banderaEditar = false;
        puesto.setSerialCode(retornarSerialCode(puesto.getSerialCode()));

        String serialCode = puesto.getSerialCode();
        Desk puestoSerial = deskFacadeLocal.buscarPorSerialCode(serialCode);
        boolean bandera = Objects.equals(puestoSerial.getDeskId(), puesto.getDeskId());
        if (bandera) {
            try {
                this.deskFacadeLocal.edit(puesto);
                banderaEditar = true;
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El registro se ha modificado correctamente"));
            } catch (Exception e) {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ha ocurrido un error al modificar el puesto"));
            }

        } else {
            // Ya existe un puesto con ese serial code
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya existe un puesto con ese serial code"));
        }
        return banderaEditar;
    }

    //ése método recibe un serialCode y lo transforma siguiendo un formato así (P1 - 102) dividiendo entre la parte literal y la parte númerica
    public String retornarSerialCode(String serial) {
        serial = serial.replaceAll(" ", "");
        String partes[] = serial.split("-");
        String serialCodeCorregido = "";
        if (partes.length == 2) {
            String parteLiteral = partes[0];
            String parteNumerica = partes[1];

            if (parteNumerica.length() <= 2) {
                parteNumerica = 0 + parteNumerica;
            }
            parteLiteral = parteLiteral + " ";
            parteNumerica = "- " + parteNumerica;

            serialCodeCorregido = parteLiteral.concat(parteNumerica);
        }
        System.out.println("ss" + serialCodeCorregido.toUpperCase());
        return serialCodeCorregido.toUpperCase();
    }

    //Este método verifica si el elemento es  un computador (1 desktop 2 laptop) 
    //para poder validar si se muestra o no el dato de pcName en la interfaz gráfica de Ver detalle elemento
    public boolean verificarTipoElemento() {
        if (elemento.getElementId() != null) {
            int tipoElementoId = elemento.getTypeId().getElementTypeId();
            boolean bandera = (tipoElementoId == 1 || tipoElementoId == 2) ? true : false;
            return bandera;
        } else {
            return false;
        }
    }

    public List<Convention> listarCovenciones() {
        return conventionFacadeLocal.findAll();
    }

    public List<Element> listarElementosMSC() {
        elementosMsc = new ArrayList<>();
        this.elementosMsc = elementFacadeLocal.listarElementosMsc();
        return elementosMsc;
    }

    public void validarPuestos() {
        FacesContext context = FacesContext.getCurrentInstance();
        serialCodeOrigen = serialCodeOrigen.toUpperCase();
        serialCodeDestino = serialCodeDestino.toUpperCase();

        if (!serialCodeOrigen.equals(serialCodeDestino)) {
            puestoOrigen = deskFacadeLocal.buscarPorSerialCode(serialCodeOrigen);
            puestoDestino = deskFacadeLocal.buscarPorSerialCode(serialCodeDestino);
            String puestoInexistente = "";

            if (puestoOrigen != null && puestoDestino != null) {
                elementosPorPuestosOrigen = elementFacadeLocal.listarElementosPorPuesto(puestoOrigen);
                elementosPorPuestosDestino = elementFacadeLocal.listarElementosPorPuesto(puestoDestino);
                this.abrirModal(1);
            } else {
                puestoInexistente = puestoOrigen == null ? serialCodeOrigen : serialCodeDestino;
            }

            if (!puestoInexistente.equals("")) {
                //Mensaje diciendo quqe los puesto no se encuentran registrados
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El puesto " + puestoInexistente + " No se encuentra regstrado"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Los puestos no pueden ser iguales"));
        }
    }

    public void migrarElementos() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean mensaje = true;
        for (Element item : elementosPorPuestos) {
            item.setDeskId(puestoDestino);
            elementFacadeLocal.edit(item);
        }
        cerrarModal(2);
        cerrarModal(3);

        if (mensaje) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Elementos migrados exitosamente"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error al migrar los elementos"));
        }
    }

    public void asignarElemento(Element elemento) {
        this.elemento = elemento;
    }

    public List<String> autoCompletarSerialCode(String query) {
        List<String> resultados = new ArrayList<String>();
        resultados.addAll(deskFacadeLocal.buscarPorSerialCodeAutoComplete(query.toUpperCase()));
        return resultados;
    }

    public void abrirModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        switch (opcion) {
            case 1:
                req.execute("PF('modalMigrarElementos').show()");
                break;
            default:
                break;
        }
    }

    public void cerrarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registrarPuesto').hide()");
                form = "formRegistrar:gridRegistrar";
                break;
            case 2:
                req.execute("PF('modalMigrar').hide()");
                form = "formMigrar:gridMigrar";
                break;
            case 3:
                req.execute("PF('modalMigrarElementos').hide()");
                form = "formMigrarElementos:gridMigrarElementos";
                init();
                break;
            case 4:
                req.execute("PF('modalVerPuesto').hide()");
                form = "";
                break;
            case 5:
                req.execute("PF('modalEditarPuesto').hide()");
                form = "formEditar:gridEditar";
                break;
            default:
                break;
        }
        req.reset(form);
        req.update("formInventario:tablaInventario");
    }

    public void asignarElementoAlPuesto(Element element) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.elemento = element;
            elemento.setDeskId(puesto);
            elementFacadeLocal.edit(elemento);

            listarElementosMSC();
            listarElementosPorPuesto();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Elemento asignado correctamente"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error al asignar elemento"));
        }
    }

    public void eliminarElementoDelPuesto(Element element) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.elemento = element;
            elemento.setDeskId(null);
            elementFacadeLocal.edit(elemento);

            listarElementosMSC();
            listarElementosPorPuesto();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Elemento eliminado correctamente"));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error al eliminar elemento"));

        }
    }

    public Desk getPuesto() {
        return puesto;
    }

    public void setPuesto(Desk puesto) {
        this.puesto = puesto;
    }

    public Element getElemento() {
        return elemento;
    }

    public void setElemento(Element elemento) {
        this.elemento = elemento;
    }

    public ElementType getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(ElementType tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public List<Element> getElementosPorPuestos() {
        return elementosPorPuestos;
    }

    public void setElementosPorPuestos(List<Element> elementosPorPuestos) {
        this.elementosPorPuestos = elementosPorPuestos;
    }

    public List<ElementType> getTipoElementos() {
        return tipoElementos;
    }

    public void setTipoElementos(List<ElementType> tipoElementos) {
        this.tipoElementos = tipoElementos;
    }

    public List<Desk> getPuestos() {
        return puestos;
    }

    public void setPuestos(List<Desk> puestos) {
        this.puestos = puestos;
    }

    public String getSerialCodeOrigen() {
        return serialCodeOrigen;
    }

    public void setSerialCodeOrigen(String serialCodeOrigen) {
        this.serialCodeOrigen = serialCodeOrigen;
    }

    public String getSerialCodeDestino() {
        return serialCodeDestino;
    }

    public void setSerialCodeDestino(String serialCodeDestino) {
        this.serialCodeDestino = serialCodeDestino;
    }

    public List<Element> getElementosPorPuestosDestino() {
        return elementosPorPuestosDestino;
    }

    public void setElementosPorPuestosDestino(List<Element> elementosPorPuestosDestino) {
        this.elementosPorPuestosDestino = elementosPorPuestosDestino;
    }

    public List<Element> getElementosPorPuestosOrigen() {
        return elementosPorPuestosOrigen;
    }

    public void setElementosPorPuestosOrigen(List<Element> elementosPorPuestosOrigen) {
        this.elementosPorPuestosOrigen = elementosPorPuestosOrigen;
    }

    public List<Element> getElementosMsc() {
        return elementosMsc;
    }

    public void setElementosMsc(List<Element> elementosMsc) {
        this.elementosMsc = elementosMsc;
    }

    public List<Convention> getConvenciones() {
        return convenciones;
    }

    public void setConvenciones(List<Convention> convenciones) {
        this.convenciones = convenciones;
    }

    public String getSerialCodeFilter() {
        return serialCodeFilter;
    }

    public void setSerialCodeFilter(String serialCodeFilter) {
        this.serialCodeFilter = serialCodeFilter;
    }

}
