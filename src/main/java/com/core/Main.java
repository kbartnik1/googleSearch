/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.utils.Config;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.Scanner;


/**
 * @author krbk
 */
public class Main {

    static {
        try {
            final Config cfg = new Config(new FileInputStream("src\\main\\resources\\log4j.properties"), new FileInputStream("src\\main\\resources\\config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final Logger log = Logger.getLogger(Main.class);
    private static int DOCKERSERVICES = 0;
    public static void main(String[] args) {
        if (System.getProperty("mode.remote").equals("true")) {
            runDockerCompose(checkNumberOfServicesToRunInDocker());
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
                        p.destroy();
                    }
                }
            }
            boolean exitCondition;
            do{
                exitCondition = checkIfNodesAreReadyToGo();
                log.info("Firefox nodes are not registered yet. Stay tuned.");
                if(exitCondition)
                    return;
                Thread.sleep(1000);
            }while(!exitCondition);

        } catch (Exception ex) {
            log.fatal("Docker initialization failed");
            ex.printStackTrace();
            log.error(ex);
            System.exit(-1);
        }
    }

    private static int checkNumberOfServicesToRunInDocker() {
        int lines = 0;
        try {
            File f = new File(System.getProperty("docker.yml.file.location") + System.getProperty("docker.yml.file.name"));
            Scanner scanner = new Scanner(f);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                if (s.contains("image:"))
                    lines++;
            }
            DOCKERSERVICES = lines;
        } catch (FileNotFoundException ex) {
            log.fatal("Docker initialization failed");
            ex.printStackTrace();
            log.error(ex);
            System.exit(-1);
        }
        return lines;
    }

    public static boolean checkIfNodesAreReadyToGo() {
        try {
            String hubURL = System.getProperty("remote.HUB.API");
            do {
                //
            }while(!pingHTTP(hubURL,80,30));

            URL url = new URL(hubURL+"/api/hub");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            log.info("response code "+conn.getResponseCode());
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String output = br.readLine();

            JsonObject jsonObject = new JsonParser().parse(output).getAsJsonObject();
            JsonObject slotCounts = (JsonObject) jsonObject.get("slotCounts");
            String freeNodes = slotCounts.get("free").toString();
            String runningNodes = slotCounts.get("total").toString();

            conn.disconnect();
           if(Integer.parseInt(runningNodes) != DOCKERSERVICES-1)
               return false;
            return freeNodes.equals(runningNodes);
        } catch (ProtocolException e) {
            e.printStackTrace();
            log.error(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
        return false;
    }

    public static boolean pingHTTP(String host, int port, int timeout) {
        log.info("Checking socket.");
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host,port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
