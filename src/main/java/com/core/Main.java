/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.utils.Config;
import com.utils.PBEEncryption;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.utils.Docker.checkNumberOfServicesToRunInDocker;
import static com.utils.Docker.runDockerCompose;

/**
 * @author krbk
 */
public class Main {

    static{
        System.setProperty("log.Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HH_mm")));
    }
    private static final Config c = new Config();
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
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
