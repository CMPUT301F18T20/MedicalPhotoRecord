/*
 * Class name: ElasticsearchPatientRecordControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 13/11/18 8:00 PM
 *
 * Last Modified: 13/11/18 6:48 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import io.searchbox.core.DeleteByQuery;

import static Controllers.Utils.nameGen;
import static GlobalSettings.GlobalSettings.getIndex;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static java.lang.Math.abs;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class ElasticsearchPatientRecordControllerTest {

    private String
            PatientRecordCreatedByUserIDInAddTest = "ImFromThePatientRecordAddTest",
            PatientRecordUserIDToGetInGetTest = "ImFromThePatientRecordGetTest",
            PatientRecordUserIDToRetrieveInGetAllTest = "ImFromPatientRecordGetAllTest",
            PatientRecordUserIDToRetrieveInGetAllBUGTest = "ImFromPatientRecordGetAllBUGTest",
            PatientRecordIDForModifyTest = "ImUserIDFromModifyTest",
            PatientRecordOriginalTitle = "Original@gmail.com",
            PatientRecordOriginalDescription = "780-555-1234",
            PatientRecordModifiedTitle = "Modified@gmail.com",
            PatientRecordModifiedDescription = "587-555-9876";

    private String[] PatientRecordTitlesToRetrieveInGetAllTest =
            nameGen("ImTitlePRGetAllTest", 3);

    private String[] PatientRecordTitlesToRetrieveInGetAllBUGTest =
            nameGen("ImTitlePRGetAllBUGTest", 50);

    //set index to testing index and remove all entries from PatientRecord database
    @After
    @Before
    public void WipePatientRecordsDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @Test
    //pass
    public void AddPatientRecordTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        //create new patientRecord
        PatientRecord newPatientRecord = new PatientRecord(PatientRecordCreatedByUserIDInAddTest, "");

        //add new patientRecord to the patientRecord database
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(newPatientRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the patientRecord database
        PatientRecord patientRecord = new ElasticsearchPatientRecordController.
                GetPatientRecordByPatientRecordUUIDTask().execute(newPatientRecord.getUUID()).get();

        //check that the new patientRecord is now in the database
        assertEquals("patientRecords were not equal", patientRecord.getCreatedByUserID(),
                newPatientRecord.getCreatedByUserID());
    }

    @Test
    //pass
    public void GetPatientRecordByPatientRecordUUIDTaskTest() throws ExecutionException, 
            InterruptedException, UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        //create new patientRecord
        PatientRecord newPatientRecord = new PatientRecord(PatientRecordUserIDToGetInGetTest,"");

        //add new patientRecord to the patientRecord database
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(newPatientRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the patientRecord database
        PatientRecord fetchedPatientRecord = new ElasticsearchPatientRecordController.
                GetPatientRecordByPatientRecordUUIDTask().execute(newPatientRecord.getUUID()).get();


        assertEquals("fetched patientRecord userID not equal",
                newPatientRecord.getCreatedByUserID(), fetchedPatientRecord.getCreatedByUserID());
    }

    @Test
    //pass
    public void getPatientRecordsByProblemUUIDTest() throws ExecutionException, TitleTooLongException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        AssertPatientRecordsCanBeAddedAndThenBatchFetched(PatientRecordUserIDToRetrieveInGetAllTest,
                PatientRecordTitlesToRetrieveInGetAllTest);
    }

    @Test
    //pass
    public void getPatientRecordsByProblemUUIDBUGTest() throws ExecutionException, TitleTooLongException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        //check we can fetch more than 10 results at once
        AssertPatientRecordsCanBeAddedAndThenBatchFetched(PatientRecordUserIDToRetrieveInGetAllBUGTest,
                PatientRecordTitlesToRetrieveInGetAllBUGTest);
    }


    private void AssertPatientRecordsCanBeAddedAndThenBatchFetched(String suppliedUserID, 
                                                                   String[] suppliedTitles)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, TitleTooLongException {
        
        String ProblemUUID = "myFakeProblemUUID";
        ArrayList<PatientRecord> expectedPatientRecords = new ArrayList<>();
        ArrayList<Boolean> expectedPatientRecordInResults = new ArrayList<>();

        //add all expected patientRecords in
        for (String title : suppliedTitles) {

            PatientRecord newPatientRecord = new PatientRecord(suppliedUserID, title);

            //set associated problem uuid
            newPatientRecord.setAssociatedProblemUUID(ProblemUUID);

            //add new patientRecord to the patientRecord database
            new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(newPatientRecord).get();

            //add new patientRecord to expected returns
            expectedPatientRecords.add(newPatientRecord);
            expectedPatientRecordInResults.add(false);
        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //make sure each of the added users is individually fetchable
        for (int i = 0; i < suppliedTitles.length; i++) {

            //fetch new PatientRecord from the PatientRecord database
            PatientRecord patientRecord = new ElasticsearchPatientRecordController.GetPatientRecordByPatientRecordUUIDTask()
                    .execute(expectedPatientRecords.get(i).getUUID()).get();

            assertEquals("Fetched PatientRecord had different UserID from one added",
                    patientRecord.getCreatedByUserID(), suppliedUserID);

            assertEquals("Fetched PatientRecord Title was different from one added",
                    patientRecord.getTitle(), suppliedTitles[i]);
        }

        //Get objects from database associated to the problem uuid
        ArrayList<PatientRecord> results = new ElasticsearchPatientRecordController.GetPatientRecordsWithProblemUUID()
                .execute(ProblemUUID).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/161
        if (suppliedTitles.length > 10 && results.size() == 10) {
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/161 " +
                            "there should be as many results as patientRecords we queried. We got exactly " +
                            "ten results instead of expected " + suppliedTitles.length,
                    results.size() == suppliedTitles.length);
        }

        assertTrue("there should be as many results as patientRecords we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedTitles.length,
                results.size() == suppliedTitles.length);

        //compare results to what we expected to find.
        //The patientRecords we added should now be there
        for (PatientRecord patientRecord : results) {
            for (int i = 0; i < suppliedTitles.length; i++) {

                //track which expected patientRecords are seen in the results
                if (patientRecord.getTitle().equals(suppliedTitles[i])) {
                    expectedPatientRecordInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected patientRecords in the results
        for (boolean patientRecordSeenInResults : expectedPatientRecordInResults) {
            assertTrue("PatientRecord missing from results", patientRecordSeenInResults);
        }
    }

    @Test
    //pass
    public void modifyPatientRecordSavesChanges() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException, TitleTooLongException {

        //setup original patientRecord
        PatientRecord patientRecord = new PatientRecord(PatientRecordIDForModifyTest,PatientRecordOriginalTitle);
        patientRecord.setDescription(PatientRecordOriginalDescription);

        //add patientRecord
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(patientRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //modify patientRecord
        patientRecord.setTitle(PatientRecordModifiedTitle);
        patientRecord.setDescription(PatientRecordModifiedDescription);

        //check the object was changed and equals our modified values
        assertEquals("Original patientRecord title not modified correctly.",
                PatientRecordModifiedTitle, patientRecord.getTitle());
        assertEquals("Original patientRecord description not modified correctly.",
                PatientRecordModifiedDescription, patientRecord.getDescription());

        //save modification
        new ElasticsearchPatientRecordController.SaveModifiedPatientRecord().execute(patientRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned patientRecord, hopefully modified
        PatientRecord returnedPatientRecord = new ElasticsearchPatientRecordController
                .GetPatientRecordByPatientRecordUUIDTask()
                .execute(patientRecord.getUUID()).get();

        //check the object was changed and equals our modified values
        assertEquals("patientRecord title on returned object not modified correctly.",
                PatientRecordModifiedTitle, returnedPatientRecord.getTitle());
        assertEquals("patientRecord description on returned object not modified correctly.",
                PatientRecordModifiedDescription, returnedPatientRecord.getDescription());
    }

    //tests for existence of BUG https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/199
    @Test
    //pass
    public void modifyPatientRecordSavesDateChangesBUG() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException, TitleTooLongException {

        //setup original patientRecord
        PatientRecord patientRecord = new PatientRecord(PatientRecordIDForModifyTest,PatientRecordOriginalTitle);
        patientRecord.setDescription(PatientRecordOriginalDescription);

        //add patientRecord
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(patientRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        Date PatientRecordModifiedDate = new Date(System.currentTimeMillis());

        //modify patientRecord date
        patientRecord.setDate(PatientRecordModifiedDate);

        //check the object was changed and equals our modified values
        assertEquals("Original patientRecord date not modified correctly.",
                PatientRecordModifiedDate.getTime(), patientRecord.getDate().getTime());

        //save modification
        new ElasticsearchPatientRecordController.SaveModifiedPatientRecord().execute(patientRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned patientRecord, hopefully modified
        PatientRecord returnedPatientRecord = new ElasticsearchPatientRecordController
                .GetPatientRecordByPatientRecordUUIDTask()
                .execute(patientRecord.getUUID()).get();

        //date is not exact, it seems to be rounded to nearest 1000 nsec
        //BUG https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/199
        assertTrue("REOPEN BUG: https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/199 " +
                "patientRecord date on returned object not modified correctly.",
                abs(PatientRecordModifiedDate.getTime() - returnedPatientRecord.getDate().getTime()) <= 1000);

    }

    @Test
    public void SearchByBodyWithOneUserID() throws TitleTooLongException
            , UserIDMustBeAtLeastEightCharactersException, ExecutionException
            , InterruptedException {
        String userID = "userIDFromSearchByBodyTest"
                ,title = "titleFromDesiredUser";
        String fakeUserID = "Shouldn'tBeInTheResults"
                ,title1 = "titleFromUnwantedUser";

        //create patientrecord with desired bodylocation
        PatientRecord recordWanted1 = new PatientRecord(userID,title);
        recordWanted1.setBodyLocation("head");

        //create record with associated bodylocation near desired location
        PatientRecord recordWanted2 = new PatientRecord(userID,title);
        recordWanted2.setBodyLocation("chest");

        //create record by correct user but unassociated bodylocation not near desired location
        PatientRecord unWantedRecord1 = new PatientRecord(userID,title);
        unWantedRecord1.setBodyLocation("leftHand");

        //create record by correct user but unassociated bodylocation not near desired location
        PatientRecord unWantedRecord2 = new PatientRecord(userID,title);
        unWantedRecord2.setBodyLocation("abs");

        //create record by incorrect user but with desired location
        PatientRecord unWantedRecord3 = new PatientRecord(fakeUserID,title1);
        unWantedRecord3.setBodyLocation("chest");

        //add to database
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(recordWanted1).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(recordWanted2).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord1).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord2).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord3).get();

        //allow time for change to reflect
        Thread.sleep(ControllerTestTimeout);
        //create new Array with just desired user for search
        ArrayList<String> userList = new ArrayList<>();
        userList.add(userID);

        //create new String with desired body location for search
        String desiredBodyLocation = "head";

        ArrayList<PatientRecord> results = new SearchController()
                .fetchNearBodyLocation(userList,desiredBodyLocation);

        for(PatientRecord record: results){
            assertEquals("Unwanted UserID appeared in result set.",userID,record.getCreatedByUserID());

            //make sure only desired output is in result
            assertThat(record.getBodyLocation(),anyOf(is("head"),is("chest")));
        }
    }

    @Test
    public void SearchByBodyWithMultipleUserIDs() throws TitleTooLongException
            , UserIDMustBeAtLeastEightCharactersException, ExecutionException
            , InterruptedException {
        String userID1 = "userIDFromSearchByBodyTest1"
                ,title = "titleFromDesiredUser";
        String userID2 = "userIDFromSearchByBodyTest2";
        String userID3 = "userIDFromSearchByBodyTest3";
        String fakeUserID = "UnwantedUserID"
                ,title2 = "UnwantedObjectTitle";


        //create patientrecord with desired bodylocation
        PatientRecord recordWanted1 = new PatientRecord(userID1,title);
        recordWanted1.setBodyLocation("head");

        //create record with associated bodylocation near desired location with different userID
        PatientRecord recordWanted2 = new PatientRecord(userID2,title);
        recordWanted2.setBodyLocation("chest");

        //create record with associated bodylocation near desired location with different userID
        PatientRecord recordWanted3 = new PatientRecord(userID3,title);
        recordWanted3.setBodyLocation("leftArm");

        //create record with associated bodylocation near desired location with different userID
        PatientRecord recordWanted4 = new PatientRecord(userID3,title);
        recordWanted4.setBodyLocation("rightArm");

        //create record by user but unassociated bodylocation not near desired location
        PatientRecord unWantedRecord1 = new PatientRecord(userID1,title2);
        unWantedRecord1.setBodyLocation("leftHand");

        //create record by user but unassociated bodylocation not near desired location
        PatientRecord unWantedRecord2 = new PatientRecord(userID2,title2);
        unWantedRecord2.setBodyLocation("abs");

        //create record by user but unassociated bodylcoation not near desired lcoation
        PatientRecord unWantedRecord3 = new PatientRecord(userID3,title2);
        unWantedRecord3.setBodyLocation("leftFoot");

        //create record by unwanted user and unassociated bodylcoation not near desired lcoation
        PatientRecord unWantedRecord4 = new PatientRecord(fakeUserID,title2);
        unWantedRecord3.setBodyLocation("rightFoot");



        //add to database
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(recordWanted1).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(recordWanted2).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(recordWanted3).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(recordWanted4).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord1).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord2).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord3).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(unWantedRecord4).get();

        //Allow time for change to reflect
        Thread.sleep(ControllerTestTimeout);
        //create new Array with just desired user for search
        ArrayList<String> userList = new ArrayList<>();
        userList.add(userID1);
        userList.add(userID2);
        userList.add(userID3);


        //create new String with desired body location for search
        String desiredBodyLocation = "head";

        ArrayList<PatientRecord> results = new SearchController()
                .fetchNearBodyLocation(userList,desiredBodyLocation);

        //make sure that size is as expected
        assertEquals("Size is not the same", 4,results.size());
        for(PatientRecord record: results){
            //make sure only wanted userIDs is in result
            assertThat(record.getCreatedByUserID(),anyOf(is(userID1),is(userID2),is(userID3)));
            //make sure only desired output is in result
            assertThat(record.getBodyLocation(),anyOf(is("head"),is("chest")
                    ,is("leftArm"),is("rightArm")));
        }
    }

    public void SearchWithKeywordAndBodyLocation(String userID,
                                                 String title,
                                                String description,
                                                String matchUserID,
                                                 int expectedSize,
                                                String bodyLocation,
                                                String... keywords)
            throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException {

    //create PatientRecord instance
    PatientRecord record = new PatientRecord(userID,title);


    }
}