/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * @author krbk
 */
public class TestNGExecutorListener implements ITestListener {

    private static final Logger log = Logger.getLogger(TestNGExecutor.class);

    @Override
    public void onTestStart(ITestResult itr) {
        log.info("Starting test: " + itr.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult itr) {
        log.info("Test succeded: " + itr.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult itr) {
        log.info("Test failed: " + itr.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult itr) {

    }

    @Override
    public void onStart(ITestContext itc) {

    }

    @Override
    public void onFinish(ITestContext itc) {
        log.info("Finished execution of tests");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult itr) {
    }

}
