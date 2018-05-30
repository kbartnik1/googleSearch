package com.tests.managers;

import com.tests.implementation.Korpolaki;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class GoogleManager {
    Korpolaki korpolaki = new Korpolaki();


    @Test
    public void checkIfGoogleFindsNotExistingAWord() {
        int r = korpolaki.checkIfWordExistsInGoogle("qetjhquhetuiqheiuthqiuehtiu");
        Assert.assertTrue(r == 1);
    }

    @Test
    public void checkGoogleForPolskiRap() {
        int r = korpolaki.checkIfWordExistsInGoogle("Polski rap");
        Assert.assertTrue(r == 1);
    }

    @AfterClass
    private void cleanUp() {
        korpolaki.closeDriverConnection();
    }

}
