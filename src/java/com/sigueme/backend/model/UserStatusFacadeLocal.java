/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.UserStatus;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ZamudioL
 */
@Local
public interface UserStatusFacadeLocal {

    void create(UserStatus userStatus);

    void edit(UserStatus userStatus);

    void remove(UserStatus userStatus);

    UserStatus find(Object id);

    List<UserStatus> findAll();

    List<UserStatus> findRange(int[] range);

    int count();

    List<UserStatus> listarEstados();

}
