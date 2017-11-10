/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Aht;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface AhtFacadeLocal {

    void create(Aht aht);

    void edit(Aht aht);

    void remove(Aht aht);

    Aht find(Object id);

    List<Aht> findAll();

    List<Aht> findRange(int[] range);

    int count();
    
}
