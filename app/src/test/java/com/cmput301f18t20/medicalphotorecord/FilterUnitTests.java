package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static org.junit.Assert.*;

public class FilterUnitTests {

    public void commonCode(Filter filter) {

        filter.setBodyLocationIncludedStatus(Boolean.TRUE);

        //ensure body location now true and geo still false
        assertEquals("Body Location Filter not True",
                filter.BodyLocationIncluded(), Boolean.TRUE);
        assertEquals("Geo Filter not False",
                filter.GeoIncluded(), Boolean.FALSE);

        filter.setGeoIncludedStatus(Boolean.TRUE);

        //ensure both true
        assertEquals("Body Location Filter not True",
                filter.BodyLocationIncluded(), Boolean.TRUE);
        assertEquals("Geo Filter not True",
                filter.GeoIncluded(), Boolean.TRUE);
    }

    @Test
    public void TestFilterSettings() {
        Filter filter = new Filter();

        //ensure both fields are false to start with
        assertEquals("Body Location Filter not false to start",
                filter.BodyLocationIncluded(), Boolean.FALSE);
        assertEquals("Geo Filter not false to start",
                filter.GeoIncluded(), Boolean.FALSE);

        commonCode(filter);
    }

    @Test
    public void TestFilterSettingsWithConstructor() {
        Filter filter = new Filter(Boolean.TRUE, Boolean.TRUE);

        //ensure both fields are true to start with
        assertEquals("Body Location Filter not true to start",
                filter.BodyLocationIncluded(), Boolean.TRUE);
        assertEquals("Geo Filter not true to start",
                filter.GeoIncluded(), Boolean.TRUE);

        filter.setBodyLocationIncludedStatus(Boolean.FALSE);
        filter.setGeoIncludedStatus(Boolean.FALSE);

        commonCode(filter);
    }
}
