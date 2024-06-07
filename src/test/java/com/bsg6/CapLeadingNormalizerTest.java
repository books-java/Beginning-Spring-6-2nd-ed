package com.bsg6;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bsg6.util.CapLeadingNormalizer;
import com.bsg6.util.Normalizer;
import static org.testng.Assert.assertEquals;

public class CapLeadingNormalizerTest {
    Normalizer normalizer = new CapLeadingNormalizer();

    @DataProvider
    Object[][] data() {
        return new Object[][] {
                { "this is a test", "This Is A Test" },
                { " This IS a test ", "This Is A Test" },
                { "this is a test", "This Is A Test" }
        };
    }

    @Test(dataProvider = "data")
    public void testNormalization(String input, String expected) {
        assertEquals(normalizer.transform(input), expected);
    }
}
