package com.utils;

public enum ConfigFiles {
    LOG4J("log4j.properties"),
    APPLICATION("config.properties");


    String file;

    ConfigFiles(String file_) {
        this.file = file_;
    }
}

