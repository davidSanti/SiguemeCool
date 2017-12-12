/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
import com.sigueme.backend.model.CourseFacadeLocal;
import com.sigueme.backend.model.UserByCourseFacadeLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.PieChart;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author ZamudioL
 */
@Named(value = "miCursoController")
@ViewScoped
public class MiCursoController implements Serializable {

    @EJB
    private UserByCourseFacadeLocal userByCourseFacadeLocal;
    @EJB
    private CourseFacadeLocal cursoFacadeLocal;

    List<UserByCourse> misCursos;
    private UserByCourse usuariosMiCurso;
    private UploadedFile file;
    private StreamedContent downloadFile;

    private String filtrarEstado;
    private PieChartModel pieModel;

    public MiCursoController() {
    }

    @PostConstruct
    public void init() {
        listraMisCursos();
        usuariosMiCurso = new UserByCourse();
        filtrarEstado = "";
        createPieModels();
    }

    public UserByCourse getUsuariosMiCurso() {
        return usuariosMiCurso;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public void setUsuariosMiCurso(UserByCourse usuariosMiCurso) {
        this.usuariosMiCurso = usuariosMiCurso;
    }

    public List<UserByCourse> getMisCursos() {
        return misCursos;
    }

    public void setMisCursos(List<UserByCourse> misCursos) {
        this.misCursos = misCursos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void asignarUsuarioCursos(UserByCourse usuarioCurso) {
        usuariosMiCurso = usuarioCurso;

        descargarAdjunto();
    }

    public String getFiltrarEstado() {
        return filtrarEstado;
    }

    public void setFiltrarEstado(String filtrarEstado) {
        this.filtrarEstado = filtrarEstado;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public void createPieModels() {
        createPieModel();
    }

    private void createPieModel() {
        pieModel = new PieChartModel();

        int cantidad = 0;
        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(true, 1);
        if (cantidad > 0) {
            pieModel.set("Aprobado " + cantidad, cantidad);
        }

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(false, 1);
        if (cantidad > 0) {
            pieModel.set("No Aprobado", cantidad);

        }

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(false, 2);
        if (cantidad > 0) {
            pieModel.set("Sin Calificar", cantidad);
        }

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(false, 3);
        if (cantidad > 0) {
            pieModel.set("Vencido", cantidad);
        }

        pieModel.setTitle("Grafica Mis Cursos");
        pieModel.setShowDataLabels(true);
        pieModel.setLegendPosition("w");
    }

    //Este metodo captura el usuario que esta en sesion y lista los cursos asginados a esa persona
    public void listraMisCursos() {
        User usuarioEnSesion = devolverUsuarioEnSesion();
        misCursos = userByCourseFacadeLocal.listarMisCursos(usuarioEnSesion);
    }

    public User devolverUsuarioEnSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        User usuarioEnSesion = (User) sesion.getAttribute("usuario");
        return usuarioEnSesion;
    }

    //Este metodo es el encargado de actualizar el registro en la tabla UserByCourse 
    // con el archivo la descrpcion proporcionada por un usuario asignado a un curso en particular
    public void subirEvidencia() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (file != null && !file.getFileName().equals("")) {
            String archivo = cargarAdjunto();
            this.usuariosMiCurso.setAttached(archivo);
            try {
                userByCourseFacadeLocal.edit(usuariosMiCurso);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Tu evidencia se ha cargado correctamente"));

            } catch (Exception e) {
                RequestContext.getCurrentInstance().execute("PF('subirEvidencia').show();");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Ha ocurrido un error al cargar tu evidencia"));
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('subirEvidencia').show();");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No puedes guardar sin adjuntar un archivo por favor"));
        }

    }

