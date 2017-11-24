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
import com.sigueme.backend.model.UserFacadeLocal;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
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

    public MiCursoController() {
    }

    @PostConstruct
    public void init() {
        listraMisCursos();
        usuariosMiCurso = new UserByCourse();
    }

    public UserByCourse getUsuariosMiCurso() {
        return usuariosMiCurso;
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
    }

    //Este metodo captura el usuario que esta en sesion y lista los cursos asginados a esa persona
    public void listraMisCursos() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        User usuarioEnSesion = (User) sesion.getAttribute("usuario");
        misCursos = userByCourseFacadeLocal.listarMisCursos(usuarioEnSesion);
    }

    //Este metodo es el encargado de actualizar el registro en la tabla UserByCourse 
    // con el archivo la descrpcion proporcionada por un usuario asignado a un curso en particular
    public void subirEvidencia() {

        if (file != null && !file.getFileName().equals("")) {
            String archivo = cargarAdjunto();
            this.usuariosMiCurso.setAttached(archivo);
            System.out.println("se" + usuariosMiCurso.getUserByCourseId());
            userByCourseFacadeLocal.edit(usuariosMiCurso);
        } else {
            RequestContext.getCurrentInstance().execute("PF('subirEvidencia').show();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No puedes guardar sin adjuntar un archivo por favor"));
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
            pathReal = "/archivos/" + nombre;

            InputStream input = file.getInputstream();
            byte[] data = new byte[input.available()];
            input.read(data);
            FileOutputStream output = new FileOutputStream(path);
            output.write(data);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathReal;
    }
}
