package com.utils;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


public class Config {

    public Config(FileInputStream fis1, FileInputStream fis2){
            addPropertiesConfigurator(fis1);
            addProperties(fis2);
    }
    static {
        System.setProperty("log.Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HH_mm")));
    }

    public void addPropertiesConfigurator(FileInputStream fis) {
        PropertyConfigurator.configure(fis);
    }

    public void addProperties(FileInputStream fis) {
        try {
            Properties p = new Properties(System.getProperties());
            p.load(fis);
            System.setProperties(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}