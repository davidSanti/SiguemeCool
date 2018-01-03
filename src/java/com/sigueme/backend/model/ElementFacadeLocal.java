/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Element;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface ElementFacadeLocal {

    void create(Element element);

    void edit(Element element);

    void remove(Element element);

    Element find(Object id);

    List<Element> findAll();

    List<Element> findRange(int[] range);

    int count();

    List<Element> listarElementosMsc();

    List<Element> verificarIdentification(String identification, int option);
}
