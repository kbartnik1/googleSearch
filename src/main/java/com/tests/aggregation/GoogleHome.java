package com.tests.aggregation;

import com.googleapi.GoogleCore;


public class GoogleHome {

    GoogleCore gc = new GoogleCore();

    public int checkIfWordExistsInGoogle(String wordToSearch) {

        gc.goToHomePage();
        gc.searchOnPage(wordToSearch);
        int returnValue = gc.checkIfWordExists();
        gc.closeDriverConnection();
        return returnValue;

    }

}
