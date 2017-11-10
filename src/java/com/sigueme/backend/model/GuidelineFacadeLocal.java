/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Guideline;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface GuidelineFacadeLocal {

    void create(Guideline guideline);

    void edit(Guideline guideline);

    void remove(Guideline guideline);

    Guideline find(Object id);

    List<Guideline> findAll();

    List<Guideline> findRange(int[] range);

    int count();
    
}
