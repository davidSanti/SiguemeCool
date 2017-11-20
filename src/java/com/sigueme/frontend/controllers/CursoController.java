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
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    List<User> usuariosPorCursoEditar;
    List<Course> cursos;
    List<UserByCourse> usuariosPorCurso;
    List<GroupCls> listaGrupos;
    List<GroupCls> gruposPersona;
    List<Role> listaRoles;

    private String filtrarEstado;

    @PostConstruct
    public void init() {
        curso = new Course();
        usuariosLista = new ArrayList<>();
        usuariosPorCurso = new ArrayList<>();
        usuariosPorCursoEditar = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        gruposPersona = new ArrayList<>();
        listaRoles = new ArrayList<>();

        listarCursos();
        filtrarEstado = new String();
    }

    public CursoController() {
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

    //Éste método obtiene todos los usuaros asociados a un curso, elimina los registros de la tabla user_by_course y por último elimina el curso seleccionado
    public void eliminarCurso(Course course) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.curso = course;
            List<UserByCourse> lista = userByCourseFacadeLocal.listarUsuariosPorCurso(curso);
            for (UserByCourse item : lista) {
                userByCourseFacadeLocal.remove(item);
            }
            this.cursoFacadeLocal.remove(curso);
            curso = new Course();
            listarCursos();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Eliminado Correctamente"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "No se pudo eliminar"));
        }
    }

    //éste método asigna el curso que está llegando de la interfaz a la variable global de éste controlador llamada curso
    public void editarCurso(Course course, int opcion) {
        this.curso = course;
        if (opcion == 1) {
            devolverUsuariosPorCurso();
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
            this.cursoFacadeLocal.edit(this.curso);
            banderita = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Editado Correctamente"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "No se pudo editar"));
        }
        return banderita;
    }

    /*éste método lo llamamos en la línea 170 de la interfaz de course.xhtml y debido a que devuleve la misma lista de usuarios 
        que se utiliza en el filtro nos sirve para que todos los usuarios se seleccionen por defecto al relizar el filtro
     */
    public List<User> devolverUsuarios() {
        return this.usuariosLista;
    }

    /*Este metodo valida si el curso tiene usuarios asignados, de ser asi registra el curso, asigna los usuarios seleccionados y 
      cierra las ventanas modales que se abrieron anteriormente y limpia el filtro de grupo y rol
     */
    public void asociarUsuarios() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!usuariosLista.isEmpty()) {
                this.cursoFacadeLocal.create(curso);
                for (int i = 0; i < usuariosLista.size(); i++) {
                    UserByCourse userbyCourse = new UserByCourse();
                    userbyCourse.setCourseId(curso);

                    userbyCourse.setUserId(usuariosLista.get(i));

                    userByCourseFacadeLocal.create(userbyCourse);
                }
                ocultarModal(3);
                ocultarModal(1);
                limpiarFiltro();
                listarCursos();

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Registrado Correctamente"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se puede crear el curso hasta que los usuarios se asocien"));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Error al asociar los usuarios al curso"));
        }
    }

    //Éste método devuelve la lista de cursos en orden descendente
    public List<Course> listarCursos() {
        cursos = cursoFacadeLocal.listarCursos();
        return cursos;
    }

    public List<Role> listarRoles() {
        return rolFacadeLocal.findAll();
    }

    public List<GroupCls> listarGrupos() {
//        return grupoFacadeLocal.listarGrupos();
        return grupoFacadeLocal.findAll();
    }

    //Cuando se llame a éste método se verifica si el curso es diferente de null y si es así retorna todos los usuarios que tenga asociados dicho curso
    public List<UserByCourse> devolverUsuariosPorCurso() {
        if (curso != null) {
            usuariosPorCurso.addAll(curso.getUserByCourseList());
        }
        return usuariosPorCurso;
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
                System.out.println("sdsds" + curso.getUserByCourseList().size());
                break;
            default:
                break;
        }
        usuariosPorCurso = new ArrayList<>();
        req.reset(formulario);
    }

    //éste método se utiliza para abrir las ventanas modales
    public void abrirModal(int opciones) {
        RequestContext request = RequestContext.getCurrentInstance();
        switch (opciones) {
            case 1:
                request.execute("PF('asociarUsuarios').show()");
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
        List<User> algo = new ArrayList<>();
        for (UserByCourse item : usuariosPorCurso) {
            algo.add(item.getUserId());
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

    }

    //Este metodo se ultiliza pára realizar la limpieza de los filtros de Grupo y Rol que se encuentran en el modal de Asociar Usuarios
    public void limpiarFiltro() {
        usuariosLista = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        listaRoles = new ArrayList<>();

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
        if (gruposPersona.size() > 0) {
            usuariosPorCurso = cursoFacadeLocal.filtrarUsuariosPorGrupo(gruposPersona, curso);
        } else {
            usuariosPorCurso = new ArrayList<>();
            devolverUsuariosPorCurso();
        }
    }

    public void removerTodosUsuariosDelCurso() {
        List<UserByCourse> listaRemover = new ArrayList<>();
        for (UserByCourse item : usuariosPorCurso) {
            if (validarSiRemueveUsuarioDelCurso(item)) {
                System.out.println("si se puede eliminar" + item.getUserId().getFirstName());
                listaRemover.add(item);
            }
        }
        usuariosPorCurso.removeAll(listaRemover);
        filtrarUsuarios();
    }

    public void removerUsuarioDelCurso(UserByCourse usuarioCurso) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (validarSiRemueveUsuarioDelCurso(usuarioCurso)) {
            System.out.println("si se puede eliminar" + usuarioCurso.getUserId().getFirstName());
            usuariosPorCurso.remove(usuarioCurso);
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

    public void asignarUsuariosAlEditar() {
        List<UserByCourse> listaUsuariosPorCurso = new ArrayList<>();
        for (User item : usuariosLista) {
            UserByCourse usuarioPorCurso = new UserByCourse();
            usuarioPorCurso.setCourseId(curso);
            usuarioPorCurso.setUserId(item);
            listaUsuariosPorCurso.add(usuarioPorCurso);
        }
        usuariosPorCurso.addAll(listaUsuariosPorCurso);
        usuariosLista.removeAll(usuariosLista);
        filtrarUsuarios();
    }

    public void guardarEdicionUsuariosCurso() {

        for (UserByCourse ss : usuariosPorCurso) {
            System.out.println("ss" + ss.getUserId().getFirstName());
        }
        //En esta parte se verifica los usuarios originales que se encontraban asignados al curso (en la base de datos)
        //y si se identifica un usuario nuevo se agrega
        for (UserByCourse usuariosAgregar : usuariosPorCurso) {
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
            }
        }
        System.out.println("tamaño agregar" + curso.getUserByCourseList().size());

        //En esta parte se verifica los usuarios originales que se encontraban asignados al curso (en la base de datos)
        //si se identifica un usuario que en la lsita orginal aparecia y en la actual no se elimina
        for (int i = 0; i < curso.getUserByCourseList().size(); i++) {
            boolean bandera = true;
            for (UserByCourse usuariosRemover : usuariosPorCurso) {
                if (usuariosRemover.getUserId().equals(curso.getUserByCourseList().get(i).getUserId())) {
                    bandera = false;
                }
            }
            if (bandera) {
                System.out.println("remove" + curso.getUserByCourseList().get(i).getUserId().getFirstName());
                userByCourseFacadeLocal.remove(curso.getUserByCourseList().remove(i));
            }
        }
        System.out.println("tamaño remove" + curso.getUserByCourseList().size());
        cursoFacadeLocal.edit(curso);
        System.out.println("super");
    }

}