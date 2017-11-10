/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Adherence;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface AdherenceFacadeLocal {

    void create(Adherence adherence);

    void edit(Adherence adherence);

    void remove(Adherence adherence);

    Adherence find(Object id);

    List<Adherence> findAll();

    List<Adherence> findRange(int[] range);

    int count();
    
}
