/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.TaxiRateByUser;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface TaxiRateByUserFacadeLocal {

    void create(TaxiRateByUser taxiRateByUser);

    void edit(TaxiRateByUser taxiRateByUser);

    void remove(TaxiRateByUser taxiRateByUser);

    TaxiRateByUser find(Object id);

    List<TaxiRateByUser> findAll();

    List<TaxiRateByUser> findRange(int[] range);

    int count();
    
}
