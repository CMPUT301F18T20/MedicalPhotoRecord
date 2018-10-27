package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ProblemUnitTest {

    /* Exercises AddRecord by adding two records to the Problem and then ensuring that
    * the objects added were in the right indexes in the Problem.  Excercises getRecord by
    * using it to fetch the records by index in above procedure.  Then it uses setRecord to
    * swap the locations of the two records and then verifies with getRecord that the set was
    * successful and they have swapped indexes */
    @Test
    public void testAddRecordGetRecordAndSetRecord() {
        /* create problem */
        Problem problem = new Problem("");
        Record record0 = new Record("10000000"),
                record1 = new Record("20000000");

        problem.addRecord(record0);
        problem.addRecord(record1);

        //record at indice 0 is definitely record0 and not record1
        assertEquals("Indice 0 should have record 0 at this step",
                problem.getRecord(0), record0);
        assertNotEquals("Indice 0 should not have record 1 at this step",
                problem.getRecord(0), record1);

        //record at indice 1 is definitely record1 and not record0
        assertEquals("Indice 1 should have record 1 at this step",
                problem.getRecord(1), record1);
        assertNotEquals("Indice 1 should have record 1 at this step",
                problem.getRecord(1), record0);

        /* change record at position 0 to record1 and at position 1 to be record0 */
        problem.setRecord(record1, 0);
        problem.setRecord(record0, 1);

        //record at indice 0 is definitely record1 and not record0
        assertEquals("Indice 0 should have record 1 at this step",
                problem.getRecord(0), record1);
        assertNotEquals("Indice 0 should have record 1 at this step",
                problem.getRecord(0), record0);

        //record at indice 1 is definitely record0 and not record1
        assertEquals("Indice 1 should have record 0 at this step",
                problem.getRecord(1), record0);
        assertNotEquals("Indice 1 should have record 0 at this step",
                problem.getRecord(1), record1);
    }

    /* Exercises getRecords() by fetching the records from the problem after they have been set
    * and getRecordCount by getting the record count and ensuring that it matches what was
    * expected.  Exercises addRecord by using it when adding records to the problem */
    @Test
    public void testAddRecordGetRecordsAndGetRecordCount() {

        /* try a few values for number of records */
        for (int recordCounter : Arrays.asList(0, 1, 3, 200) ) {

            /* create problem */
            Problem problem = new Problem("");

            /* Create our own Arraylist of records adding "recordCounter" records in */
            /* also add them to the problem */
            ArrayList<Record> records = new ArrayList<>();
            Record record = new Record("");
            for (int i = 0; i < recordCounter; i++) {
                records.add(record);
                problem.addRecord(record);
            }

            /* ensure correct record count */
            assertEquals("Record counter was incorrect",
                    problem.getRecordCount(), recordCounter);

            /*  ensure array is correctly fetched and was correctly set */
            assertArrayEquals("Record arrays were not equal",
                    problem.getRecords().toArray(), records.toArray());
        }
    }

    /* ensures that getRecord, setRecord and removeRecord will return an
     * IndexOutOfBoundsException on a bad access index */
    @Test
    public void testIndexBoundsException() {
        Record record = new Record("");
        Problem problem = new Problem("");

        //add 1 record to problem
        problem.addRecord(record);

        //Make sure it was added correctly
        assertEquals("Indice 0 should have record at this step",
                problem.getRecord(0), record);

        try {
            //try to access a bad index with get
            record = problem.getRecord(1);
            fail("invalid access to problem record list did not " +
                    "generate an IndexOutOfBoundsException when using getRecord");
        } catch(IndexOutOfBoundsException e) {
            //correct functionality should generate an error
        }

        try {
            //try to access a bad index with set
            problem.setRecord(record, 1);
            fail("invalid access to problem record list did not " +
                    "generate an IndexOutOfBoundsException when using setRecord");
        } catch(IndexOutOfBoundsException e) {
            //correct functionality should generate an error
        }

        try {
            //try to access a bad index with remove
            problem.removeRecord(1);
            fail("invalid access to problem record list did not " +
                    "generate an IndexOutOfBoundsException when using removeRecord");
        } catch(IndexOutOfBoundsException e) {
            //correct functionality should generate an error
        }
    }

    /* tests that removing a record either by object or by index functions as intended.  Adds
    * records using addRecord, fetches using getRecord and removes using both versions of
    * removeRecord. */
    @Test
    public void testRemoveRecord() {
        Record record0 = new Record("10000000"),
                record1 = new Record("20000000"),
                record2 = new Record("30000000");

        Problem problem = new Problem("");

        //add records to problem
        problem.addRecord(record0);
        problem.addRecord(record1);
        problem.addRecord(record2);

        //Ensure records are at correct locations
        assertEquals("Indice 0 should have record 0 at this step",
                problem.getRecord(0), record0);
        assertEquals("Indice 1 should have record 1 at this step",
                problem.getRecord(1), record1);
        assertEquals("Indice 2 should have record 2 at this step",
                problem.getRecord(2), record2);

        //remove record by object
        problem.removeRecord(record0);

        //Ensure records are at correct locations
        assertEquals("Indice 0 should have record 1 at this step",
                problem.getRecord(0), record1);
        assertEquals("Indice 1 should have record 2 at this step",
                problem.getRecord(1), record2);

        //remove record by index
        problem.removeRecord(0);

        //Ensure record is at correct location
        assertEquals("Indice 0 should have record 2 at this step",
                problem.getRecord(0), record2);

        //Ensure only one record stored in problem
        assertEquals("Record counter was incorrect", problem.getRecordCount(), 1);
    }

    //TODO: test fetchUpdatedRecordList, getAllPhotos, getAllGeo, updateIndex,
    //TODO: getAggregateKeywordCounts, updateAggregatedIndex, getDescription, setDescription
}

