package com.tests.aggregation;

import com.googleapi.services.GmailCore;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class GoogleMail {
    GmailCore gmail = new GmailCore();

    public void sendMailFromGoogleMail() throws InvalidKeySpecException, NoSuchAlgorithmException {
        gmail.sendEmailMessage("hate776@gmail.com","topic", "test");
    }

    public void readMailFromGoogleMail() throws InvalidKeySpecException, NoSuchAlgorithmException {
        gmail.readMail();
    }
    public void loginToGoogle() throws InvalidKeySpecException, NoSuchAlgorithmException {
        gmail.goToHomePage();
        gmail.login();
    }
    public void logoutFromGoogle(){
        gmail.logout();
    }
}
