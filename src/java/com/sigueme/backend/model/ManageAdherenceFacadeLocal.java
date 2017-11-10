/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.ManageAdherence;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface ManageAdherenceFacadeLocal {

    void create(ManageAdherence manageAdherence);

    void edit(ManageAdherence manageAdherence);

    void remove(ManageAdherence manageAdherence);

    ManageAdherence find(Object id);

    List<ManageAdherence> findAll();

    List<ManageAdherence> findRange(int[] range);

    int count();
    
}
