package com.googleapi;


import com.enums.GoogleErrorDictionary;
import com.seleniumapi.SeleniumCore;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;


/**
 * @author krbk
 */

public class GoogleCore extends SeleniumCore {
    private final By GOOGLE_SEARCH_BOX = By.cssSelector("#lst-ib");
    private final By NO_SEARCH_RESULTS_AT_ALL = By.xpath(".//*[@id='topstuff']/*[contains(.,'Podana fraza')]");
    private final By GOOGLE_SEARCH_INFO_ABOUT_SEARCH = By.xpath(".//p[@id='fprs']");
    private final String GOOGLE_SEARCH_INFO_STRING = ".//p[@id='fprs']/*";
    private final String HOME_PAGE = "https://www.google.com";


    private static final Logger log = Logger.getLogger(GoogleCore.class);


    @Override
    public void login() throws InvalidKeySpecException, NoSuchAlgorithmException {
        log.info("Loging to google HOMEPAGE");
        throw new UnsupportedOperationException("Not yet supported.");

    }

    @Override
    public void logout() {
        log.info("Loging off from google HOMEPAGE");
        throw new UnsupportedOperationException("Not yet supported.");
    }

    @Override
    public void searchOnPage(String searchString) {
        super.sendKeysToInputAndHitReturn(GOOGLE_SEARCH_BOX, searchString);
    }

    @Override
    public void goToHomePage() {
        goToPage(HOME_PAGE);
    }

    public int checkIfWordExists() {
        if (checkIfAndWaitUntilElementExists(GOOGLE_SEARCH_INFO_ABOUT_SEARCH)) {
            List<WebElement> pageElements = getAllObjectsStartingFromRoot(GOOGLE_SEARCH_INFO_STRING);
            for (WebElement element : pageElements) {
                for (GoogleErrorDictionary ged : GoogleErrorDictionary.values()) {
                    if (element.getText().contains(ged.getTextError())) {
                        log.debug("Not good.");
                        log.warn("Word not found, but probably you've misspelled it. GoogleHome tries to help You !");
                        return 0;
                    }
                }
            }
        }
        if (checkIfAndWaitUntilElementExists(NO_SEARCH_RESULTS_AT_ALL)) {
            log.error("Selected word probably does not exist");
            return -1;
        } else {
            log.info("Selected word found");
            return 1;
        }
    }
}
