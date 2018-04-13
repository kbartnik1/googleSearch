package com.tests;

import com.googleapi.GoogleCore;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;


public class Korpolaki {

    private final static Logger log = Logger.getLogger(Korpolaki.class);
    GoogleCore driver;
    private final String word = "Truskawka";

    public Korpolaki() throws MalformedURLException {
        driver = new GoogleCore();
    }

    public int checkIfWordExistsInGoogle() {
        driver.goToHomePage();
        driver.searchOnPage(word);
        return driver.checkIfWordExists();
    }
}
