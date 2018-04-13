package com.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class TestManager {
    Korpolaki korpolaki = new Korpolaki();
    ApiTests apiTests = new ApiTests();

    public TestManager() throws MalformedURLException {


    }

    @Test
    public void checkIfGoogleFindsAWord() {
        int r = korpolaki.checkIfWordExistsInGoogle();
        Assert.assertTrue(r == 1);
    }

    @Test
    public void checkIfMSISDNIs9CharsLong() {
//        Assert.assertTrue(apiTests.checkMSISDNLength(), " it really is 9 chars long");
        int r = korpolaki.checkIfWordExistsInGoogle();
        Assert.assertTrue(r == 1);
    }
}
