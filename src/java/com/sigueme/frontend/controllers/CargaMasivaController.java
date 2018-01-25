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
import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.model.ConventionFacadeLocal;
import com.sigueme.backend.model.DeskFacadeLocal;
import com.sigueme.backend.model.ElementFacadeLocal;
import com.sigueme.backend.model.ElementTypeFacadeLocal;
import com.sigueme.backend.model.GroupClsFacadeLocal;
import com.sigueme.backend.model.RoleFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import com.sigueme.backend.model.UserStatusFacadeLocal;
import com.sigueme.backend.utilities.Crypto;
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
    private RoleFacadeLocal rolFacadeLocal;
    @EJB
    private GroupClsFacadeLocal grupoFacadeLocal;
    @EJB
    private UserStatusFacadeLocal userStatusFacadeLocal;
    @EJB
    private DeskFacadeLocal deskFacadeLocal;
    @EJB
    private ConventionFacadeLocal conventionFacadeLocal;
    @EJB
    private ElementFacadeLocal elementFacadeLocal;
    @EJB
    private ElementTypeFacadeLocal elementTypeFacadeLocal;

    private User usuario;
    private Role rol;
    private GroupCls grupo;
    private Desk puesto;
    private Convention convencion;
    private Element elemento;
    private ElementType tipoElemento;

    private UploadedFile file;

    public CargaMasivaController() {
    }

    @PostConstruct
    public void init() {
        usuario = new User();
        rol = new Role();
        grupo = new GroupCls();
        puesto = new Desk();
        convencion = new Convention();
        elemento = new Element();
        tipoElemento = new ElementType();
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

    private String cargarAdjunto() {
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

            } 
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo" + e.getMessage());
        }
        return path;
    }

    /*Inicio Código para la carga masiva de Puesto*/
    public void cargaMasivaDePuestos() {
        cargarPuesto();
    }

    private void cargarPuesto() {
        int cuantosSeRegistraron = 0;
        try {
            String path = cargarAdjunto();
            File ff = new File(path);
            Workbook workbook = Workbook.getWorkbook(ff);
            Sheet sheet = workbook.getSheet(0);
            for (int fila = 1; fila < (sheet.getRows()); fila++) {
                puesto = new Desk();
                convencion = new Convention();

                String serialCode = sheet.getCell(0, fila).getContents();
                String stringconvencion = sheet.getCell(1, fila).getContents();

                puesto.setSerialCode(retornarSerialCode(serialCode));

                this.convencion = buscarConvencionPorDescripcion(stringconvencion);
                if (this.convencion == null) {
                    this.convencion = registrarConvencion(stringconvencion);
                }
                puesto.setConventionId(this.convencion);
                try {
                    deskFacadeLocal.create(puesto);
                    cuantosSeRegistraron += 1;
                } catch (Exception e) {
                    System.out.println("ERROR AL REGISTRAR EL PUESTO: " + serialCode);
                }
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
    private String retornarSerialCode(String serial) {
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

    private Convention buscarConvencionPorDescripcion(String descripcion) {
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

    private Convention registrarConvencion(String nombre) {
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

    /*Fin Código para la carga masiva de Puesto*/
 /*Inicio Código para la carga masiva de Elementos*/
    public void cargaMasivaDeElementos() {
        cargarElementos();
    }

    private void cargarElementos() {
        int cantidadElementosRegistrados = 0;
        try {
            String path = cargarAdjunto();
            File ff = new File(path);
            Workbook workbook = Workbook.getWorkbook(ff);
//            Sheet sheet = workbook.getSheet(0);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheet(i);
                System.out.println("nombre" + sheet.getName());

                for (int fila = 1; fila < (sheet.getRows()); fila++) {
                    elemento = new Element();
                    tipoElemento = new ElementType();

                    String stringPuesto = sheet.getCell(0, fila).getContents();
                    String stringTipoElemento = sheet.getCell(1, fila).getContents();
                    String serialCode = sheet.getCell(2, fila).getContents();
                    String placaInventario = sheet.getCell(3, fila).getContents();
                    String marca = sheet.getCell(4, fila).getContents();
                    String modelo = sheet.getCell(5, fila).getContents();
                    String comentario = sheet.getCell(7, fila).getContents();

                    Desk puestoElemento = buscarPuestoPorSerialCode(retornarSerialCode(stringPuesto));

                    if (puestoElemento != null) {
                        elemento.setDeskId(puestoElemento);

                        tipoElemento = buscarTipoElementoPorDescripcion(stringTipoElemento);
                        if (tipoElemento == null) {
                            tipoElemento = registrarNuevoTipoElemento(stringTipoElemento);
                        }

                        elemento.setTypeId(tipoElemento);
                        elemento.setSerialCode(serialCode);
                        elemento.setInventoryPlaque(placaInventario);
                        elemento.setBrand(marca);
                        elemento.setModel(modelo);
                        if (verificarSiElementoEsComputador(tipoElemento)) {
                            String nombrePc = sheet.getCell(6, fila).getContents();
                            elemento.setPcName(nombrePc);
                        }
                        try {
                            elementFacadeLocal.create(elemento);
                        } catch (Exception e) {
                            System.out.println("ERROR AL REGISTRAR EL ELEMENTO DEL PUESTO: " + stringPuesto);
                        }
                        cantidadElementosRegistrados += 1;
                    }
                }
            }
            System.out.println("cuantos se registraron:" + cantidadElementosRegistrados);
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

    private Desk buscarPuestoPorSerialCode(String serialPuesto) {
        Desk puestoReal = null;
        try {
            puestoReal = deskFacadeLocal.buscarPorSerialCode(serialPuesto);
        } catch (Exception e) {
        }
        return puestoReal;
    }

    private ElementType buscarTipoElementoPorDescripcion(String descripcion) {
        ElementType tipoBusqueda = null;
        try {
            List<ElementType> lista = elementTypeFacadeLocal.findAll();
            descripcion = descripcion.replaceAll(" ", "");

            for (ElementType item : lista) {
                String itemDescripcion = item.getDescription().replaceAll(" ", "");

                if (itemDescripcion.equalsIgnoreCase(descripcion)) {
                    tipoBusqueda = item;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return tipoBusqueda;
    }

    private ElementType registrarNuevoTipoElemento(String nombre) {
        ElementType nuevoTipo = new ElementType();
        try {
            nuevoTipo.setDescription(nombre);
            elementTypeFacadeLocal.create(nuevoTipo);
        } catch (Exception e) {
        }
        return nuevoTipo;
    }

    private boolean verificarSiElementoEsComputador(ElementType tipo) {
        String descripcion = tipo.getDescription().replaceAll(" ", "").toLowerCase();
        if (descripcion.equalsIgnoreCase("desktop") || descripcion.equalsIgnoreCase("laptop")) {
            return true;
        } else {
            return false;
        }
    }

    public void cargaMasivaDeUsuarios() {
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        int cuantosSeRegistraron = 0;
        try {
            String path = cargarAdjunto();
            File ff = new File(path);
            Workbook workbook = Workbook.getWorkbook(ff);
            Sheet sheet = workbook.getSheet(0);

            for (int fila = 1; fila < (sheet.getRows()); fila++) {
                usuario = new User();
                rol = new Role();
                grupo = new GroupCls();

                String stringGrupo = sheet.getCell(0, fila).getContents();
                String identificacion = sheet.getCell(1, fila).getContents();
                String peopleSoft = sheet.getCell(2, fila).getContents();
                String apellido = sheet.getCell(3, fila).getContents();
                String nombre = sheet.getCell(4, fila).getContents();
                String correo = sheet.getCell(5, fila).getContents();
                String stringRol = sheet.getCell(6, fila).getContents();

                grupo = buscarGrupoPorDescripcion(stringGrupo);
                if (grupo == null) {
                    grupo = registrarGrupo(stringGrupo);
                }

                rol = buscarRolPorDescripcion(stringRol);
                if (rol == null) {
                    rol = registrarRol(stringRol);
                }

                usuario.setGroupId(grupo);
                usuario.setRoleId(rol);
                usuario.setIdentification(identificacion);
                usuario.setPeopleSoft(peopleSoft);
                usuario.setLastName(apellido);
                usuario.setFirstName(nombre);
                usuario.setEmail(correo);
                usuario.setUserPassword(Crypto.Encriptar(identificacion));
                //Todos los usuarios se dejan en estado 4 (Cambiar contraseña) para cuando ingresen por 1° vez tengan que cambiar su password
                usuario.setUserStatusId(userStatusFacadeLocal.find(4));
                try {
                    userFacadeLocal.create(usuario);
                    cuantosSeRegistraron += 1;
                } catch (Exception e) {
                    System.out.println("ERROR AL REGISTRAR EL Usuario:" + identificacion + " " + apellido + " " + nombre);
                }
            }
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

    private GroupCls buscarGrupoPorDescripcion(String descripcion) {
        GroupCls grupoBusqueda = null;
        String nombreGrupo = descripcion.replaceAll(" ", "").toLowerCase();
        try {
            List<GroupCls> lista = grupoFacadeLocal.findAll();
            for (GroupCls item : lista) {
                String nombreItem = item.getGroupName().replaceAll(" ", "").toLowerCase();

                if (nombreGrupo.equals(nombreItem)) {
                    grupoBusqueda = item;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return grupoBusqueda;
    }

    private GroupCls registrarGrupo(String nombre) {
        GroupCls nuevoGrupo = new GroupCls();
        try {
            nuevoGrupo.setGroupName(nombre);
            nuevoGrupo.setAverageAht(0);
            grupoFacadeLocal.create(nuevoGrupo);
        } catch (Exception e) {
        }
        return nuevoGrupo;
    }

    private Role buscarRolPorDescripcion(String descripcion) {
        Role rolBusqueda = null;
        String nombreRol = descripcion.replaceAll(" ", "").toLowerCase();
        try {
            List<Role> lista = rolFacadeLocal.findAll();
            for (Role item : lista) {
                String nombreItem = item.getDescription().replaceAll(" ", "").toLowerCase();

                if (nombreRol.equals(nombreItem)) {
                    rolBusqueda = item;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return rolBusqueda;
    }

    private Role registrarRol(String nombre) {
        Role nuevoRol = new Role();
        try {
            nuevoRol.setDescription(nombre);
            rolFacadeLocal.create(nuevoRol);
        } catch (Exception e) {
        }
        return nuevoRol;
    }

    /*Fin Código para la carga masiva de Elementos*/
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
