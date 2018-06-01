package com.tests.aggregation;

import com.googleapi.GoogleCore;


public class Korpolaki extends GoogleCore {

    public int checkIfWordExistsInGoogle(String word) {
        goToHomePage();
        searchOnPage(word);
        return checkIfWordExists();
    }
}
