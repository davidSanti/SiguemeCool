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
import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.ConventionFacadeLocal;
import com.sigueme.backend.model.DeskFacadeLocal;
import com.sigueme.backend.model.ElementFacadeLocal;
import com.sigueme.backend.model.ElementTypeFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Santi
 */
@Named(value = "cargaMasivaController")
@ViewScoped
public class CargaMasivaController implements Serializable {

    @EJB
    private UserFacadeLocal userFacadeLocal;
    @EJB
    private DeskFacadeLocal deskFacadeLocal;
    @EJB
    private ConventionFacadeLocal conventionFacadeLocal;
    @EJB
    private ElementFacadeLocal elementFacadeLocal;
    @EJB
    private ElementTypeFacadeLocal elementTypeFacadeLocal;

    private User usuario;
    private Desk puesto;
    private Convention convencion;
    private Element elemento;
    private ElementType tipoElmento;

    private UploadedFile file;

    public CargaMasivaController() {
    }

    @PostConstruct
    public void init() {
        usuario = new User();
        puesto = new Desk();
        convencion = new Convention();
        elemento = new Element();
        tipoElmento = new ElementType();
    }

    public void cerrarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = "";
        switch (opcion) {
            case 1:
                req.execute("PF('cargaMasiva').hide()");
                formulario = ":formCargaMasiva:gridCargaMasiva";
                break;
            default:
                break;
        }
        init();
        req.reset(formulario);
    }

    public String cargarAdjunto() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");

        String pathReal = null;
        try {
            if (file != null && !file.getFileName().equals("")) {
                String nombre = file.getFileName();
                pathReal = "archivos\\" + nombre;
                path += "\\" + nombre;
                InputStream input = file.getInputstream();
                byte[] data = new byte[input.available()];
                input.read(data);
                FileOutputStream output = new FileOutputStream(path);
                output.write(data);
                output.close();
        
            } else {
                System.out.println("nada");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo" + e.getMessage());
        }
        return path;
    }

    public void cargaMasivaDePuestos() {
        cargarPuesto();
    }

    public void cargarPuesto() {
        try {
            puesto = new Desk();
            convencion = new Convention();

            String path = cargarAdjunto();
            File ff = new File(path);
            System.out.println("sss" + ff.exists());
            Workbook workbook = Workbook.getWorkbook(ff);
            System.out.println("vamos");
            Sheet sheet = workbook.getSheet(0);
            System.out.println("sss" + sheet.getRows());
            for (int fila = 1; fila < (sheet.getRows()); fila++) {
                String serialCode = sheet.getCell(0, fila).getContents();
                String stringconvencion = sheet.getCell(1, fila).getContents();

                puesto.setSerialCode(retornarSerialCode(serialCode));
                System.out.println("serial: " + puesto.getSerialCode());

                this.convencion = buscarConvencionPorDescripcion(stringconvencion);
                if (this.convencion == null) {
                    this.convencion = registrarConvencion(stringconvencion);
                }
                puesto.setConventionId(this.convencion);
                System.out.println("convencion" + puesto.getConventionId().getDescription());
                deskFacadeLocal.create(puesto);
            }

            cerrarModal(1);
        } catch (Exception e) {
            System.out.println("ee" + e.getMessage());
            Logger.getLogger(CargaMasivaController.class.getName()).log(Level.SEVERE, null, e);

        }

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().setKeepMessages(true);
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces" + ec.getRequestPathInfo());
        } catch (IOException ex) {
            Logger.getLogger(CargaMasivaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Este método es exclusivo de los puestos
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
        return serialCodeCorregido.toUpperCase();
    }

    public Convention buscarConvencionPorDescripcion(String descripcion) {
        Convention convencionBusqueda = null;
        try {
            List<Convention> lista = conventionFacadeLocal.findAll();
            descripcion = descripcion.replaceAll(" ", "");

            for (Convention item : lista) {
                String itemDescripcion = item.getDescription().replaceAll(" ", "");

                if (itemDescripcion.equals(descripcion)) {
                    convencionBusqueda = item;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return convencionBusqueda;
    }

    public Convention registrarConvencion(String nombre) {
        Convention nuevaConvencion = new Convention();
        boolean bandera = false;
        try {
            nuevaConvencion.setDescription(nombre);
            conventionFacadeLocal.create(nuevaConvencion);
            bandera = true;
        } catch (Exception e) {
        }

        if (bandera) {
            return nuevaConvencion;
        } else {
            return null;
        }
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Desk getPuesto() {
        return puesto;
    }

    public void setPuesto(Desk puesto) {
        this.puesto = puesto;
    }

    public Convention getConvencion() {
        return convencion;
    }

    public void setConvencion(Convention convencion) {
        this.convencion = convencion;
    }

    public Element getElemento() {
        return elemento;
    }

    public void setElemento(Element elemento) {
        this.elemento = elemento;
    }

    public ElementType getTipoElmento() {
        return tipoElmento;
    }

    public void setTipoElmento(ElementType tipoElmento) {
        this.tipoElmento = tipoElmento;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
