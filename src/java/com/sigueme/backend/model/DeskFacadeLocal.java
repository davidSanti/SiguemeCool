/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Desk;
import com.sigueme.backend.entities.GroupCls;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface DeskFacadeLocal {

    void create(Desk desk);

    void edit(Desk desk);

    void remove(Desk desk);

    Desk find(Object id);

    List<Desk> findAll();

    List<Desk> findRange(int[] range);

    int count();

    Desk buscarPorSerialCode(String serialCode);
    
    List<String> buscarPorSerialCodeAutoComplete(String serialCode);
    
    List<Desk> listarPuestosPorGrupo(List<GroupCls> grupo);
}
