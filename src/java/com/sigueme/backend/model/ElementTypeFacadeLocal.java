/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.ElementType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface ElementTypeFacadeLocal {

    void create(ElementType elementType);

    void edit(ElementType elementType);

    void remove(ElementType elementType);

    ElementType find(Object id);

    List<ElementType> findAll();

    List<ElementType> findRange(int[] range);

    int count();
    
}
