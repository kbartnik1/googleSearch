/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.utils.Config;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

import static com.utils.Docker.*;

/**
 * @author krbk
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);
    private static final Config c = new Config();

    public static void main(String[] args) {
        if (System.getProperty("mode.remote").equals("true")) {
            try {
                runDockerCompose(checkNumberOfServicesToRunInDocker());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        TestNGExecutor testNGExecutor = new TestNGExecutor();
        testNGExecutor.runTests();
    }
}
