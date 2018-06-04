package com.tests.management;

import com.tests.aggregation.GoogleHome;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Google {

    GoogleHome google = new GoogleHome();
    private static final Logger log = Logger.getLogger(Google.class);

    @Test
    public void checkIfGoogleFindsNotExistingAWord() {
        try {
            int r = google.checkIfWordExistsInGoogle("qetjhquhetuiqheiuthqiuehtiu");
            Assert.assertTrue(r == -1);
        } catch (Exception e) {
            log.error(e);
            Assert.fail();
        }
    }

    @Test
    public void checkGoogleForPolskiRap() {
        try {
            int r = google.checkIfWordExistsInGoogle("Polski rap");
            Assert.assertTrue(r == 1);
        } catch (Exception e) {
            log.error(e);
            Assert.fail();
        }
    }
}
