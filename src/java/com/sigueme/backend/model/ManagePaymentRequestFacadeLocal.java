/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.ManagePaymentRequest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author RobayoDa
 */
@Local
public interface ManagePaymentRequestFacadeLocal {

    void create(ManagePaymentRequest managePaymentRequest);

    void edit(ManagePaymentRequest managePaymentRequest);

    void remove(ManagePaymentRequest managePaymentRequest);

    ManagePaymentRequest find(Object id);

    List<ManagePaymentRequest> findAll();

    List<ManagePaymentRequest> findRange(int[] range);

    int count();
    
}
