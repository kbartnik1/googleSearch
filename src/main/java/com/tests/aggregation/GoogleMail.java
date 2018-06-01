package com.tests.aggregation;

import com.googleapi.services.GmailCore;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class GoogleMail {
    GmailCore gmail = new GmailCore();

    public void sendMailFromGoogleMail() throws InvalidKeySpecException, NoSuchAlgorithmException {
        gmail.goToHomePage();
        gmail.login();
        gmail.sendEmailMessage("hate776@gmail.com", "test");
        gmail.logout();
    }

    public void readMailFromGoogleMail() throws InvalidKeySpecException, NoSuchAlgorithmException {
        gmail.goToHomePage();
        gmail.login();
        gmail.readMail();
        gmail.logout();
    }
}
