package com.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Scanner;

public class Docker {

    private static final Logger log = Logger.getLogger(Docker.class);

    public static void runDockerCompose(int servicesToStart) {
        try {
            log.info("Starting docker services...");
            int quitCondition = 0;
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "-a", "/c", "mkdir ~\\docker & docker-compose -f " +
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
                        log.info("Waiting 15 seconds to let firefox hub and nodes to man up.");
                        Thread.sleep(15000);
                        p.destroy();
                    }
                }
                if (line.contains("error")) {
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

    public static int checkNumberOfServicesToRunInDocker() throws FileNotFoundException {
        int lines = 0;
        File f;
        if (System.getProperty("docker.yml.file.location").contains("%cd%")) {
            f = new File(System.getProperty("user.dir") + "\\\\" + System.getProperty("docker.yml.file.name"));
        } else {
            f = new File(System.getProperty("docker.yml.file.location") + "\\\\" + System.getProperty("docker.yml.file.name"));
        }
        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.contains("image:"))
                lines++;
        }
        return lines;
    }

    public static void dockerComposeDown() {
        try {
            log.info("Shutting down docker services.");
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "docker-compose -f " + System.getProperty("docker.yml.file.location") + "\\\\" + System.getProperty("docker.yml.file.name") + " down");
            Process p = pb.start();
            p.waitFor();
            p.destroy();
            log.info("Docker services have been stopped and removed.");
            log.info("You might want to remove " + System.getProperty("user.home") + "\\docker from your machine.");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
