/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.utils.Config;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Scanner;

/**
 * @author krbk
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Config.addPropertiesConfigurator(new FileInputStream("src\\main\\resources\\log4j.properties"));
            Config.addProperties(new FileInputStream("src\\main\\resources\\config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    private static void runDockerCompose(int servicesToStart) {
        try {
            log.info("Starting docker services...");
            int quitCondition = 0;
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "docker-compose -f " + System.getProperty("docker.yml.file.location") + System.getProperty("docker.yml.file.name") + " up -d");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (quitCondition != servicesToStart) {
                line = r.readLine();
                if (line.contains("done")) {
                    quitCondition++;
                    if (quitCondition == servicesToStart) {
                        log.info(servicesToStart + " Docker service(s) have been started!");
                        Thread.sleep(3000);
                        p.destroy();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Docker initialization failed");
            ex.printStackTrace();
        }

    }

    private static int checkNumberOfServicesToRunInDocker() throws FileNotFoundException {
        int lines = 0;
        File f = new File(System.getProperty("docker.yml.file.location") + System.getProperty("docker.yml.file.name"));
        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.contains("image:"))
                lines++;
        }
        return lines;
    }
}
