/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generators;

import java.util.Random;

/**
 *
 * @author krbk
 */
public class NumberGenerators {

    Random random = new Random();

    public String MSISDN() {
        StringBuilder sb = new StringBuilder();
        sb.append(random.nextInt(9)+1);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
