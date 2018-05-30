package com.utils;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;


public class Config {

    private static final Logger log = Logger.getLogger(Config.class);


    public Config() {
        basicConfig();
    }

    public void basicConfig() {
        log.debug("Configuration started.");
        addConfigFromFiles();
    }

    public void addConfigFromFiles() {
        log.debug("Loading configuration...");
        try {
            for (ConfigFiles cf : ConfigFiles.values()) {
                Properties p = new Properties(System.getProperties());
                InputStream is = Config.class.getClassLoader().getResourceAsStream(cf.file);
                if (is != null)
                    p.load(is);
                System.setProperties(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}