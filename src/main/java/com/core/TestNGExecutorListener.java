/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.utils.Docker;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author krbk
 */
public class TestNGExecutorListener implements ITestListener {

    private static final Logger log = Logger.getLogger(TestNGExecutor.class);
    private static List<String> executedTestList = new ArrayList<>();
    private static List<String> passedTestList = new ArrayList<>();
    private static List<String> skippedTestList = new ArrayList<>();
    private static List<String> failedTestList = new ArrayList<>();
    private static int executed = 0, skipped = 0, failed = 0, passed = 0;

    @Override
    public void onTestStart(ITestResult itr) {
        log.info("Starting test: " + itr.getMethod().getMethodName());
        executed++;
        executedTestList.add(itr.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult itr) {
        log.info("Test succeded: " + itr.getMethod().getMethodName());
        passed++;
        passedTestList.add(itr.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult itr) {
        log.info("Test failed: " + itr.getMethod().getMethodName());
        failed++;
        failedTestList.add(itr.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult itr) {
        log.info("Test skipped: " + itr.getMethod().getMethodName());
        skipped++;
        skippedTestList.add(itr.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext itc) {

    }

    @Override
    public void onFinish(ITestContext itc) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult itr) {
    }

    public void afterClass() {
        log.info("Tests concluded. Results:\n" +
                "Tests executed: " + executed + " " + getResultsFromList(executedTestList) + "\n" +
                "Passed tests: " + passed + " " + getResultsFromList(passedTestList) + "\n" +
                "Skipped tests: " + skipped + " " + getResultsFromList(skippedTestList) + "\n" +
                "Failed tests: " + failed + " " + getResultsFromList(failedTestList));
        if (System.getProperty("mode.remote").equals("true"))
            Docker.dockerComposeDown();
    }

    private String getResultsFromList(List<String> listOfTests) {
        String returnString = "";
        for (String singleTest : listOfTests) {
            returnString += singleTest + " ";
        }
        return returnString;
    }
}
