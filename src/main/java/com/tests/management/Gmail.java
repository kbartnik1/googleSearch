package com.tests.management;

import com.tests.aggregation.GoogleMail;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Gmail {

    private static final Logger log = Logger.getLogger(Gmail.class);
    GoogleMail gm = new GoogleMail();

/*    @Test
    public void sendMailFromGoogleMail() {
        try {
            gm.loginToGoogle();
            gm.sendMailFromGoogleMail();
            gm.logoutFromGoogle();
        } catch (Exception e) {
           log.error(e);
            Assert.fail();
        }
    }*/


    @Test
    public void readMailFromGoogleMail() {
        try {
            gm.loginToGoogle();
            do {
                gm.readMailFromGoogleMail();
            }while(true);
        } catch (Exception e) {
            log.error(e);
            Assert.fail();
        }

    }
}

