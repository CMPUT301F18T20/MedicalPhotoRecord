package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.*;

public class FilterUnitTests {

    public void flipGeoAndBodyAndMakeSureTheyFlip(Filter filter) {

        filter.setBodyLocationIncludedStatus(TRUE);

        //ensure body location now true and geo still false
        assertEquals("Body Location Filter not True",
                filter.BodyLocationIncluded(), TRUE);
        assertEquals("Geo Filter not False",
                filter.GeoIncluded(), FALSE);

        filter.setGeoIncludedStatus(TRUE);

        //ensure both true
        assertEquals("Body Location Filter not True",
                filter.BodyLocationIncluded(), TRUE);
        assertEquals("Geo Filter not True",
                filter.GeoIncluded(), TRUE);
    }

    @Test
    public void TestFilterSettings() {
        Filter filter = new Filter();

        //ensure both fields are false to start with
        assertEquals("Body Location Filter not false to start",
                filter.BodyLocationIncluded(), FALSE);
        assertEquals("Geo Filter not false to start",
                filter.GeoIncluded(), FALSE);
        assertEquals("SearchForProblems not TRUE at start", TRUE, filter.SearchForProblems());
        assertEquals("SearchForRecords not FALSE at start", FALSE, filter.SearchForRecords());
        assertEquals("SearchForPatientRecords not FALSE at start",
                FALSE, filter.SearchForPatientRecords());

        flipGeoAndBodyAndMakeSureTheyFlip(filter);
    }

    @Test
    public void TestFilterSettingsWithConstructor() {
        Filter filter = new Filter(TRUE, TRUE, FALSE, TRUE, TRUE);

        //ensure both fields are true to start with
        assertEquals("Body Location Filter not true to start",
                filter.BodyLocationIncluded(), TRUE);
        assertEquals("Geo Filter not true to start",
                filter.GeoIncluded(), TRUE);
        assertEquals("SearchForProblems not FALSE at start", FALSE, filter.SearchForProblems());
        assertEquals("SearchForRecords not TRUE at start", TRUE, filter.SearchForRecords());
        assertEquals("SearchForPatientRecords not TRUE at start",
                TRUE, filter.SearchForPatientRecords());

        filter.setBodyLocationIncludedStatus(FALSE);
        filter.setGeoIncludedStatus(FALSE);

        flipGeoAndBodyAndMakeSureTheyFlip(filter);
    }
}
