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
import com.sigueme.backend.model.GroupClsFacadeLocal;
import com.sigueme.backend.model.UserByCourseFacadeLocal;
import com.sigueme.backend.model.UserFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.primefaces.model.chart.Axis;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;

/**
 *
 * @author Santi
 */
@Named(value = "cursoChartController")
@ViewScoped
public class CursoChartController implements Serializable {

    @EJB
    private CourseFacadeLocal cursoFacadeLocal;
    @EJB
    private UserByCourseFacadeLocal userByCourseFacadeLocal;
    @EJB
    private UserFacadeLocal userFacadeLocal;
    @EJB
    private GroupClsFacadeLocal groupFacadeLocal;

    private Course curso;
    private List<User> listaUsuariosSinEvidencia;
    List<GroupCls> gruposPersona;
    private BarChartModel barModel;

    private String nombrePersona;

    @PostConstruct
    private void init() {
        curso = new Course();
        listaUsuariosSinEvidencia = new ArrayList<>();
        listarUsuarios();
        gruposPersona = new ArrayList<>();
        barModel = new BarChartModel();
        nombrePersona = "";
    }

    public CursoChartController() {
    }

    public Course getCurso() {
        return curso;
    }

    public void setCurso(Course curso) {
        System.out.println("marei" + curso.getCourseName());
        this.curso = curso;
    }

    public List<User> getListaUsuariosSinEvidencia() {
        return listaUsuariosSinEvidencia;
    }

    public void setListaUsuariosSinEvidencia(List<User> listaUsuariosSinEvidencia) {
        this.listaUsuariosSinEvidencia = listaUsuariosSinEvidencia;
    }

    public List<GroupCls> getGruposPersona() {
        return gruposPersona;
    }

    public void setGruposPersona(List<GroupCls> gruposPersona) {
        this.gruposPersona = gruposPersona;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public void listarUsuarios() {
        if (this.curso != null && this.curso.getCourseId() != null) {
            listaUsuariosSinEvidencia = new ArrayList<>();
            listaUsuariosSinEvidencia = userByCourseFacadeLocal.listarUsuariosSinEvidencia(curso);
            listarGrupos();
            generarBarModel();
        }
    }

    public List<GroupCls> listarGrupos() {
        if (curso != null && curso.getCourseId() != null) {
            return groupFacadeLocal.listarGruposPorPersonasDelCurso(curso);
        } else {
            return groupFacadeLocal.findAll();
        }
    }

    public void filtrarGrafica() {
        if (gruposPersona.size() > 0) {
            generarGrafica(gruposPersona);
        }
    }

    public void filtrarPersonas() {
        if (gruposPersona.size() > 0) {
            filtrarPersonasPorGrupo();
            if (!nombrePersona.equals("")) {
                buscarPersonaPorNombre();
            }
            filtrarGrafica();
        } else {
            listarUsuarios();
            buscarPersonaPorNombre();
        }

    }

    public void filtrarPersonasPorGrupo() {
        List<User> listaTemporal = new ArrayList<>();
        listarUsuarios();
        for (User item : listaUsuariosSinEvidencia) {
            for (GroupCls grupo : gruposPersona) {
                if (item.getGroupId().equals(grupo) && item.getUserStatusId().getUserStatusId() != 2) {
                    listaTemporal.add(item);
                }
            }
        }

        listaUsuariosSinEvidencia = new ArrayList<>();
        listaUsuariosSinEvidencia.addAll(listaTemporal);
    }

    public void buscarPersonaPorNombre() {
        List<User> listaTemporal = new ArrayList<>();
        for (User item : listaUsuariosSinEvidencia) {
            if ((item.getFirstName().contains(nombrePersona) || item.getLastName().contains(nombrePersona))
                    && item.getUserStatusId().getUserStatusId() != 2) {
                listaTemporal.add(item);
            }
        }

        listaUsuariosSinEvidencia = new ArrayList<>();
        listaUsuariosSinEvidencia.addAll(listaTemporal);
    }

    private void generarBarModel() {
        generarGrafica(groupFacadeLocal.findAll());
    }

    public int contarPersonasPorGrupo(List<UserByCourse> lista, GroupCls grupo) {
        int cantidad = 0;
        for (UserByCourse item : lista) {
            if (Objects.equals(item.getUserId().getGroupId().getGroupId(), grupo.getGroupId())) {
                cantidad += 1;
            }
        }
        return cantidad;
    }

    private void generarGrafica(List<GroupCls> grupos) {
        barModel = new BarChartModel();
        boolean hayDatos = false;
        List<UserByCourse> listaTotal = userByCourseFacadeLocal.listarUsuariosPorCurso(curso);
        List<GroupCls> listaGrupos = grupos;

        BarChartSeries totalUsuarios = new BarChartSeries();
        totalUsuarios.setLabel("Total Cuenta");
        //Se realiza un ciclo con todos los grupos que hay, y se cuentan los usuarios de ese grupo que están asignados al curso
        for (GroupCls grupo : listaGrupos) {
            int cantidad = contarPersonasPorGrupo(listaTotal, grupo);
            if (cantidad > 0) {
                hayDatos = true;
                totalUsuarios.set(grupo.getGroupName(), cantidad);
            }
        }
//        totalUsuarios.set("grupo", 3);
//        totalUsuarios.set("after ah", 5);
//        totalUsuarios.set("otra1", 4);
//        totalUsuarios.set("otra2", 7);
//        totalUsuarios.set("otra5", 9);
//        totalUsuarios.set("otra6", 9);
//        totalUsuarios.set("otra7", 4);
//        totalUsuarios.set("otra8", 7);
//        totalUsuarios.set("otra9", 3);

        barModel.addSeries(totalUsuarios);

        List<User> usuariosSinEvidencia = userByCourseFacadeLocal.listarUsuariosSinEvidencia(curso);
        List<UserByCourse> usuariosCursoSinEvidencia = new ArrayList<>();

//        En ese ciclo se llena la lista de UserByCourse con los usuarios del curso, para posteriormente enviarle esa lista
//          al método de "contarPersonasProGrupo" que trabaja con una lista de UserByCourse y no de usuarios
        for (User usuario : usuariosSinEvidencia) {
            UserByCourse usuarioCurso = new UserByCourse();
            usuarioCurso.setUserId(usuario);
            usuariosCursoSinEvidencia.add(usuarioCurso);
        }

        //Esta serie contiene el número de personas por grupo que ya subieron la evidencia del curso
        BarChartSeries totalConEvidencia = new BarChartSeries();
        totalConEvidencia.setLabel("Con evidencia");
        for (GroupCls grupo : listaGrupos) {
            int cantidadTotal = contarPersonasPorGrupo(listaTotal, grupo);
            int cantidadSinEvidencia = contarPersonasPorGrupo(usuariosCursoSinEvidencia, grupo);
            //al tene el total de persona de un grupo asignado a un curso y
            // el total de persona de ese grupo que no han enviado la evidencia
            //sabemos que la resta nos da el número de personas que ya subieron la evidencia del curso.
            int cantidad = cantidadTotal - cantidadSinEvidencia;
            if (cantidadTotal > 0) {
                totalConEvidencia.set(grupo.getGroupName(), cantidad);
            }
        }
        barModel.addSeries(totalConEvidencia);

        barModel.setTitle("Rendimiento Curso");
        barModel.setLegendPosition("ne");
        barModel.setMouseoverHighlight(true);
        barModel.setShowDatatip(false);
        barModel.setShowPointLabels(true);
        Axis yAxis = barModel.getAxis(AxisType.Y);
    }

}
