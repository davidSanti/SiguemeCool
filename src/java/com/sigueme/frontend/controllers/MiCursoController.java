/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
import com.sigueme.backend.model.UserByCourseFacadeLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author ZamudioL
 */
@Named(value = "miCursoController")
@ViewScoped
public class MiCursoController implements Serializable {

    @EJB
    private UserByCourseFacadeLocal userByCourseFacadeLocal;

    List<UserByCourse> misCursos;
    private UserByCourse usuariosMiCurso;
    private UploadedFile file;
    private StreamedContent downloadFile;

    private String filtrarEstado;

    public MiCursoController() {
    }

    @PostConstruct
    public void init() {
        listraMisCursos();
        usuariosMiCurso = new UserByCourse();
        filtrarEstado = "";
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
                String path = context.getExternalContext().getRealPath("/");
                path = path.substring(0, path.indexOf("\\build\\"));
                path += "\\Web\\" + url;

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
        path = path.substring(0, path.indexOf("\\build\\"));
        path += "\\Web\\archivos\\";
        String pathReal = null;

        try {
            String nombre = file.getFileName();
            path += nombre;
//            pathReal = "/archivos/" + nombre;
            pathReal = "\\archivos\\" + nombre;
            System.out.println("ruta " + pathReal);
            InputStream input = file.getInputstream();
            byte[] data = new byte[input.available()];
            input.read(data);
            FileOutputStream output = new FileOutputStream(path);
            output.write(data);
            output.close();
            renombrarArchivo(pathReal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathReal;
    }

    public void renombrarArchivo(String pathDB) {
//        String nombreCurso = usuariosMiCurso.getCourseId().getCourseName();
//        String cedula = usuariosMiCurso.getUserId().getIdentification();
//        String archivo = nombreCurso.replaceAll(" ", "_") + "_" + cedula;
//
//        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + pathDB;
//        path = path.substring(0, path.indexOf("\\build\\"));
//        path += "\\Web\\" + pathDB;
//        File f = new File(path);
//
//        String rutaCompleta;
//        if (f.exists()) {
//            String url = path.substring(0, path.indexOf("\\archivos\\"));
//            String extension = pathDB.substring(pathDB.lastIndexOf(".", pathDB.length()));
//            
//            rutaCompleta = url + "\\archivos\\" + archivo + extension;
//            System.out.println(rutaCompleta);
//            if (f.renameTo(new File(rutaCompleta))) {
//                System.out.println("bo");
//            }
//        }

        File f = new File("C:\\Users\\ZamudioL\\Desktop\\PROYECTO QUE I ESTA BIEN BONITO\\otro\\SiguemeKahe\\web\\archivos\\mua.txt");
        if (f.exists()) {
            System.out.println("ss");
            boolean ff = f.renameTo(new File("C:\\Users\\ZamudioL\\Desktop\\PROYECTO QUE I ESTA BIEN BONITO\\otro\\SiguemeKahe\\web\\archivos\\hola.txt"));
            if (ff) {
                System.out.println("goog");
            }
        }
    }

    public void descargarAdjunto() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (this.usuariosMiCurso.getAttached() != null) {
                String url = this.usuariosMiCurso.getAttached();
                String path = fc.getExternalContext().getRealPath("/") + url;
                path = path.substring(0, path.indexOf("\\build\\"));
                path += "\\Web\\" + url;
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

        if (usuarioCurso.getGrade() == null) {
            if (usuarioCurso.getAttached() == null || usuarioCurso.getAttached().equals("")) {
                calificacion = "Sin Evidencia";
            } else {
                calificacion = "Pendiente Por Calificar";
            }

        } else if (usuarioCurso.getGrade()) {
            calificacion = "Aprobado";
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
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, true,"calificado");
                break;
            case "no_Aprobado":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, false,"calificado");
                break;
            case "pendiente":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, false, "pendiente");                
                break;
            case "sin_Evidencia":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCValificacion(usuarioEnSeison, false,"sin_Evidencia");
                break;
            case "todos":
                listraMisCursos();
                break;
            default:
                break;
        }
        RequestContext.getCurrentInstance().update("formMiCurso:miCursoTabla");
    }
}
