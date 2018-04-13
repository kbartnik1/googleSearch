/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.tests.TestManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author krbk
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);
    public static List<String> methodNames = new ArrayList<>();

    public static void main(String[] args) throws MalformedURLException {
        Main m = new Main();
        try {
            PropertyConfigurator.configure(new FileInputStream("src\\main\\resources\\log4j.properties"));
            log.info("Property files located and loaded");
        } catch (FileNotFoundException ex) {
            log.error("Property files not found. " + ex.getMessage());
        }
        getTestMethodNames();

        TestNGExecutor testNGExecutor = new TestNGExecutor();
        testNGExecutor.runTests();
    }


    public static void getTestMethodNames() {
        Class c = TestManager.class;
        Method[] m = c.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
            methodNames.add(m[i].toString());
            String tmpName = methodNames.get(i);
            String[] tmp = tmpName.split("\\.");
            methodNames.set(i, tmp[tmp.length - 1].substring(0, tmp[tmp.length - 1].length() - 2));
        }
    }


}
