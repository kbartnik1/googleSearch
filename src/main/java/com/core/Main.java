/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.utils.Config;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

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

    private static void runDockerCompose(int servicesToStart) {
        try {
            log.info("Starting docker services...");
            int quitCondition = 0;
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "-a", "/c","mkdir C:\\docker & docker-compose -f " +
                    System.getProperty("docker.yml.file.location") +
                    "\\\\" +
                    System.getProperty("docker.yml.file.name") +
                    " up -d");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (quitCondition != servicesToStart) {
                line = r.readLine();
                System.out.println(line);
                if (line.contains("done")) {
                    quitCondition++;
                    if (quitCondition == servicesToStart) {
                        log.info(servicesToStart + " Docker service(s) have been started!");
                        log.info("Waiting 15 seconds to let firefox hub and images man up.");
                        Thread.sleep(15000);
                        p.destroy();
                    }
                }
                if(line.contains("error")){
                    log.error("Error starting docker container. Please check out docker logs !");
                    p.destroy();
                    System.exit(-1);
                }
            }
        } catch (Exception ex) {
            System.out.println("Docker initialization failed");
            ex.printStackTrace();
        }

    }

    private static int checkNumberOfServicesToRunInDocker() throws FileNotFoundException {
        int lines = 0;
        File f;
        if(System.getProperty("docker.yml.file.location").contains("%cd%")){
            f = new File(System.getProperty("user.dir") +"\\\\"+ System.getProperty("docker.yml.file.name"));
        }
        else{
            f = new File(System.getProperty("docker.yml.file.location")+"\\\\" + System.getProperty("docker.yml.file.name"));
        }
        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.contains("image:"))
                lines++;
        }
        return lines;
    }
}
