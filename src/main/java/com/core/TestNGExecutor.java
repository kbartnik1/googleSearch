/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.utils.ClassCollector;
import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<List<String>> listOfAllClassesAndTheirTestMethods = new ArrayList<List<String>>();
    private List<String> classNamesWithTests = new ArrayList<>();
    List<TestNG> testNGInstances = new ArrayList<>();

    public TestNGExecutor() {
        getManagerClassNames();
        getTestMethodNamesFromAllClassses();
    }

    private XmlSuite createXMLSuite(String className, List<String> methodZ) throws ClassNotFoundException {
        XmlSuite xmlSuite = new XmlSuite();
        for (int i = 0; i < methodZ.size(); i++) {
            XmlTest xmlTest = new XmlTest(xmlSuite);
            xmlSuite.setName("Suite " + className);
            xmlSuite.setParallel(XmlSuite.ParallelMode.TESTS);
            xmlSuite.setVerbose(1);
            xmlTest.setName("Test " + i);
            xmlTest.setPreserveOrder(true);
            XmlClass xmlClass = new XmlClass(Class.forName(className));
            xmlClass.setIncludedMethods(Arrays.asList(new XmlInclude(methodZ.get(i))));
            xmlTest.setXmlClasses(Arrays.asList(xmlClass));
        }
        return xmlSuite;
    }

    void collectTests() throws ClassNotFoundException {
        TestNG testNG;
        List<List<XmlSuite>> listOfSuiteList = new ArrayList<>();
        if (classNamesWithTests.size() == 0)
            log.warn("Testing classes not found. Aborting.");
        for (int i = 0; i < classNamesWithTests.size(); i++) {
            if (listOfAllClassesAndTheirTestMethods.get(i).size() == 0) {
                log.info("Class " + classNamesWithTests.get(i) + " does not have any runnable testing methods. Skipping !");
            } else {
                listOfSuiteList.add(Arrays.asList(createXMLSuite(classNamesWithTests.get(i), listOfAllClassesAndTheirTestMethods.get(i))));
            }
        }
        for (int i = 0; i < listOfSuiteList.size(); i++) {
            testNG = new TestNG();
            testNG.setXmlSuites(listOfSuiteList.get(i));
            testNG.addListener(listener);
            testNGInstances.add(testNG);
        }
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<List<String>> getTestMethodNamesFromAllClassses() {
        try {
            for (int j = 0; j < classNamesWithTests.size(); j++) {
                List<String> listOfMethodsInSpecificClass = new ArrayList<>();
                Method[] m = Class.forName(classNamesWithTests.get(j)).getDeclaredMethods();
                if (m.length == 0) {
                    listOfAllClassesAndTheirTestMethods.add(Arrays.asList());
                } else {
                    for (int i = 0; i < m.length; i++) {
                        if (m[i].isAnnotationPresent(org.testng.annotations.Test.class)) {
                            String[] tmp = m[i].toString().split("\\.");
                            listOfMethodsInSpecificClass.add(tmp[tmp.length - 1].substring(0, tmp[tmp.length - 1].length() - 2));
                        }
                    }
                    listOfAllClassesAndTheirTestMethods.add(listOfMethodsInSpecificClass);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return listOfAllClassesAndTheirTestMethods;
    }

    private void getManagerClassNames() {
        ClassCollector cc = new ClassCollector();
        classNamesWithTests = cc.find(System.getProperty("tests.manager.classses.package"));
    }

}