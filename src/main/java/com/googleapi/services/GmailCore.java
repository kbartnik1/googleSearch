package com.googleapi.services;

import com.googleapi.GoogleCore;
import com.utils.PBEEncryption;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class GmailCore extends GoogleCore {

    private final String HOME_PAGE = "https://gmail.com";
    private final By LOGIN_INPUT = By.cssSelector("#identifierId");
    private final By PASSWORD_INPUT = By.cssSelector("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input");
    private final By GMAIL_SEARCHBOX = By.cssSelector("");

    private static final Logger log = Logger.getLogger(GmailCore.class);

    @Override
    public void login() throws InvalidKeySpecException, NoSuchAlgorithmException {
        log.info("Loging to Google's Gmail service.");
        String login = JOptionPane.showInputDialog(null, "Login: ", "Please input your login");
        String pwd = JOptionPane.showInputDialog(null, "Password: ", "Please input your pwd");
        if(validatePassword(pwd)){
            sendKeysToInputAndHitReturn(LOGIN_INPUT, login);
            sendKeysToInputAndHitReturn(PASSWORD_INPUT, pwd);

        }
            else{
            throw new SecurityException("Incorrect password. Can't log in to service");
        }

    }

    @Override
    public void logout() {
        log.info("Logging off from Gmail service.");

    }

    @Override
    public void goToHomePage() {
        log.info("Going to Gmail's home page.");
        goToPage(HOME_PAGE);
    }

    @Override
    public void searchOnPage(String searchString) {
        super.sendKeysToInputAndHitReturn(GMAIL_SEARCHBOX, searchString);
    }
    public void sendEmailMessage(String sendTo, String mailBody) {
        log.info("Trying to send email message to " + sendTo);

    }

    public void readMail() {
        log.info("Opening first available email message.");
    }

    public void readMail(int mailNumber) {
        log.info("Trying to open " + mailNumber + ". email message");
    }

    public void readMail(String mailTitle) {
        log.info("Trying to open  email message containing \"" + mailTitle + "\" in the title.");
    }

    private boolean validatePassword(String pwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        log.info("Validating password against it's hash.");
        PBEEncryption pbe = new PBEEncryption(Integer.parseInt(System.getProperty("security.password.google.iterations")));
        pbe.setSalt(System.getProperty("security.password.google.salt").getBytes());
        return pbe.createHash(pwd).equals(System.getProperty("security.password.google.hash"));
    }
}
