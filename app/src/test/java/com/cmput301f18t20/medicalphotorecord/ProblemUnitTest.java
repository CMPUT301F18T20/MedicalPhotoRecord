package com.cmput301f18t20.medicalphotorecord;

import org.apache.commons.lang3.StringUtils;
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

    @Test
    public void testPatientRecordCanBeAddedAndFetched() {
        Problem problem = new Problem("");
        PatientRecord patientRecord = new PatientRecord("");
        Record record = new Record("");

        /* add patient record */
        problem.addRecord(patientRecord);

        /* verify a few assertions that should be true */
        assertEquals("record count should be 1", 1, problem.getRecordCount());
        assertEquals("only record should be the patient record",
                problem.getRecord(0), patientRecord);

        /* add normal record */
        problem.addRecord(record);

        /* verify a few assertions that should be true */
        assertEquals("record count should be 2", 2, problem.getRecordCount());
        assertEquals("first record should be the patient record",
                problem.getRecord(0), patientRecord);
        assertEquals("second record should be the normal record",
                problem.getRecord(1), record);
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
            //correct functionality should generate an error, so nothing to do here
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

    /* tests that fetchUpdatedRecordListTest will fetch updated database results */
    @Test
    public void fetchUpdatedRecordListTest() {
        Record record = new Record("");
        Problem problem = new Problem("");
        problem.addRecord(record); //TODO consideration, wouldn't problem.addRecord add the record to database?
        fail("Not fully implemented");
        //TODO: XXX URGENT: Need a way to add a record to the database

        /*
        add record, record2 to database.
        Check actual this.records instead of using getRecords to verify that only record is in there and recordCount is 1
        call problem.fetchUpdatedRecordList()
        See that it called fetchUpdatedRecordListTest() and now the record list has those exact two records
        add record3 to database
        check this.records only has two members with problem.getRecordCount()
        call record.getList()
        See that it called fetchUpdatedRecordListTest() and now the record list has all three records
         */
    }

    /* test getAllPhotos returns all the photos assigned to PatientRecords and in chronological
     * order of date the record was added to this app.  Adds three photos to two records. Record1
     * in chronological order gets photo 3, record2 gets photo 1 then photo 2.  Check that the
     * internal structure of problem has maintained the order of the records by making sure the
     * photos from record1 come back before record2, and photos from record2 come back in the
     * order they were added by calling problem.getAllPhotos(). Delete record 1, which removes
     * the associated photo1 and photo2. Call problem.getAllPhotos() to make sure only photo3 is
     * returned.
     */
    //TODO need version where a non PatientRecord entry exists,
    //TODO and one where only record objects exist and no photos should be returned
    @Test
    public void getAllPhotos() {
        Problem problem = new Problem("");
        ArrayList<Photo> testPhotos = new ArrayList<>();

        // create new photos, they are in chronological order
        Photo Photo1 = new Photo();
        Photo Photo2 = new Photo();
        Photo Photo3 = new Photo();

        // create new patient records to add the photos to, these are in chronological order
        PatientRecord patientRecord1 = new PatientRecord("");
        PatientRecord patientRecord2 = new PatientRecord("");

        //add photos in a non chronological order to the PatientRecords.  The order of the photo
        //creations should be ignored and the only thing they are ordered by is chronological
        //order of records followed by add order to the record

        //photo3 should appear first in results as patient record 1 is the
        //first record chronologically
        patientRecord1.addPhoto(Photo3);

        //photo 1 should be next followed by photo2 as they were added in that order to the second
        //record chronologically
        patientRecord2.addPhoto(Photo1);
        patientRecord2.addPhoto(Photo2);

        //add records to problem
        problem.addRecord(patientRecord1);
        problem.addRecord(patientRecord2);

        // this is the order they should come back in based on above description
        testPhotos.addAll(Arrays.asList(Photo3, Photo1, Photo2));

        assertEquals("Photos did not come back in correct order",
                testPhotos, problem.getAllPhotosFromRecordsInOrder());

        //remove patientRecord2, which removes Photo1 and Photo2 from
        //the results. Only Photo 3 remains
        problem.removeRecord(patientRecord2);

        //set up testPhotos to match changes
        testPhotos.clear();
        testPhotos.add(Photo3);

        //Results should be only photo3
        assertEquals("Expected to only see photo 3 in results as record2 was removed",
                testPhotos, problem.getAllPhotosFromRecordsInOrder());
    }

    @Test
    public void canSetGetDescription() {
        String newDescription = "Hola Mundo";
        Problem problem = new Problem("");

        /* description should initially be null */
        assertEquals("Initial problem description was not null",
                problem.getDescription(), null);

        /* set description to new value */
        problem.setDescription(newDescription);

        assertEquals("Problem description was not set to newDescription",
                problem.description, newDescription);

        assertEquals("Problem description was not fetched correctly",
                problem.getDescription(), newDescription);


    }

    //TODO: test fetchUpdatedRecordList, getAllGeo,

    //TODO network and local storage tests
}

