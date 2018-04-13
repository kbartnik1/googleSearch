/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.tests.TestManager;
import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author krbk
 */
public class TestNGExecutor {

    private static final Logger log = Logger.getLogger(TestNGExecutor.class);
    private TestNGExecutorListener listener = new TestNGExecutorListener();
    private List<String> methodNames = new ArrayList<>();
    List<TestNG> testNGInstances = new ArrayList<>();

    public TestNGExecutor() {
        getTestMethodNames();
    }

    private XmlSuite createXMLSuite(int testIndex) {
        XmlSuite xmlSuite = new XmlSuite();
        XmlTest xmlTest = new XmlTest(xmlSuite);
        xmlSuite.setName("Suite " + testIndex);
        xmlSuite.setParallel(XmlSuite.ParallelMode.CLASSES);
        xmlSuite.setVerbose(1);
        xmlTest.setName("Test " + testIndex);
        xmlTest.setPreserveOrder(true);
        XmlClass xmlClass = new XmlClass(TestManager.class);
        List<XmlInclude> includeMethods = new ArrayList<>();
        includeMethods.add(new XmlInclude(methodNames.get(testIndex)));
        xmlClass.setIncludedMethods(includeMethods);
        List<XmlClass> classList = new ArrayList<>();
        classList.add(xmlClass);
        xmlTest.setXmlClasses(classList);
        methodNames.remove(testIndex);
        return xmlSuite;
    }

    void collectTests() {
        TestNG testNG;
        List<List<XmlSuite>> listOfSuiteList = new ArrayList<>();
        List<XmlSuite> suiteList;
        while (methodNames.size() != 0) {
            suiteList = new ArrayList<>();
            suiteList.add(createXMLSuite(0));
            listOfSuiteList.add(suiteList);
        }
        do {
            testNG = new TestNG();
            testNG.setXmlSuites(listOfSuiteList.get(0));
            testNG.addListener(listener);
            testNGInstances.add(testNG);
            listOfSuiteList.remove(0);
        } while (listOfSuiteList.size() != 0);
    }

    public void runTests() {
        try {
            collectTests();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (TestNG ngInstance : testNGInstances) {
                executor.submit(() -> {
                    ngInstance.run();
                });
            }
            executor.shutdown();
            executor.awaitTermination(1800L, TimeUnit.SECONDS);
            new TestNGExecutorListener().afterClass();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void getTestMethodNames() {
        Class c = TestManager.class;
        Method[] m = c.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {

            if (m[i].isAnnotationPresent(org.testng.annotations.Test.class)) {
                methodNames.add(m[i].toString());
            }
        }
        for (int i = 0; i < methodNames.size(); i++) {
            String[] tmp = methodNames.get(i).split("\\.");
            methodNames.set(i, tmp[tmp.length - 1].substring(0, tmp[tmp.length - 1].length() - 2));
        }
    }
}