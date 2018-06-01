package com.tests.management;

import com.tests.aggregation.ApiTests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Others {

    ApiTests apiTests = new ApiTests();

    @Test
    public void simpleNumberCheck() {
        Assert.assertTrue(apiTests.checkMSISDNLength(), " it really is 9 chars long");
    }
}
