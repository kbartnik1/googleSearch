/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seleniumapi;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;


/*
 * @author krbk
 */
public abstract class SeleniumCore {
    private int counter = 0;
    private final int MAX_TRIES = 2;
    private static final Logger log = Logger.getLogger(SeleniumCore.class);
    protected WebDriver driver;

    public SeleniumCore() {
        try {
            if (System.getProperty("mode.remote").equals("false")) {
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
            } else {
                FirefoxOptions cap = new FirefoxOptions();
                cap.setCapability("version", System.getProperty("remote.browserVersion"));
                cap.setCapability("platform", System.getProperty("remote.platform"));
                cap.setCapability("browserName", System.getProperty("remote.browserName"));
                String s = System.getProperty("remote.URL");
                driver = new RemoteWebDriver(new URL(s), cap);
            }
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToPage(String url) {
        log.info("Going to page: " + url);
        driver.get(url);
    }

    public String getText(String vLocator) {
        String text = driver.findElement(By.cssSelector(vLocator)).getText();
        log.info(vLocator + "'s text is following: " + text);
        return text;
    }

    public void sendText(By vLocator, String text) {
        log.info("Sending " + text + " to object " + vLocator.toString());
        driver.findElement(vLocator).clear();
        driver.findElement(vLocator).sendKeys(text);
    }

    public void sendKey(By webElement, Keys k) {
        log.info("Sending " + k.name() + " key to " + webElement.toString());
        driver.findElement(webElement).sendKeys(k);
    }

    public boolean checkIfAndWaitUntilElementExists(By vLocator) {
        counter = 0;
        do {
            try {
                log.debug("Checking for " + vLocator + " object on page");
                Thread.sleep(1000);
                if (!driver.findElements(vLocator).isEmpty()) {
                    log.info("Object " + vLocator + " is present on page");
                    return true;
                }
            } catch (InterruptedException ex) {
                log.error("Error waiting for object " + vLocator);
                ex.printStackTrace();
            }
            counter++;
        } while (counter < MAX_TRIES);
        log.error("Object " + vLocator + " not found on " + getURL() + " page");
        return false;
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }

    public List<WebElement> getAllObjectsStartingFromRoot(String vLocator) {
        return driver.findElements(By.xpath(vLocator));
    }

    protected abstract void searchOnPage(String searchString);

    protected abstract void goToHomePage();

    protected abstract void login() throws UnsupportedEncodingException;

    public void closeDriverConnection() {
        log.warn("Warning ! Closing driver's connection...");
        driver.quit();
    }
}
