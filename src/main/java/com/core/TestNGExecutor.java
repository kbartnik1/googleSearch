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
    private List<List<String>> methodNames = new ArrayList<List<String>>();
    private List<String> classCollection = new ArrayList<>();
    List<TestNG> testNGInstances = new ArrayList<>();

    public TestNGExecutor() {
        getManagerClassNames();
        getTestMethodNamesFromAllClassses();

    }

    private XmlSuite createXMLSuite(int testIndex, String className, List<String> methodZ) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        XmlSuite xmlSuite = new XmlSuite();
        System.out.println("debug. List of methods passed to createXMLSuite = "+methodZ);
            for (int i = 0; i < methodZ.size(); i++) {
                XmlTest xmlTest = new XmlTest(xmlSuite);
                xmlSuite.setName("Suite " + testIndex);
                xmlSuite.setParallel(XmlSuite.ParallelMode.CLASSES);
                xmlSuite.setVerbose(1);
                xmlTest.setName("Test " + testIndex);
                xmlTest.setPreserveOrder(true);
                XmlClass xmlClass = new XmlClass(Class.forName(className));    // ?
                List<XmlInclude> includeMethods = new ArrayList<>();
                includeMethods.add(new XmlInclude(methodZ.get(testIndex))); // ?
                xmlClass.setIncludedMethods(includeMethods);
                List<XmlClass> classList = new ArrayList<>();
                classList.add(xmlClass);
                xmlTest.setXmlClasses(classList);
                /*methodNames.remove(testIndex);*/
            }

        return xmlSuite;
    }

    void collectTests() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        TestNG testNG;
        List<List<XmlSuite>> listOfSuiteList = new ArrayList<>();
        List<XmlSuite> suiteList;
        /*while (methodNames.size() != 0) {*/
        for (int i = 0; i < classCollection.size(); i ++){
            System.out.println(classCollection.get(i) +  " exploring this class:");
            for(int j = 0; j < methodNames.get(i).size(); j++){
                    /*for(int)*/
                    suiteList = new ArrayList<>();
                    suiteList.add(createXMLSuite(j, classCollection.get(i), methodNames.get(i)));
                    listOfSuiteList.add(suiteList);
                    /*methodNames.get(0).remove(0);*/
                }
        }
        for(int i = 0; i < listOfSuiteList.size(); i++){
            System.out.println(listOfSuiteList.get(0)+ " ??????????????");
            testNG = new TestNG();
            testNG.setXmlSuites(listOfSuiteList.get(0));
            testNG.addListener(listener);
            testNGInstances.add(testNG);
        }
    }

    public void runTests() {
        try {
            collectTests();
            printImportantData();
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
            for (int j = 0; j < classCollection.size(); j++) {
                List<String> listOfMethodsInSpecificClass = new ArrayList<>();
                Class c = Class.forName(classCollection.get(j));
                Method[] m = c.getDeclaredMethods();
                for (int i = 0; i < m.length; i++) {
                    if (m[i].isAnnotationPresent(org.testng.annotations.Test.class)) {
                        /*System.out.println(m[i]);*/
                        listOfMethodsInSpecificClass.add(m[i].toString());
                    }
                }
                for (int i = 0; i < listOfMethodsInSpecificClass.size(); i++) {
                    String[] tmp = listOfMethodsInSpecificClass.get(i).split("\\.");
                    listOfMethodsInSpecificClass.set(i, tmp[tmp.length - 1].substring(0, tmp[tmp.length - 1].length() - 2));
                }
                methodNames.add(listOfMethodsInSpecificClass);
            }
            printImportantData();
        } catch (Exception e) {
            System.out.println(e);
        }
        return methodNames;
    }

    private void getManagerClassNames() {
        ClassCollector cc = new ClassCollector();
        classCollection = cc.find(System.getProperty("tests.manager.classses.package"));
        System.out.println(classCollection);
    }

    private void printImportantData() {
        System.out.println("All collected methods\n==================");
        for (int i = 0; i < methodNames.size(); i++) {
            for (int j = 0; j < methodNames.get(i).size(); j++) {
                System.out.println(methodNames.get(i).get(j));
            }
        }
    }
}