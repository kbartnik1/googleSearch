package com.tests.management;

import com.tests.aggregation.GoogleHome;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Google {
    GoogleHome google = new GoogleHome();

    @Test
    public void checkIfGoogleFindsNotExistingAWord() {
        try {
            int r = google.checkIfWordExistsInGoogle("qetjhquhetuiqheiuthqiuehtiu");
            Assert.assertTrue(r == -1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
/*
    @Test
    public void checkGoogleForPolskiRap() {
        try {
            int r = google.checkIfWordExistsInGoogle("Polski rap");
            Assert.assertTrue(r == 1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }*/
}
