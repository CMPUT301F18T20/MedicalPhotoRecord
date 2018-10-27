package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ProblemUnitTest {
    @Test
    public void testAddRecord() {
        /* create problem */
        Problem problem = new Problem("");
        

    }

    @Test
    public void testGetRecordsAndGetRecordCount() {

        /* create problem */
        Problem problem = new Problem("");

        /* try a few values for number of records */
        for (int recordCounter : Arrays.asList(0, 1, 3, 200) ) {

            /* Create our own Arraylist of records adding "recordCounter" records in */
            ArrayList<Record> records = new ArrayList<>();
            for (int i = 0; i < recordCounter; i++) {
                records.add(new Record(""));
            }

            /*set our array of records in the problem to be our new array */
            problem.records = records;

            /* ensure correct record count */
            assertEquals("Record counter was incorrect",
                    problem.getRecordCount(), recordCounter);

            /*  ensure array is correctly fetched */
            assertArrayEquals(problem.getRecords().toArray(), records.toArray());
        }
    }
}

