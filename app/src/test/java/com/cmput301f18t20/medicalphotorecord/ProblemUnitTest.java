package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.TooManyPhotosForSinglePatientRecord;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ProblemUnitTest {
    static final String Correct_User_ID = "abcdefgh";
    static final String Correct_Title = "abcdefgh";

    /* Exercises AddRecord by adding two records to the Problem and then ensuring that
    * the objects added were in the right indexes in the Problem.  Excercises getRecord by
    * using it to fetch the records by index in above procedure.  Then it uses setRecord to
    * swap the locations of the two records and then verifies with getRecord that the set was
    * successful and they have swapped indexes */
    @Test(expected = Test.None.class /* no exception expected */)
    public void testAddRecordGetRecordAndSetRecord()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        /* create problem */
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        Record record0 = new Record(Correct_User_ID, Correct_Title),
                record1 = new Record(Correct_User_ID, Correct_Title);

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

    @Test(expected = Test.None.class /* no exception expected */)
    public void testPatientRecordCanBeAddedAndFetched()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        PatientRecord patientRecord = new PatientRecord(Correct_User_ID, Correct_Title);
        Record record = new Record(Correct_User_ID, Correct_Title);

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
    @Test(expected = Test.None.class /* no exception expected */)
    public void testAddRecordGetRecordsAndGetRecordCount()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        /* try a few values for number of records */
        for (int recordCounter : Arrays.asList(0, 1, 3, 200) ) {

            /* create problem */
            Problem problem = new Problem(Correct_User_ID, Correct_Title);

            /* Create our own Arraylist of records adding "recordCounter" records in */
            /* also add them to the problem */
            ArrayList<Record> records = new ArrayList<>();
            Record record = new Record(Correct_User_ID, Correct_Title);
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
    @Test(expected = Test.None.class /* no exception expected */)
    public void testIndexBoundsException()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record = new Record(Correct_User_ID, Correct_Title);
        Problem problem = new Problem(Correct_User_ID, Correct_Title);

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
    @Test(expected = Test.None.class /* no exception expected */)
    public void testRemoveRecord()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Record record0 = new Record(Correct_User_ID, Correct_Title),
                record1 = new Record(Correct_User_ID, Correct_Title),
                record2 = new Record(Correct_User_ID, Correct_Title);

        Problem problem = new Problem(Correct_User_ID, Correct_Title);

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

    /* test getAllPhotos returns all the photos assigned to PatientRecords and in chronological
     * order of date the record was added to this app.  Adds three photos to two records. Record1
     * in chronological order gets photo 3, record2 gets photo 1 then photo 2.  Check that the
     * internal structure of problem has maintained the order of the records by making sure the
     * photos from record1 come back before record2, and photos from record2 come back in the
     * order they were added by calling problem.getAllPhotos(). Delete record 1, which removes
     * the associated photo1 and photo2. Call problem.getAllPhotos() to make sure only photo3 is
     * returned.
     */
    //TODO one where only record objects exist and no photos should be returned
    @Test(expected = Test.None.class /* no exception expected */)
    public void getAllPhotos()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException,
            TooManyPhotosForSinglePatientRecord {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        ArrayList<Photo> testPhotos = new ArrayList<>();

        // create new photos, they are in chronological order
        Photo Photo1 = new Photo();
        Photo Photo2 = new Photo();
        Photo Photo3 = new Photo();

        // create new patient records to add the photos to, these are in chronological order
        final PatientRecord patientRecord1 = new PatientRecord(Correct_User_ID, Correct_Title);
        PatientRecord patientRecord2 = new PatientRecord(Correct_User_ID, Correct_Title);
        //this record can't contain photos, so it will be filtered out
        Record commentRecord = new Record(Correct_User_ID, Correct_Title);

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

        //this record will be filtered out as it cannot contain photos
        problem.addRecord(commentRecord);

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

    @Test(expected = Test.None.class /* no exception expected */)
    public void testgetAllPatientRecords()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        //TODO refactor into functions
        Problem problem = new Problem(Correct_User_ID, Correct_Title);

        // create new patient records
        final PatientRecord patientRecord1 = new PatientRecord(Correct_User_ID, Correct_Title);
        final PatientRecord patientRecord2 = new PatientRecord(Correct_User_ID, Correct_Title);

        //this record will be filtered out
        final Record commentRecord = new Record(Correct_User_ID, Correct_Title);

        /* add the records */
        problem.addRecord(patientRecord1);
        problem.addRecord(patientRecord2);
        problem.addRecord(commentRecord);

        //Results should be {patientRecord1, patientRecord2, commentRecord)
        assertEquals("record list differs from what was expected",
                problem.getRecords(),
                new ArrayList<Record>() {{
                        add(patientRecord1);
                        add(patientRecord2);
                        add(commentRecord);
                }}
        );

        //Results should filter out the commentRecord
        assertEquals("problem did not filter the records correctly",
                problem.getAllPatientRecords(),
                new ArrayList<Record>() {{
                    add(patientRecord1);
                    add(patientRecord2);
                }}
        );

        //remove the two patient records and make sure the returns are still correct
        problem.removeRecord(patientRecord1);
        problem.removeRecord(patientRecord2);


        //Results should be {commentRecord)
        assertEquals("record list differs from what was expected",
                problem.getRecords(),
                new ArrayList<Record>() {{
                    add(commentRecord);
                }}
        );

        //Results should filter out the commentRecord and have nothing left
        assertEquals("problem did not filter the records correctly",
                problem.getAllPatientRecords(),
                new ArrayList<Record>()
        );

    }

    /* test getAllGeo returns all the locations assigned to PatientRecords and in chronological
     * order of date the record was added to this app.  Record1 in chronological order gets location2,
     * record2 gets location1.  Check that the location from record1 come back before record2,
     * Delete record 1, which removes location2. Call problem.getAllGeo() to make sure only
     * location1 is returned.
     */
    //TODO and one where only record objects exist and no locations should be returned
    @Test(expected = Test.None.class /* no exception expected */)
    public void getAllGeo()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        ArrayList<Location> testGeo = new ArrayList<>();

        // create new locations, they are in chronological order
        Location Location1 = new Location("1");
        Location Location2 = new Location("2");

        // create new patient records to add the locations to, these are in chronological order
        final PatientRecord patientRecord1 = new PatientRecord(Correct_User_ID, Correct_Title);
        PatientRecord patientRecord2 = new PatientRecord(Correct_User_ID, Correct_Title);

        //this record can't contain locations, so it will be filtered out
        Record commentRecord = new Record(Correct_User_ID, Correct_Title);

        //location2 should appear first in results as patient record 1 is the
        //first record chronologically
        patientRecord1.setGeolocation(Location2);

        //location 1 should be next as it has just been added to the second record
        patientRecord2.setGeolocation(Location1);

        //add records to problem
        problem.addRecord(patientRecord1);
        problem.addRecord(patientRecord2);

        //this record will be filtered out as it cannot contain locations
        problem.addRecord(commentRecord);

        // this is the order they should come back in based on above description
        testGeo.addAll(Arrays.asList(Location2, Location1));

        assertEquals("Geo did not come back in correct order",
                testGeo, problem.getAllGeoFromRecords());

        //remove patientRecord2, which removes Location1 from
        //the results. Only Location2 remains
        problem.removeRecord(patientRecord2);

        //set up testGeo to match changes
        testGeo.clear();
        testGeo.add(Location2);

        //Results should be only location2
        assertEquals("Expected to only see location2 in results as record2 was removed",
                testGeo, problem.getAllGeoFromRecords());
    }


    @Test(expected = Test.None.class /* no exception expected */)
    public void canSetGetDescription()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException,
            ProblemDescriptionTooLongException {

        String newDescription = "Hola Mundo";
        Problem problem = new Problem(Correct_User_ID, Correct_Title);

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

    /* does not generate UserIDMustBeAtLeastEightCharactersException on valid input
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetCreatedUserIDAndConstructorSanity()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        /* list of UserIDs to test against */
        for (String TestUserID : Arrays.asList("18004192", "UserName", "'$%%**?+++")) {

            Problem problem = new Problem(TestUserID, Correct_Title);

            assertEquals("UserIDs did not match.",
                    TestUserID, problem.getCreatedByUserID());
            assertEquals("Titles did not match.",
                    Correct_Title, problem.getTitle());
        }
    }

    /* generates UserIDMustBeAtLeastEightCharactersException on invalid input
     */
    @Test
    public void UserIDMustBeAtLeastEightCharactersExceptionGeneration ()
            throws TitleTooLongException {
        for (String TestUserID : Arrays.asList("Small", "Limits7", "")) {

            try {
                Problem problem = new Problem(TestUserID, Correct_Title);
                fail("UserIDMustBeAtLeastEightCharactersException should have been " +
                        "generated for input " + TestUserID);

            } catch (UserIDMustBeAtLeastEightCharactersException e) {
                //Do nothing as correct functionality generates this exception
            }
        }
    }

    /**
     * Testing that getting and setting Title for an problem behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetTitle()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        String string1 = "hello";
        String string2 = "world";
        List<String> SetAndGetTestStrings = Arrays.asList(string1, string2, string1);

        for (String currTitle: SetAndGetTestStrings) {
            problem.setTitle(currTitle);
            assertEquals("Title not set correctly", currTitle, problem.title);
            assertEquals("Title not fetched correctly", currTitle, problem.getTitle());
        }
    }

    /** if title is longer than 30 chars, should raise TitleTooLongException.
     * if title is less than or equal to 30 chars, it should not raise TitleTooLongException.
     * the other two cases are fail states.
     */
    @Test
    public void TitleBoundaries()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        List<String> BoundaryTestStrings = Arrays.asList(
                "", //0 char
                "a", //1 char
                "ababa", //5 chars
                "aabbccddeeaabbccddeeaabbccdde", //29 chars
                "aabbccddeeaabbccddeeaabbccddee", //30 chars
                "aabbccddeeaabbccddeeaabbccddeea", //31 chars
                "aabbccddeeaabbccddeeaabbccddeeaabbccddee" //40 chars
        );
        Boolean isLongerThanAcceptable;
        int Acceptable = 30;

        for (String currTitle: BoundaryTestStrings) {

            /* test if exception should be raised for current title */
            isLongerThanAcceptable = currTitle.length() > Acceptable;

            try {
                /* if isLongerThanAcceptable is true, should raise TitleTooLongException.
                if false, it should not raise TitleTooLongException.
                the other two cases are fail states. */
                problem.setTitle(currTitle);

                /* if it is longer than acceptable length */
                if (isLongerThanAcceptable) {
                    fail("Title too long exception was encountered when it shouldn't have been.\n"
                            + "Current title length:" + currTitle.length() + ",\n"
                            + "Current acceptable title length:" + Acceptable);
                }


            } catch (TitleTooLongException e){

                /* if it is shorter than or equal to acceptable length */
                if (!isLongerThanAcceptable) {
                    fail("Title too long exception was not encountered when it should have been.\n"
                            + "Current title length:" + currTitle.length() + ",\n"
                            + "Current acceptable title length:" + Acceptable);
                }
            }
        }
    }

    /**
     * Testing that getting and setting Date for a problem behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetDate()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            problem.setDate(date);

            assertEquals("Date was not set correctly", date, problem.dateCreated);
            assertEquals("Date was not fetched correctly", date, problem.getDate());
        }
    }

    /**
     * Testing that getting and setting DateLastModified for a problem behaves as expected.
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetLastModifiedDate()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        for (int i = 0; i < 5; i++) {
            Date date = new Date(System.currentTimeMillis());
            problem.setDateLastModified(date);

            assertEquals("Date was not set correctly", date, problem.dateLastModified);
            assertEquals("Date was not fetched correctly", date, problem.getDateLastModified());
        }
    }

    //TODO: getAllPatientRecords
    //also need version where a non PatientRecord problem exists
}

