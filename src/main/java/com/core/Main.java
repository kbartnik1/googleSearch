/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author krbk
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);


    public static void main(String[] args){
        try {
            PropertyConfigurator.configure(new FileInputStream("src\\main\\resources\\log4j.properties"));
            log.info("Property files located and loaded");
        } catch (FileNotFoundException ex) {
            log.error("Property files not found. " + ex.getMessage());
        }
        TestNGExecutor testNGExecutor = new TestNGExecutor();
        testNGExecutor.runTests();
    }

}
