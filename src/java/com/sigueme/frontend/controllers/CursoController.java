/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controllers;

import com.sigueme.backend.entities.Course;
import com.sigueme.backend.entities.GroupCls;
import com.sigueme.backend.entities.Role;
import com.sigueme.backend.entities.User;
import com.sigueme.backend.entities.UserByCourse;
import com.sigueme.backend.model.CourseFacadeLocal;
import com.sigueme.backend.model.GroupClsFacadeLocal;
import com.sigueme.backend.model.RoleFacadeLocal;
import com.sigueme.backend.model.UserByCourseFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import com.sigueme.backend.utilities.Mail;
import java.awt.AWTEventMulticaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author ZamudioL
 */
@Named(value = "cursoController")
@ViewScoped
public class CursoController implements Serializable {

    @EJB
    private CourseFacadeLocal cursoFacadeLocal;
    @EJB
    private UserByCourseFacadeLocal userByCourseFacadeLocal;
    @EJB
    private UserFacadeLocal usuarioFacadeLocal;

    @EJB
    private RoleFacadeLocal rolFacadeLocal;

    @EJB
    private GroupClsFacadeLocal grupoFacadeLocal;

    private Course curso;

    List<User> usuariosLista;
    List<User> usuariosListaAsignar;
    List<User> usuariosPorCursoEditar;
    List<Course> cursos;
    List<Integer> listaCursosTemporal;
    List<UserByCourse> usuariosPorCurso;
    List<UserByCourse> usuariosTemporalesPorCurso;
    List<GroupCls> listaGrupos;
    List<GroupCls> gruposPersona;
    List<Role> listaRoles;

    private String filtrarEstado;
    private UserByCourse usuarioPorCursoActual;
    private StreamedContent file;
    private Date fechaInicio;
    private Date fechaFin;
    private String busqueda;

    private List<User> usuariosRemovidosDelCursoEmail;

    @Inject
    CursoChartController cursoGraficaController;

    @PostConstruct
    public void init() {
        curso = new Course();
        usuariosLista = new ArrayList<>();
        usuariosListaAsignar = new ArrayList<>();
        usuariosPorCurso = new ArrayList<>();
        usuariosTemporalesPorCurso = new ArrayList<>();
        usuariosPorCursoEditar = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        gruposPersona = new ArrayList<>();
        listaRoles = new ArrayList<>();
        usuarioPorCursoActual = new UserByCourse();
        listaCursosTemporal = new ArrayList<>();
        listarCursos();
        filtrarEstado = new String();
        usuariosRemovidosDelCursoEmail = new ArrayList<>();
    }

