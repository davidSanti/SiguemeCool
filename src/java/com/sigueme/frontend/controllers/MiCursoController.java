/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
import com.sigueme.backend.model.CourseFacadeLocal;
import com.sigueme.backend.model.UserByCourseFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @EJB
    private UserFacadeLocal userFacadeLocal;

    List<UserByCourse> misCursos;
    private UserByCourse usuariosMiCurso;
    private UploadedFile file;
    private StreamedContent downloadFile;

    private String filtrarEstado;
    private PieChartModel pieModel;
    private Map<String, Integer> tabla;
    private String busqueda;
    private Date fechaInicio;
    private Date fechaFin;

    public MiCursoController() {
    }

    @PostConstruct
    public void init() {
        listraMisCursos();
        usuariosMiCurso = new UserByCourse();
        filtrarEstado = "";
        tabla = new HashMap();
        createPieModels();
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
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

    public Map<String, Integer> getTabla() {
        return tabla;
    }

    public void setTabla(Map<String, Integer> tabla) {
        this.tabla = tabla;
    }

    public void asignarUsuarioCursos(UserByCourse usuarioCurso, boolean opcion) {
        usuariosMiCurso = usuarioCurso;
        if (opcion) {
            descargarAdjunto();
        }
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
        User usuarioEnSesion = devolverUsuarioEnSesion();
        int cantidad = 0;

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(usuarioEnSesion, true, 1);
        if (cantidad > 0) {
            tabla.put("Aprobado", cantidad);
        }

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(usuarioEnSesion, false, 1);
        if (cantidad > 0) {
            tabla.put("No Aprobado", cantidad);
        }

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(usuarioEnSesion, false, 2);
        if (cantidad > 0) {
            tabla.put("Sin Calificar", cantidad);
        }

        cantidad = userByCourseFacadeLocal.listarMisCursosCalificados(usuarioEnSesion, false, 3);
        if (cantidad > 0) {
            tabla.put("Vencido", cantidad);
        }

        for (Map.Entry<String, Integer> entry : tabla.entrySet()) {
            String clave = entry.getKey();
            Integer valor = entry.getValue();
            pieModel.set(clave, valor);
        }

        pieModel.setTitle("Grafica Mis Cursos");
        pieModel.setShowDataLabels(true);
        pieModel.setLegendPosition("w");
        pieModel.setMouseoverHighlight(true);
        pieModel.setShadow(true);
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
            if (usuariosMiCurso.getAttached() != null && !archivo.equals(usuariosMiCurso.getAttached())) {
                System.out.println("eliminar anterior archivo");
//                File f = new File("C:\\Users\\Microsoft Windows 10\\Documents\\_SUSTS\\DEVELOP\\SIGUEME\\Sigueme\\build\\web\\archivos\\Curso_Total_1030692952.jpg");
//                f.delete();
//                eliminarEvidencia(2);
            }
            this.usuariosMiCurso.setAttached(archivo);
            try {
                userByCourseFacadeLocal.edit(usuariosMiCurso);
                enviarCorreoAlEnviarEvidencia(usuariosMiCurso.getUserId());
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

    public void enviarCorreoAlEnviarEvidencia(User user) {
        try {
            List<User> listaUsuarios;
            GroupCls grupoUsuarioEnSesion = user.getGroupId();
            //Como el correo se debe enviar al jefe, si el rol es de supervisor, el correo va dirigido al Site Manager
            if (grupoUsuarioEnSesion.getGroupId() == 1) {
                listaUsuarios = userFacadeLocal.listarUsuariosSiteManager();
            } else {
                listaUsuarios = userFacadeLocal.listarSupervisorPorGrupo(grupoUsuarioEnSesion);
            }
//           Aquí se puede añadir uno o varios usuarios(Correos) si no solo se quiere enviar el correo al supervisor (Ej. Copia al practicante)
//            listaUsuarios.add();
            String nombreUsuario = user.getFirstName() + " " + user.getLastName();
            this.enviarCorreo(listaUsuarios,
                    "Envío de Evidencia de " + nombreUsuario,
                    "El Asesor " + nombreUsuario + "Envío la actividad correpondiente al siguiente curso: ");

        } catch (Exception e) {
            System.out.println("Error al enviar correo enviarCorreoAlEnviarEvidencia: " + e.getMessage());
        }
    }

    public void enviarCorreo(List<User> usuariosCorreo, String asunto, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");
        Course curso = usuariosMiCurso.getCourseId();
        try {
            System.out.println(asunto + mensaje
                    + "\n\nNombre: " + curso.getCourseName()
                    + "\nDescripción: " + curso.getDescription()
                    + "\nFecha de Finalizacion: " + fecha.format(curso.getFinishDate()));
//            Mail.send(usuariosCorreo, asunto, mensaje
//                    + "\n\nNombre: " + curso.getCourseName()
//                    + "\nDescripción: " + curso.getDescription()
//                    + "\nFecha de Finalizacion: " + fecha.format(curso.getFinishDate()));
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Correo enviado exitosamente"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo enviar el correo electrónico"));
        }
    }

    public void eliminarEvidenciaCurso(UserByCourse usuarioCurso) {
        FacesContext context = FacesContext.getCurrentInstance();
        usuariosMiCurso = usuarioCurso;
        if (eliminarEvidencia(1)) {
            usuariosMiCurso = new UserByCourse();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Tu evidencia se ha eliminado correctamente"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Tu evidencia no se pudo eliminar, intenta más tarde"));
        }
    }

    public boolean eliminarEvidencia(int opcion) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean bandera = false;
        try {
            if (usuariosMiCurso != null) {
                String url = usuariosMiCurso.getAttached();
                String path = context.getExternalContext().getRealPath("/") + url;
                File f = new File(path);
//                File f = new File("C:\\Users\\Microsoft Windows 10\\Documents\\_SUSTS\\DEVELOP\\SIGUEME\\Sigueme\\build\\web\\archivos\\Curso_Total_1030692952.pdf");

                InputStream stream = (InputStream) new FileInputStream(f);
                stream.close();
                if (f.exists()) {
                    System.out.println("if exist: " + path);
                    if (f.delete()) {
                        System.out.println("if dele");
                        if (opcion == 1) {
                            usuariosMiCurso.setDescription(null);
                            usuariosMiCurso.setAttached(null);
                            userByCourseFacadeLocal.edit(usuariosMiCurso);
                        }
                        bandera = true;
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "La ruta del archivo no se ha encontrado en el sistema"));
                }
            }

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Ha ocurrido un error al elminar tu evidencia"));
        }
        return bandera;
    }

    // Este metodo se encarga de la carga de archivos opteniendo el archivo desde intefaz y guardandolo en la carpeta archivos
    public String cargarAdjunto() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
