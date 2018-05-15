package com.tests;

import com.googleapi.GoogleCore;
import org.apache.log4j.Logger;


public class Korpolaki extends GoogleCore {

    public int checkIfWordExistsInGoogle(String word) {
        goToHomePage();
        searchOnPage(word);
        return checkIfWordExists();
    }
}
