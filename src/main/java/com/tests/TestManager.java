package com.tests;

import com.core.TestNGExecutorListener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class TestManager {
    Korpolaki korpolaki = new Korpolaki();
    ApiTests apiTests = new ApiTests(); // does not use driver so it is not required to destroy it afterwards

    @Test
    public void checkIfGoogleFindsNotExistingAWord() {
        int r = korpolaki.checkIfWordExistsInGoogle("qetjhquhetuiqheiuthqiuehtiu");
        Assert.assertTrue(r == 1);
    }

    @AfterClass
    private void cleanUp() {
        korpolaki.closeDriverConnection();
    }


    @Test
    public void checkGoogleForPolskiRap() {
        int r = korpolaki.checkIfWordExistsInGoogle("Polski rap");
        Assert.assertTrue(r == 1);
    }

    @Test
    public void simpleNumberCheck() {
        Assert.assertTrue(apiTests.checkMSISDNLength(), " it really is 9 chars long");
    }
}
