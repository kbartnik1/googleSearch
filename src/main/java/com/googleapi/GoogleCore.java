package com.googleapi;


import com.enums.GoogleErrorDictionary;
import com.seleniumapi.SeleniumCore;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    public void searchOnPage(String searchString) {
        sendText(GOOGLE_SEARCH_BOX, searchString);
        sendKey(GOOGLE_SEARCH_BOX, Keys.RETURN);
    }


    @Override
    public void goToHomePage() {
        goToPage(HOME_PAGE);
    }

    @Override
    protected void login() throws UnsupportedEncodingException {
        log.info("Loging to google services");
        log.warn(URLDecoder.decode("String to decode", "UTF-8"));
    }

    public int checkIfWordExists() {
        if (checkIfAndWaitUntilElementExists(GOOGLE_SEARCH_INFO_ABOUT_SEARCH)) {
            List<WebElement> lista = getAllObjectsStartingFromRoot(GOOGLE_SEARCH_INFO_STRING);
            for (WebElement e : lista) {
                for (GoogleErrorDictionary ged : GoogleErrorDictionary.values()) {
                    if (e.getText().contains(ged.getTextError())) {
                        log.debug("Not good.");
                        log.warn("Word not found, but probably you've misspelled it. Google tries to help You !");
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
