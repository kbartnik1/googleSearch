/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seleniumapi;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/*
 * @author krbk
 */
public abstract class SeleniumCore {
    private int counter = 0;
    private final int MAX_TRIES = 3;
    private static final Logger log = Logger.getLogger(SeleniumCore.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

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
            wait = new WebDriverWait(driver, 60);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract void login() throws InvalidKeySpecException, NoSuchAlgorithmException;

    public abstract void logout();

    public abstract void searchOnPage(String searchString);

    public abstract void goToHomePage();

    protected void goToPage(String url) {
        log.info("Going to page: " + url);
        driver.get(url);
    }

    protected String getText(By vLocator) {
        checkIfAndWaitUntilElementExists(vLocator);
        String text = driver.findElement(vLocator).getText();
        log.info(vLocator + "'s text is following: " + text);
        return text;
    }

    protected void sendText(By vLocator, String text) {
        log.info("Sending " + text + " to object " + vLocator.toString());
        checkIfAndWaitUntilElementExists(vLocator);
        driver.findElement(vLocator).clear();
        driver.findElement(vLocator).sendKeys(text);
    }

    protected void sendKey(By vLocator, Keys k) {
        log.info("Sending " + k.name() + " key to " + vLocator.toString());
        checkIfAndWaitUntilElementExists(vLocator);
        driver.findElement(vLocator).sendKeys(k);
    }

    protected boolean checkIfAndWaitUntilElementExists(By vLocator) {
        counter = 0;
        do {
            try {
                log.debug("Checking for " + vLocator + " object on page");
                Thread.sleep(1000);
                if (!driver.findElements(vLocator).isEmpty()) {
                    if (driver.findElements(vLocator).size() == 1) {
                        WebElement element = driver.findElement(vLocator);
                        log.info("Object " + vLocator + " is present on page");
                        if (!isVisibleAndDisplayed(vLocator)) {
                            counter++;
                            continue;
                        } else
                            scrollToCenterOfObject(element);
                        return true;
                    } else {
                        log.warn("Found more than one object");
                        rollThroughFoundObjects(vLocator);
                    }
                }counter++;
            } catch (InterruptedException ex) {
                log.error("Error waiting for object " + vLocator);
                ex.printStackTrace();
            }
        } while (counter < MAX_TRIES);
        log.error("Object " + vLocator + " not found on " + getURL() + " page");
        return false;
    }

    protected String getURL() {
        return driver.getCurrentUrl();
    }

    protected List<WebElement> getAllObjectsStartingFromRoot(String vLocator) {
        return driver.findElements(By.xpath(vLocator));
    }

    public void closeDriverConnection() {
        log.warn("Warning ! Closing driver's connection...");
        driver.quit();
    }

    protected void scrollPageByXPixels(int pixelsToScroll) {
        log.info("Scrolling the page by " + pixelsToScroll + " pixels.");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + pixelsToScroll + ")");
    }

    protected void scrollToCenterOfObject(WebElement ele) {
        log.debug("Scrolling the page to make sure it is visible on the page.");
        /*((JavascriptExecutor) driver).executeScript("window.scrollTo(" + ele.getLocation().x + "," + ele.getLocation().y + ")");*/
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
    }

    protected void sendKeysToInputAndHitReturn(By vLocator, String text) {
        sendText(vLocator, text);
        sendKey(vLocator, Keys.RETURN);
    }

    protected void waitAndClickOnElement(By vLocator) {
        log.info("Clicking on " + vLocator + " webelement");
        checkIfAndWaitUntilElementExists(vLocator);
        driver.findElement(vLocator).click();
    }

    protected boolean isVisibleAndDisplayed(By vLocator) {
        log.info("Checking if element is displayed and enabled on the page.");
        WebElement element = driver.findElement(vLocator);
        boolean exitCondition = element.isDisplayed() && element.isEnabled();
        log.trace("Exit condition = " + exitCondition);
        return exitCondition;
    }

    private void rollThroughFoundObjects(By vLocator) {
        log.trace("Trying to remove one of webelements that is invisible");
        List<WebElement> elems = driver.findElements(vLocator);
        for (WebElement e : elems) {
            if (!e.isDisplayed() || !e.isEnabled()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].parentNode.removeChild(arguments[0])", e);
                return;
            }
        }
        return;
    }
}
