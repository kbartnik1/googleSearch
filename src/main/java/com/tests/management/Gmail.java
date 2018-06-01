package com.tests.management;

import com.googleapi.services.GmailCore;
import com.tests.aggregation.ApiTests;
import com.tests.aggregation.GoogleMail;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Gmail {

    GoogleMail gm = new GoogleMail();

    @Test
    public void sendMailFromGoogleMail() {
        try {
            gm.sendMailFromGoogleMail();
        }catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }
  /*  @Test
    public void readMailFromGoogleMail(){
        try {
            gm.readMailFromGoogleMail();
        }catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail();
        }

    }*/
}