    public void eliminarEvidencia(UserByCourse usuarioCurso) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (usuarioCurso != null) {
                boolean bandera = false;
                String url = usuarioCurso.getAttached();
                String path = context.getExternalContext().getRealPath("/") + url;
                File f = new File(path);
                InputStream stream = (InputStream) new FileInputStream(f);
                stream.close();
                if (f.exists()) {
                    if (f.delete()) {
                        usuarioCurso.setDescription(null);
                        usuarioCurso.setAttached(null);
                        userByCourseFacadeLocal.edit(usuarioCurso);
                        bandera = true;
                    }
                }
                if (bandera) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Tu evidencia se ha eliminado correctamente"));
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Tu evidencia no se pudo eliminar, intenta más tarde"));
                }
            }

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Ha ocurrido un error al elminar tu evidencia"));
        }

    }

    // Este metodo se encarga de la carga de archivos opteniendo el archivo desde intefaz y guardandolo en la carpeta archivos
    public String cargarAdjunto() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
//        path = path.substring(0, path.indexOf("\\build\\"));
//        path += "\\Web\\archivos\\";
        String pathReal = null;

        try {
//            String nombre = file.getFileName();
            String nombre = renombrarArchivo(file.getFileName());
            pathReal = "archivos\\" + nombre;
            path += "\\" + nombre;
            InputStream input = file.getInputstream();
            byte[] data = new byte[input.available()];
            input.read(data);
            FileOutputStream output = new FileOutputStream(path);
            output.write(data);
            output.close();
        } catch (IOException e) {
            System.out.println("Error al cargar el arhcivo" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al cargar el arhcivo" + e.getMessage());
        }
        return pathReal;
    }

    public String renombrarArchivo(String nombreOriginal) {
        String nombreCurso = usuariosMiCurso.getCourseId().getCourseName();
        String cedula = usuariosMiCurso.getUserId().getIdentification();
        String archivo = nombreCurso.replaceAll(" ", "_") + "_" + cedula;
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".", nombreOriginal.length()));
        archivo += extension;
        return archivo;
    }

    public void descargarAdjunto() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (this.usuariosMiCurso.getAttached() != null) {
                String url = this.usuariosMiCurso.getAttached();
                String path = fc.getExternalContext().getRealPath("/") + url;
                File f = new File(path);
                InputStream stream = (InputStream) new FileInputStream(f);

                downloadFile = new DefaultStreamedContent(stream, URLConnection.guessContentTypeFromStream(stream), f.getName());

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String determinarCalificacion(UserByCourse usuarioCurso) {
        String calificacion = "No Aprobado";
        if (usuarioCurso != null && usuarioCurso.getUserByCourseId() != null) {
            if (usuarioCurso.getGrade() == null) {
                if (usuarioCurso.getAttached() == null || usuarioCurso.getAttached().equals("")) {
                    if (!usuarioCurso.getCourseId().getCouseStatus()) {
                        calificacion = "Vencido";
                    } else {
                        calificacion = "Sin Evidencia";
                    }
                } else {
                    calificacion = "Pendiente Por Calificar";
                }

            } else if (usuarioCurso.getGrade()) {
                calificacion = "Aprobado";
            }
        }
        return calificacion;
    }

    //Este método es importante ya que cuando el archivo se descarga por primera vez el Stream se cierra 
    //y debemos asignar de nuevo el archivo parta que no arroje error
    public void descargarAdjuntoDeNuevo() {
        descargarAdjunto();
    }

    public void filtrarPorEstado(AjaxBehaviorEvent event) {
        User usuarioEnSeison = devolverUsuarioEnSesion();

        switch (filtrarEstado) {
            case "aprobado":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, true, "calificado");
                break;
            case "no_Aprobado":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, false, "calificado");
                break;
            case "pendiente":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, false, "pendiente");
                break;
            case "sin_Evidencia":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, true, "sin_Evidencia");
                break;
            case "vencido":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, false, "sin_Evidencia");
                break;
            case "todos":
                listraMisCursos();
                break;
            default:
                break;
        }
        RequestContext.getCurrentInstance().update("formMiCurso:miCursoTabla");
    }

    public boolean validarSiCursoVencido(UserByCourse usuarioCurso, int opcion) {
        boolean bandera = false;
        if (opcion == 1) {
            if (usuarioCurso != null && usuarioCurso.getUserByCourseId() != null) {
                if (determinarCalificacion(usuarioCurso).equals("Vencido")) {
                    bandera = true;
                }
            }
        } else {
            if (usuarioCurso.getGrade() != null) {
                bandera = usuarioCurso.getGrade();
            }
        }
        return bandera;
    }
}