//        path = path.substring(0, path.indexOf("\\build\\"));
//        path += "\\Web\\archivos\\";
        String pathReal = null;
        FileOutputStream output = null;
        try {
//            String nombre = file.getFileName();
            if (file != null && !file.getFileName().equals("")) {
                String nombre = renombrarArchivo(file.getFileName());
                pathReal = "archivos\\" + nombre;
                path += "\\" + nombre;
                InputStream input = file.getInputstream();
                byte[] data = new byte[input.available()];
                input.read(data);
                output = new FileOutputStream(path);
                output.write(data);
                output.close();
            }
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
        FacesContext fc = FacesContext.getCurrentInstance();
        InputStream stream = null;
        try {
            if (this.usuariosMiCurso.getAttached() != null) {
                String url = this.usuariosMiCurso.getAttached();
                String path = fc.getExternalContext().getRealPath("/") + url;
                File f = new File(path);
                downloadFile = null;
                if (f.exists()) {
                    stream = (InputStream) new FileInputStream(f);
                    downloadFile = new DefaultStreamedContent(stream, URLConnection.guessContentTypeFromStream(stream), f.getName());
                }
            }
            stream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
//        finally {
//            try {
//                stream.close();
//            } catch (IOException ex) {
//                Logger.getLogger(MiCursoController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
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
                    calificacion = "Pendiente Por Validar";
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
        if (downloadFile == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Al parecer el archivo se ha eliminado"));
        }
    }

    public void filtrarPorEstado(AjaxBehaviorEvent event) {
        User usuarioEnSeison = devolverUsuarioEnSesion();

        switch (filtrarEstado) {
            case "aprobado":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCalificacion(usuarioEnSeison, true, "calificado");
                break;
            case "no_Aprobado":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCalificacion(usuarioEnSeison, false, "calificado");
                break;
            case "pendiente":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCalificacion(usuarioEnSeison, false, "pendiente");
                break;
            case "sin_Evidencia":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCalificacion(usuarioEnSeison, true, "sin_Evidencia");
                break;
            case "vencido":
                misCursos = userByCourseFacadeLocal.filtrarMisCursosPorCalificacion(usuarioEnSeison, false, "sin_Evidencia");
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

    public void filtrarPorCurso() {
        misCursos = new ArrayList<>();
        misCursos = cursoFacadeLocal.filtrarMisCursosPorNombre(busqueda, devolverUsuarioEnSesion());
    }

    public void filtrarPorFechas() {
        misCursos = new ArrayList<>();
        if (fechaInicio == null && fechaFin == null) {
            listraMisCursos();
        } else {
            if (fechaInicio != null || fechaFin != null) {
                misCursos = cursoFacadeLocal.filtrarMisCursosPorFechas(fechaInicio, fechaFin, devolverUsuarioEnSesion());
            } else {
                listraMisCursos();
            }
        }
    }

}
