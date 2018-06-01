/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tests.aggregation;

import com.generators.NumberGenerators;
import org.apache.log4j.Logger;

/**
 * @author krbk
 */

public class ApiTests {

    private static final Logger log = Logger.getLogger(ApiTests.class);
    NumberGenerators numbergenerator = new NumberGenerators();

    public boolean checkMSISDNLength() {
        String MSISDN = numbergenerator.MSISDN();
        if (MSISDN.length() == 9) {
            log.info("Generated MSISDN " + MSISDN + " is indeed 9 chars long");
            return true;
        }
        log.error("Something's wrong with that MSISDN of yours.");
        return false;
    }

}