    public CursoController() {
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
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

    public Course getCurso() {
        return curso;
    }

    public List<UserByCourse> getUsuariosPorCurso() {
        return usuariosPorCurso;
    }

    public void setUsuariosPorCurso(List<UserByCourse> usuariosPorCurso) {
        this.usuariosPorCurso = usuariosPorCurso;
    }

    public void setCurso(Course curso) {
        this.curso = curso;
    }

    public List<User> getUsuariosLista() {
        return usuariosLista;
    }

    public void setUsuariosLista(List<User> usuariosLista) {
        this.usuariosLista = usuariosLista;
    }

    public List<Course> getCursos() {
        return cursos;
    }

    public void setCursos(List<Course> cursos) {
        this.cursos = cursos;
    }

    public List<GroupCls> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<GroupCls> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public List<Role> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Role> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public String getFiltrarEstado() {
        return filtrarEstado;
    }

    public void setFiltrarEstado(String filtrarEstado) {
        this.filtrarEstado = filtrarEstado;
    }

    public List<GroupCls> getGruposPersona() {
        return gruposPersona;
    }

    public void setGruposPersona(List<GroupCls> gruposPersona) {
        this.gruposPersona = gruposPersona;
    }

    public List<User> getUsuariosPorCursoEditar() {
        return usuariosPorCursoEditar;
    }

    public void setUsuariosPorCursoEditar(List<User> usuariosPorCursoEditar) {
        this.usuariosPorCursoEditar = usuariosPorCursoEditar;
    }

    public List<UserByCourse> getUsuariosTemporalesPorCurso() {
        return usuariosTemporalesPorCurso;
    }

    public void setUsuariosTemporalesPorCurso(List<UserByCourse> usuariosTemporalesPorCurso) {
        this.usuariosTemporalesPorCurso = usuariosTemporalesPorCurso;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public UserByCourse getUsuarioPorCursoActual() {
        return usuarioPorCursoActual;
    }

    public void setUsuarioPorCursoActual(UserByCourse usuarioPorCursoActual) {
        this.usuarioPorCursoActual = usuarioPorCursoActual;
    }

    public List<User> getUsuariosListaAsignar() {
        return usuariosListaAsignar;
    }

    public void setUsuariosListaAsignar(List<User> usuariosListaAsignar) {
        this.usuariosListaAsignar = usuariosListaAsignar;
    }

    //Aquí se llama al método que valida las fechas y si éste devuelva "true" llama al método encargado de abrir el modal para asiganar usuarios
    public void registrarCurso() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (validarFechas()) {
            try {
                this.curso.setCouseStatus(true);
                this.abrirModal(1);

            } catch (Exception ex) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "No se pudo registrar"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Verifique la fechas por favor"));

        }
    }

    public void filtrarPersonasAsignar() {
        usuariosListaAsignar = new ArrayList<>();
        if (!gruposPersona.isEmpty()) {
            for (UserByCourse userByCourse : usuariosTemporalesPorCurso) {
                for (GroupCls groupCls : gruposPersona) {
                    if (Objects.equals(userByCourse.getUserId().getGroupId().getGroupId(), groupCls.getGroupId())) {
                        usuariosListaAsignar.add(userByCourse.getUserId());
                    }
                }
            }
        } else {
            for (UserByCourse userByCourse : usuariosTemporalesPorCurso) {
                usuariosListaAsignar.add(userByCourse.getUserId());
            }
        }
    }

    /*Este metodo valida si el curso tiene usuarios asignados, de ser asi registra el curso, asigna los usuarios seleccionados y 
      cierra las ventanas modales que se abrieron anteriormente y limpia el filtro de grupo y rol
     */
    public void asociarUsuarios() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!usuariosLista.isEmpty()) {
                
                usuariosListaAsignar.addAll(usuariosLista);

                for (User user : usuariosLista) {
                    System.out.println("item" + user.getFirstName());
                    UserByCourse usuarioCurso = new UserByCourse();
                    usuarioCurso.setUserId(user);
                    usuariosTemporalesPorCurso.add(usuarioCurso);
                }
                usuariosLista = new ArrayList<>();
                filtrarUsuarios();
            }
            System.out.println("cuantos??" + usuariosLista.size());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Registrado Correctamente"));

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Error al asociar los usuarios al curso"));
        }
    }

    public void removerTodosLosUsuariosAginar() {
        for (User user : usuariosListaAsignar) {
            removerUsuarioDeListaTemporal(user);
        }
//            usuariosListaAsignar.remove(user);
        filtrarUsuarios();
        filtrarPersonasAsignar();

    }

    public void removerUsuarioAginar(User usuario) {

        removerUsuarioDeListaTemporal(usuario);
        usuariosListaAsignar.remove(usuario);
        filtrarUsuarios();
        filtrarPersonasAsignar();
    }

    //Éste método obtiene todos los usuaros asociados a un curso, elimina los registros de la tabla user_by_course y por último elimina el curso seleccionado
    public void eliminarCurso(Course course) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.curso = course;
        try {
            if (curso.getFinishDate().after(Date.from(Instant.now()))) {
                boolean bandera = cursoFacadeLocal.validarEvidenciaUsuariosCurso(curso);

                if (!bandera) {
                    //abrir modal para aceptar la eliminacon de usuarios
                    abrirModal(2);
                } else {
                    if (eliminarUsuariosDelCurso()) {
                        cursoFacadeLocal.remove(curso);
                        enviarCorreoAlEditarUsuariosCurso(new ArrayList<>(), usuariosRemovidosDelCursoEmail);
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Curso Eliminado Correctamente"));
                    }
                }
                listarCursos();
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El curso se encuentra vencido y no es posible eliminarlo"));
            }
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "No se pudo eliminar el curso, intentalo más tarde"));
        }
    }

    public void eliminarTodosLosUsuariosDelCurso() {
        if (eliminarUsuariosDelCurso()) {
            ocultarModal(12);
            enviarCorreoAlEditarUsuariosCurso(new ArrayList<>(), usuariosRemovidosDelCursoEmail);
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "los usuarios se han removido correctamente"));
        }
    }

    //Este método elimina todos los usuario que aún no hallan enviado la evidencia del curso
    public boolean eliminarUsuariosDelCurso() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean bandera = true;
        try {
            List<UserByCourse> listaUsuariosPorCurso = userByCourseFacadeLocal.listarUsuariosPorCurso(curso);
            for (UserByCourse item : listaUsuariosPorCurso) {
                if (item.getAttached() == null && item.getDescription() == null) {
                    System.out.println("eliminar a " + item.getUserId().getFirstName());
                    userByCourseFacadeLocal.remove(item);
                    //Lista que luego enviará correo
                    usuariosRemovidosDelCursoEmail.add(item.getUserId());
                }
            }
        } catch (Exception e) {
            bandera = false;
        }

        if (!bandera) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Algunos usuarios no se eliminaron intentalo nuevamnte"));
        }

        ocultarModal(9);
        return bandera;
    }

    //éste método asigna el curso que está llegando de la interfaz a la variable global de éste controlador llamada curso
    public void editarCurso(Course course, int opcion) {
        this.curso = course;
        if (opcion == 1) {
            devolverUsuariosPorCurso(1);
            System.out.println("aqui");
            cursoGraficaController.setCurso(this.curso);
        }
    }

    /*Este método se utiliza para validar desde la interfaz si se debe cerrar o no el modal de editar teniendo en cuenta
        si la edicion se realizo exitosamente
     */
    public void editarCerrarModal() {
        if (editarCurso()) {
            ocultarModal(2);
            curso = new Course();
        }
    }

    //En éste método se realiza la edición de un curso y si no arroja ninguna excepción su valor deretrono es "true".
    public boolean editarCurso() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean banderita = false;
        try {
            if (validarFechas()) {
                verificarEstadoCurso();
                this.cursoFacadeLocal.edit(this.curso);
                banderita = true;
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Editado Correctamente"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Verifique la fechas por favor"));
            }

        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Ha ocurrido un error al editar el curso"));
        }
        return banderita;
    }

    public void verificarEstadoCurso() {
        if (curso.getFinishDate().after(Date.from(Instant.now()))) {
            curso.setCouseStatus(true);
        }
    }

    /*éste método lo llamamos en la línea 170 de la interfaz de course.xhtml y debido a que devuleve la misma lista de usuarios 
        que se utiliza en el filtro nos sirve para que todos los usuarios se seleccionen por defecto al relizar el filtro
     */
    public List<User> devolverUsuarios() {
        return this.usuariosLista;
    }

    public void enviarCorreoAlEditarUsuariosCurso(List<User> usuariosChecked, List<User> usuariosUnChecked) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean bandera = false;
        try {
            if (!usuariosChecked.isEmpty()) {
                for (User user : usuariosChecked) {
                    System.out.println("agregar: " + user.getFirstName());
                }
                this.enviarCorreo(usuariosChecked,
                        "Nueva Capacitación Asignada",
                        "Tienes una nueva Capacitación, dale un vistazo.");
                bandera = true;
            }
            if (!usuariosUnChecked.isEmpty()) {
                for (User user : usuariosUnChecked) {
                    System.out.println("remover: " + user.getFirstName());
                }
                this.enviarCorreo(usuariosUnChecked,
                        "Capacitación",
                        "Ten han removido de la siguiente capacitación: ");
                bandera = true;
            }

            if (bandera) {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Correo enviado exitosamente"));
            }
        } catch (Exception e) {
            System.out.println("Error al enviar correo edit: " + e.getMessage());
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "No se pudo enviar el correo electrónico"));
        }
    }

    public void enviarCorreo(List<User> usuariosCorreo, String asunto, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");
        try {
//            System.out.println(asunto + mensaje
//                    + "\n\nNombre: " + curso.getCourseName()
//                    + "\nDescripción: " + curso.getDescription()
//                    + "\nFecha de Finalizacion: " + fecha.format(curso.getFinishDate()));
//            for (User user : usuariosCorreo) {
//                System.out.println("correo a:" + user.getFirstName());
//            }
            Mail.send(usuariosCorreo, asunto, mensaje
                    + "\n\nNombre: " + curso.getCourseName()
                    + "\nDescripción: " + curso.getDescription()
                    + "\nFecha de Finalizacion: " + fecha.format(curso.getFinishDate()));
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Correo enviado exitosamente"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo enviar el correo electrónico"));
        }
    }

    //Éste método devuelve la lista de cursos en orden descendente
    public List<Course> listarCursos() {
        vencerCursos();
        cursos = cursoFacadeLocal.listarCursos();
        listaCursosTemporal = new ArrayList<>();
        asignarListaTemoral();
        return cursos;
    }

    public void asignarListaTemoral() {
        listaCursosTemporal = new ArrayList<>();
        for (Course item : cursos) {
            listaCursosTemporal.add(item.getCourseId());
        }
    }

    public void vencerCursos() {
        cursoFacadeLocal.vencerCursos();
    }

    public List<Role> listarRoles() {
        return rolFacadeLocal.findAll();
    }

    public List<GroupCls> listarGrupos() {
//        return grupoFacadeLocal.listarGrupos();
        return grupoFacadeLocal.findAll();
    }

    //Cuando se llame a éste método se verifica si el curso es diferente de null y si es así retorna todos los usuarios que tenga asociados dicho curso
    public List<UserByCourse> devolverUsuariosPorCurso(int opcion) {
        if (usuariosTemporalesPorCurso.isEmpty() && opcion == 1) {
            usuariosPorCurso = new ArrayList<>();
            if (curso != null) {
                usuariosPorCurso.addAll(userByCourseFacadeLocal.listarUsuariosPorCurso(curso));

            }
            usuariosTemporalesPorCurso = new ArrayList<>();
            usuariosTemporalesPorCurso.addAll(usuariosPorCurso);
            return usuariosPorCurso;
        } else {
            return usuariosTemporalesPorCurso;
        }
    }

    /*Este metodo sirve para cerrar los modales segun corresponda a cada uno de los casos de switch 
       y resetea los campos del formulario que se le indique 
     */
    public void ocultarModal(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        String formulario = null;
        switch (opcion) {
            case 1:
                req.execute("PF('registrarCurso').hide()");
                formulario = "formRegistrar:gridRegistrar";
                init();
                break;
            case 2:
                req.execute("PF('editarCurso').hide()");
                formulario = "formEditar:gridEditar";
                init();
                break;
            case 3:
                req.execute("PF('asociarUsuarios').hide();");
                init();
                break;
            case 4:
                req.execute("PF('verCurso').hide();");
                init();
                break;
            case 5:
                req.execute("PF('editarUsuarios').hide();");
                limpiarFiltro();
                usuariosLista = new ArrayList<>();
                usuariosTemporalesPorCurso = new ArrayList<>();
                break;
            case 6:
                req.execute("PF('subirEvidencia').hide();");
                break;
            case 7:
                req.execute("PF('Descargar').hide();");
                break;
            case 8:
                req.execute("PF('calificarCurso').hide();");
                usuarioPorCursoActual = new UserByCourse();
                formulario = "formCalificar:gridCalificar";
                break;
            case 9:
                req.execute("PF('confirmarEliminacion').hide();");
                break;
            case 10:
                req.execute("PF('graficaCurso').hide();");
                break;

            case 11:
                req.execute("PF('Grafica').hide();");
                break;
            case 12:
                req.execute("PF('confirmarEliminacion').hide();");
                curso = new Course();
                usuariosRemovidosDelCursoEmail = new ArrayList<>();
                break;
            default:
                break;
        }
        //Este condicional nos sirve para determinar si la lista usuariosPorCurso se debe vaciar
        //para algunos modales se debe hacer en otros dañaría el funcionamiento en el modal de verCurso
        if (opcion != 8 && opcion != 4 && opcion != 10) {
            usuariosPorCurso = new ArrayList<>();
        }
        req.reset(formulario);
    }

    //éste método se utiliza para abrir las ventanas modales
    public void abrirModal(int opciones) {
        RequestContext request = RequestContext.getCurrentInstance();
        switch (opciones) {
            case 1:
                request.execute("PF('asociarUsuarios').show()");
                break;
            case 2:
                request.execute("PF('confirmarEliminacion').show()");
                break;
            default:
                break;
        }
    }

    //Este metodo valida las fechas del curso teniendo en cuenta que la fecha inicial no sea mayor a la fecha final.
    public boolean validarFechas() {
        boolean bandera = false;

        if (this.curso.getStartDate().before(curso.getFinishDate())
                && this.curso.getFinishDate().after(Date.from(Instant.now()))) {
            bandera = true;
        }
        return bandera;
    }

    /*Este metodo es el encargado de devolver los usuarios que concuerden con el filtro realizado (al cerrar el selectCheckBoxMenu)
      el filtro puede ser por grupo,por rol o ambos (esto depende de si la lista del filtro esta vacia o no)
     */
    public void filtrarUsuarios() {
        List<Integer> algo = new ArrayList<>();
        System.out.println("filtrarUsuarios  tempo" + usuariosTemporalesPorCurso.size());

        for (UserByCourse item : usuariosTemporalesPorCurso) {
            algo.add(item.getUserId().getUserId());
            System.out.println("item " + item.getUserId().getFirstName());
        }
        if (!listaGrupos.isEmpty()) {

            if (!listaRoles.isEmpty()) {
                usuariosLista = usuarioFacadeLocal.filtrarUsuariosPorRolYGrupos(listaRoles, listaGrupos, algo);
            } else {
                usuariosLista = usuarioFacadeLocal.filtrarUsuariosPorGrupo(listaGrupos, algo);
            }
        } else {
            if (!listaRoles.isEmpty()) {
                usuariosLista = usuarioFacadeLocal.filtrarUsuariosPorRol(listaRoles, algo);
            } else {
                usuariosLista = new ArrayList<>();
            }
        }
        System.out.println("usuarios lista" + usuariosLista.size());
        for (User user : usuariosLista) {
            System.out.println("item lista rara " + user.getFirstName());
        }

    }

    //Este metodo se ultiliza pára realizar la limpieza de los filtros de Grupo y Rol que se encuentran en el modal de Asociar Usuarios
    public void limpiarFiltro() {
        usuariosLista = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        listaRoles = new ArrayList<>();
        gruposPersona = new ArrayList<>();

        listarGrupos();
        listarRoles();
        RequestContext.getCurrentInstance().reset("formAsociar");
        RequestContext.getCurrentInstance().update("formAsociar");
    }

    /*éste método verifica el tipo de filtro que se desea aplicar (el valor que tenga "filtrarEstado") 
      y reliza la búsqueda de los cursos acorde con la misma
     */
    public void filtrarPorEstado(AjaxBehaviorEvent event) {
        if (!filtrarEstado.equals("all")) {
            if (filtrarEstado.equals("true")) {
                cursos = cursoFacadeLocal.listarCursosPorEstado(true);
            } else {
                cursos = cursoFacadeLocal.listarCursosPorEstado(false);
            }
        } else {
            listarCursos();
        }
        RequestContext.getCurrentInstance().update("formCurso:tablaCurso");
    }

    public void filtrarPersonasPorGrupo() {
        List<User> algo = new ArrayList<>();
        for (UserByCourse item : usuariosTemporalesPorCurso) {
            algo.add(item.getUserId());
        }
        if (gruposPersona.size() > 0) {
//            usuariosPorCurso = cursoFacadeLocal.filtrarUsuariosPorGrupo(gruposPersona, curso, algo);
            usuariosPorCurso = filtrarPersonasPorGrupo(usuariosTemporalesPorCurso);
        } else {
            devolverUsuariosPorCurso(2);
        }

        System.out.println("filtrarPersona: usuariosPorCurso" + usuariosPorCurso.size());
        System.out.println("filtrarPersona: usuariosTemporalesPorCurso" + usuariosTemporalesPorCurso.size());
    }

    public List<UserByCourse> filtrarPersonasPorGrupo(List<UserByCourse> listaParaFiltrar) {
        List<UserByCourse> listaFiltrada = new ArrayList<>();

        for (UserByCourse item : listaParaFiltrar) {
            for (GroupCls grupo : gruposPersona) {
                if (item.getUserId().getGroupId().equals(grupo)) {
                    listaFiltrada.add(item);
                }
            }
        }
        return listaFiltrada;
    }

    public void removerTodosUsuariosDelCurso() {
        List<UserByCourse> listaRemover = new ArrayList<>();
        System.out.println("a eliminar cuantos" + usuariosPorCurso.size());
        boolean bandera = true;
        for (UserByCourse item : usuariosPorCurso) {
            if (validarSiRemueveUsuarioDelCurso(item)) {
                System.out.println("si se puede eliminar" + item.getUserId().getFirstName());
                listaRemover.add(item);
            } else {
                bandera = false;
            }
        }
        usuariosPorCurso.removeAll(listaRemover);
        usuariosTemporalesPorCurso.removeAll(listaRemover);
        filtrarUsuarios();
        if (!bandera) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                            " algunos usuarios no se pueden remover del curso porque ya enviaron su evidencia"));
        }
    }

    public void removerUsuarioDelCurso(UserByCourse usuarioCurso) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (validarSiRemueveUsuarioDelCurso(usuarioCurso)) {
            System.out.println("si se puede eliminar" + usuarioCurso.getUserId().getFirstName());
            for (UserByCourse userByCourse : usuariosPorCurso) {
                System.out.println("usuariosPorCurso " + userByCourse.getUserId().getFirstName());
            }
            usuariosPorCurso.remove(usuarioCurso);
//            usuariosTemporalesPorCurso.remove(usuarioCurso); no se por qué no funciona es como si elimara otro objeto
            removerUsuarioDeListaTemporal(usuarioCurso.getUserId());
            for (UserByCourse userByCourse : usuariosTemporalesPorCurso) {
                System.out.println("usuariosTemporalesPorCurso " + userByCourse.getUserId().getFirstName());
            }
        } else {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                            usuarioCurso.getUserId().getFirstName() + " ya envio su evidencia, no se puede remover del curso"));
        }
        filtrarUsuarios();
    }

    public boolean validarSiRemueveUsuarioDelCurso(UserByCourse usuarioCurso) {
        if (usuarioCurso.getAttached() == null && usuarioCurso.getDescription() == null) {
            return true;
        } else {
            return false;
        }
    }

    public void removerUsuarioDeListaTemporal(User usuarioTemporal) {
        for (int i = 0; i < usuariosTemporalesPorCurso.size(); i++) {
            if (Objects.equals(usuariosTemporalesPorCurso.get(i).getUserId().getUserId(), usuarioTemporal.getUserId())) {
                usuariosTemporalesPorCurso.remove(i);
                break;
            }
        }
    }

    public void asignarUsuariosAlEditar() {
        List<UserByCourse> listaUsuariosPorCurso = new ArrayList<>();
        for (User item : usuariosLista) {
            UserByCourse usuarioPorCurso = new UserByCourse();
            usuarioPorCurso.setCourseId(curso);
            usuarioPorCurso.setUserId(item);
            listaUsuariosPorCurso.add(usuarioPorCurso);
        }
        usuariosTemporalesPorCurso.addAll(listaUsuariosPorCurso);
        usuariosLista.removeAll(usuariosLista);

        System.out.println("en usuariosCuros" + usuariosPorCurso.size());
        System.out.println("en usuariosTemporalesPorCurso" + usuariosTemporalesPorCurso.size());
        usuariosPorCurso = new ArrayList<>();
        usuariosPorCurso.addAll(usuariosTemporalesPorCurso);

        filtrarUsuarios();
    }

    public void guardarEdicionCerrarModal() {
        if (!usuariosTemporalesPorCurso.isEmpty()) {
            guardarEdicionUsuariosCurso();
            ocultarModal(5);
            ocultarModal(2);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No puedes dejar un curso sin usuarios"));
        }
    }

    public void guardarEdicionUsuariosCurso() {
        List<User> listaUsuariosAgregados = new ArrayList<>();
        List<User> listaUsuariosRemovidos = new ArrayList<>();
        //En esta parte se verifica los usuarios originales que se encontraban asignados al curso (en la base de datos)
        //y si se identifica un usuario nuevo se agrega
        for (UserByCourse usuariosAgregar : usuariosTemporalesPorCurso) {
            boolean bandera = false;
            for (UserByCourse listaOriginal : curso.getUserByCourseList()) {
                if (listaOriginal.getUserId().equals(usuariosAgregar.getUserId())) {
                    bandera = true;
                }
            }
            if (!bandera) {
                UserByCourse usuarioCurso = new UserByCourse();
                usuarioCurso.setCourseId(curso);
                usuarioCurso.setUserId(usuariosAgregar.getUserId());
                curso.getUserByCourseList().add(usuarioCurso);
                //Se agregan los items para su posterior envio de correo
                listaUsuariosAgregados.add(usuariosAgregar.getUserId());
            }
        }

        //En esta parte se verifica los usuarios originales que se encontraban asignados al curso (en la base de datos)
        //si se identifica un usuario que en la lsita orginal aparecia y en la actual no se elimina
        for (int i = 0; i < curso.getUserByCourseList().size(); i++) {
            boolean bandera = true;
            for (UserByCourse usuariosRemover : usuariosTemporalesPorCurso) {
                if (usuariosRemover.getUserId().equals(curso.getUserByCourseList().get(i).getUserId())) {
                    bandera = false;
                }
            }
            if (bandera) {
                listaUsuariosRemovidos.add(curso.getUserByCourseList().get(i).getUserId());
                userByCourseFacadeLocal.remove(curso.getUserByCourseList().remove(i));
                //Se agregan los usuarios removidos para notificación por correo electrónico
            }
        }
        verificarEstadoCurso();
        cursoFacadeLocal.edit(curso);
        //Aqui rocedemos a enviar el correo
        enviarCorreoAlEditarUsuariosCurso(listaUsuariosAgregados, listaUsuariosRemovidos);

        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Los datos se han modificado correctamente"));
    }

    public void asignarUsuarioCurso(UserByCourse personaCurso) {
        this.usuarioPorCursoActual = personaCurso;
        descargarAdjunto();
    }

    public void calificarUsuarioCurso() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            userByCourseFacadeLocal.edit(usuarioPorCursoActual);
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La calificación se han realizado correctamente"));
            ocultarModal(8);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Ha ocurrido un error al calificar el curso"));
        }
    }

    public String determinarCalificacion(UserByCourse usuarioCurso) {
        String calificacion = "No Aprobado";
        if (usuarioCurso.getGrade() == null) {
            calificacion = "Pendiente";
        } else if (usuarioCurso.getGrade()) {
            calificacion = "Aprobado";
        }
        return calificacion;
    }

    public void descargarAdjunto() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (this.usuarioPorCursoActual.getAttached() != null) {
                String url = this.usuarioPorCursoActual.getAttached();
                String path = fc.getExternalContext().getRealPath("/") + url;
                File f = new File(path);
                InputStream stream = (InputStream) new FileInputStream(f);
                file = new DefaultStreamedContent(stream, URLConnection.guessContentTypeFromStream(stream), f.getName());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserByCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean desabilitarInputSiCursoVencido() {
        boolean bandera = false;
        if (this.curso != null && this.curso.getCourseId() != null) {
            bandera = curso.getFinishDate().before(Date.from(Instant.now()));
        }
        return bandera;
    }

    public void verificarListaCursosTemporal() {
        if (listaCursosTemporal.isEmpty()) {
            listarCursos();
        }
    }

    public void filtrarCurso() {
        System.out.println("entro");
        boolean validacion = true;
        verificarListaCursosTemporal();
        if (busqueda != null && (!busqueda.equals(""))) {
            filtrarPorNombre();
            asignarListaTemoral();
            System.out.println("filtro por nombre: " + busqueda);
            if (fechaInicio != null || fechaFin != null) {
                verificarListaCursosTemporal();
                filtrarPorFechas();
                System.out.println("filtro por nombre y fecha");
            } else {
                validacion = false;
            }
        } else if (fechaInicio != null || fechaFin != null) {
            listarCursos();
            filtrarPorFechas();
            System.out.println("filtro por fecha no sam");

        } else {
            listarCursos();
            System.out.println("listo todo por que  paila");

            validacion = false;
        }

        if (validacion) {
            System.out.println("asignadmos");
            asignarListaTemoral();
        }
    }

    public void filtrarPorNombre() {
        cursos = new ArrayList<>();
        cursos = cursoFacadeLocal.filtrarPorNombre(busqueda, listaCursosTemporal);
    }

    public void filtrarPorFechas() {
        cursos = new ArrayList<>();
        cursos = cursoFacadeLocal.filtrarPorFechas(fechaInicio, fechaFin, listaCursosTemporal);
    }

    public void limpiarFiltro(int opcion) {
        switch (opcion) {
            case 1:
                busqueda = "";
                break;
            case 2:
                fechaInicio = null;
                break;
            case 3:
                fechaFin = null;
                break;
            default:
                break;
        }
        //Esto se realiza para que se reetee la lista y realice el filtro de nuevo
        listaCursosTemporal = new ArrayList<>();
        filtrarCurso();
    }

}
