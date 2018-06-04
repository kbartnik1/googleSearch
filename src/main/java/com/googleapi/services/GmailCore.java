package com.googleapi.services;

import com.googleapi.GoogleCore;
import com.utils.PBEEncryption;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class GmailCore extends GoogleCore {

    private final String HOME_PAGE = "https://gmail.com";
    private final By LOGIN_INPUT = By.cssSelector("#identifierId");
    private final By PASSWORD_INPUT = By.cssSelector("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input");
    private final By SIGN_OUT_OPTIONS = By.xpath("//a[contains(@href,'SignOutOptions')]");
    private final By LOGOUT_BUTTON = By.xpath("//a[contains(@href,'Logout')]");
    private final By CREATE_NEW_MSG_BUTTON = By.xpath("//div[contains(@style,'user-select')]//div[contains(@tabindex,'0') and contains(@style,'user') and contains(@role,'button')]");
    private final By SEND_TO_INPUT = By.xpath("//div/textarea[contains(@name,'to')]");
    private final By MESSAGE_SUBJECT = By.xpath("//div/input[contains(@name,'subjectbox')]");
    private final By MESSAGE_BODY = By.xpath("//div[contains(@class,'editable') and contains(@g_editable,'true')]");
    private final By SEND_MAIL = By.xpath("//tbody//div[contains(@role,'button') and contains(@data-tooltip,'Ctrl+Enter')]");
    private final By FIRST_UNREADEN_MAIL = By.xpath("//tbody/tr[1]/td//*//span/b[1]");
    private final By RETURN_TO_INBOX = By.xpath("//a[contains(@href,'#inbox')]/span");
    private final By GMAIL_SEARCH_INPUT = By.xpath("//tbody//input[contains(@type,'text') and contains(@aria-label,'S')]");

    private static final Logger log = Logger.getLogger(GmailCore.class);

    @Override
    public void login() throws InvalidKeySpecException, NoSuchAlgorithmException {
        log.info("Loging to Google's Gmail service.");
        JLabel jUserName = new JLabel("User Name");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField(128);
        Object[] ob = {jUserName, userName, jPassword, password};
        int OK = JOptionPane.showConfirmDialog(null, ob, "Please input credentials to log into gmail service.", JOptionPane.OK_CANCEL_OPTION);
        if (validatePassword(String.valueOf(((JPasswordField) password).getPassword())) && OK == JOptionPane.OK_OPTION) {
            sendKeysToInputAndHitReturn(LOGIN_INPUT, userName.getText());
            sendKeysToInputAndHitReturn(PASSWORD_INPUT, String.valueOf(((JPasswordField) password).getPassword()));
        } else {
            throw new SecurityException("Incorrect password, or action cancelled by user. Can't log in to service");
        }
    }

    @Override
    public void logout() {
        log.info("Logging off from Gmail service.");
        waitAndClickOnElement(SIGN_OUT_OPTIONS);
        waitAndClickOnElement(LOGOUT_BUTTON);
    }

    @Override
    public void goToHomePage() {
        log.info("Going to Gmail's home page.");
        goToPage(HOME_PAGE);
    }

    @Override
    public void searchOnPage(String searchString) {
        sendKeysToInputAndHitReturn(GMAIL_SEARCH_INPUT, searchString);
    }
    @Override
    protected void sendText(By vLocator, String text) {
        log.info("Sending text to object " + vLocator.toString());
        checkIfAndWaitUntilElementExists(vLocator);
        driver.findElement(vLocator).clear();
        driver.findElement(vLocator).sendKeys(text);
    }

    public void sendEmailMessage(String sendTo, String messageSubject, String mailBody) {
        log.info("Trying to send email message to " + sendTo);
        waitAndClickOnElement(CREATE_NEW_MSG_BUTTON);
        sendText(SEND_TO_INPUT, sendTo);
        sendText(MESSAGE_SUBJECT, messageSubject);
        sendText(MESSAGE_BODY, mailBody);
        waitAndClickOnElement(SEND_MAIL);
    }

    public void readMail() {
        log.info("Opening first unreaden available email message.");
        searchOnPage("label:unread");
        waitAndClickOnElement(FIRST_UNREADEN_MAIL);
        returnBackToMainView();
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
    private void returnBackToMainView(){
        log.info("Going back to Gmail's main screen");
        waitAndClickOnElement(RETURN_TO_INBOX);
    }
}
