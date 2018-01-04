/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.utilities;

/**
 *
 * @author Santi
 */
public class PasswordGenerator {

    private static final String NUMEROS = "0123456789";
    private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String ESPECIALES = "%/$&#";

    public static String getPassword() {
        return getPassword(10);
    }

    private static String getPassword(int length) {
        return getPassword(NUMEROS + MAYUSCULAS + MINUSCULAS + ESPECIALES, length);
    }

    private static String getPassword(String key, int length) {
        String pswd = "";
        for (int i = 0; i < length; i++) {
            pswd += (key.charAt((int) (Math.random() * key.length())));
        }
        return pswd;
    }
}
