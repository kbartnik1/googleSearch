package com.utils;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static void addPropertiesConfigurator(FileInputStream fis) {
        PropertyConfigurator.configure(fis);
    }

    public static void addProperties(FileInputStream fis) {
        try {
            Properties p = new Properties(System.getProperties());
            p.load(fis);
            System.setProperties(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}