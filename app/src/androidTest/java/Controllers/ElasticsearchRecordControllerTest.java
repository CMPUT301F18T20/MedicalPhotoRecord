/*
 * Class name: ElasticsearchRecordControllerTest
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

import com.cmput301f18t20.medicalphotorecord.Record;

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

import static GlobalSettings.GlobalSettings.getIndex;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static java.lang.Math.abs;
import static org.junit.Assert.*;


public class ElasticsearchRecordControllerTest {

    private String
            RecordCreatedByUserIDInAddTest = "ImFromTheRecordAddTest",
            RecordUserIDToGetInGetTest = "ImFromTheRecordGetTest",
            RecordUserIDToRetrieveInGetAllTest = "ImFromRecordGetAllTest",
            RecordUserIDToRetrieveInGetAllBUGTest = "ImFromRecordGetAllBUGTest",
            RecordIDForModifyTest = "ImUserIDFromModifyTest",
            RecordOriginalTitle = "Original@gmail.com",
            RecordOriginalDescription = "780-555-1234",
            RecordModifiedTitle = "Modified@gmail.com",
            RecordModifiedDescription = "587-555-9876";

    private String[] RecordTitlesToRetrieveInGetAllTest = {
            "ImTitleRecordGetAllTest1",
            "ImTitleRecordGetAllTest2",
            "ImTitleRecordGetAllTest3"
    };

    private String[] RecordTitlesToRetrieveInGetAllBUGTest = {
            "ImTitleRecordGetAllBUGTest1",
            "ImTitleRecordGetAllBUGTest2",
            "ImTitleRecordGetAllBUGTest3",
            "ImTitleRecordGetAllBUGTest4",
            "ImTitleRecordGetAllBUGTest5",
            "ImTitleRecordGetAllBUGTest6",
            "ImTitleRecordGetAllBUGTest7",
            "ImTitleRecordGetAllBUGTest8",
            "ImTitleRecordGetAllBUGTest9",
            "ImTitleRecordGetAllBUGTest10",
            "ImTitleRecordGetAllBUGTest11",
            "ImTitleRecordGetAllBUGTest12",
            "ImTitleRecordGetAllBUGTest13",
            "ImTitleRecordGetAllBUGTest14",
    };

    //set index to testing index and remove all entries from Record database
    @After
    @Before
    public void WipeRecordsDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchRecordController.DeleteRecordsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @Test
    //pass
    public void AddRecordTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        //create new record
        Record newRecord = new Record(RecordCreatedByUserIDInAddTest, "");

        //add new record to the record database
        new ElasticsearchRecordController.AddRecordTask().execute(newRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the record database
        Record record = new ElasticsearchRecordController.GetRecordByRecordIDTask()
                .execute(newRecord.getElasticSearchID()).get();

        //check that the new record is now in the database
        assertEquals("records were not equal", record.getCreatedByUserID(),
                newRecord.getCreatedByUserID());
    }

    @Test
    //pass
    public void GetRecordByRecordIDTaskTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        //create new record
        Record newRecord = new Record(RecordUserIDToGetInGetTest,"");

        //add new record to the record database
        new ElasticsearchRecordController.AddRecordTask().execute(newRecord).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the record database
        Record fetchedRecord = new ElasticsearchRecordController.GetRecordByRecordIDTask()
                .execute(newRecord.getElasticSearchID()).get();

        assertEquals("fetched record userID not equal",
                newRecord.getCreatedByUserID(), fetchedRecord.getCreatedByUserID());
    }

    @Test
    //pass
    public void getRecordsByProblemIDTest() throws ExecutionException, TitleTooLongException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        AssertRecordsCanBeAddedAndThenBatchFetched(RecordUserIDToRetrieveInGetAllTest,
                RecordTitlesToRetrieveInGetAllTest);
    }

    @Test
    //pass
    public void getRecordsByProblemIDBUGTest() throws ExecutionException, TitleTooLongException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        //check we can fetch more than 10 results at once
        AssertRecordsCanBeAddedAndThenBatchFetched(RecordUserIDToRetrieveInGetAllBUGTest,
                RecordTitlesToRetrieveInGetAllBUGTest);
    }


    private void AssertRecordsCanBeAddedAndThenBatchFetched(
            String suppliedUserID, String[] suppliedTitles)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, TitleTooLongException {
        String ProblemID = "myFakeProblemID";
        ArrayList<Record> expectedRecords = new ArrayList<>();
        ArrayList<Boolean> expectedRecordInResults = new ArrayList<>();

        //add all expected records in
        for (String title : suppliedTitles) {

            Record newRecord = new Record(suppliedUserID, title);

            //set associated problem id
            newRecord.setAssociatedProblemID(ProblemID);

            //add new record to the record database
            new ElasticsearchRecordController.AddRecordTask().execute(newRecord).get();

            //add new record to expected returns
            expectedRecords.add(newRecord);
            expectedRecordInResults.add(false);
        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //make sure each of the added users is individually fetchable
        for (int i = 0; i < suppliedTitles.length; i++) {
            //fetch new Record from the Record database
            Record record = new ElasticsearchRecordController.GetRecordByRecordIDTask()
                    .execute(expectedRecords.get(i).getElasticSearchID()).get();

            assertEquals("Fetched Record had different UserID from one added",
                    record.getCreatedByUserID(), suppliedUserID);

            assertEquals("Fetched Record Title was different from one added",
                    record.getTitle(), suppliedTitles[i]);
        }

        //Get objects from database associated to the problem id
        ArrayList<Record> results = new ElasticsearchRecordController.GetRecordsWithProblemID()
                .execute(ProblemID).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedTitles.length > 10 && results.size() == 10) {
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as records we queried. We got exactly " +
                            "ten results instead of expected " + suppliedTitles.length,
                    results.size() == suppliedTitles.length);
        }

        assertTrue("there should be as many results as records we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedTitles.length,
                results.size() == suppliedTitles.length);

        //compare results to what we expected to find.
        //The records we added should now be there
        for (Record record : results) {
            for (int i = 0; i < suppliedTitles.length; i++) {

                //track which expected records are seen in the results
                if (record.getTitle().equals(suppliedTitles[i])) {
                    expectedRecordInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected records in the results
        for (boolean recordSeenInResults : expectedRecordInResults) {
            assertTrue("Record missing from results", recordSeenInResults);
        }
    }

    @Test
    //pass
    public void modifyRecordSavesChanges() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException, TitleTooLongException {

        //setup original record
        Record record = new Record(RecordIDForModifyTest,RecordOriginalTitle);
        record.setDescription(RecordOriginalDescription);

        //add record
        new ElasticsearchRecordController.AddRecordTask().execute(record).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //modify record
        record.setTitle(RecordModifiedTitle);
        record.setDescription(RecordModifiedDescription);

        //check the object was changed and equals our modified values
        assertEquals("Original record title not modified correctly.",
                RecordModifiedTitle, record.getTitle());
        assertEquals("Original record description not modified correctly.",
                RecordModifiedDescription, record.getDescription());

        //save modification
        new ElasticsearchRecordController.SaveModifiedRecord().execute(record).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned record, hopefully modified
        Record returnedRecord = new ElasticsearchRecordController.GetRecordByRecordIDTask()
                .execute(record.getElasticSearchID()).get();

        //check the object was changed and equals our modified values
        assertEquals("record title on returned object not modified correctly.",
                RecordModifiedTitle, returnedRecord.getTitle());
        assertEquals("record description on returned object not modified correctly.",
                RecordModifiedDescription, returnedRecord.getDescription());
    }

    //tests for existence of BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/199
    @Test
    //fail
    public void modifyRecordSavesDateChangesBUG() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException, TitleTooLongException {

        //setup original record
        Record record = new Record(RecordIDForModifyTest,RecordOriginalTitle);
        record.setDescription(RecordOriginalDescription);

        //add record
        new ElasticsearchRecordController.AddRecordTask().execute(record).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        Date RecordModifiedDate = new Date(System.currentTimeMillis());

        //modify record date
        record.setDate(RecordModifiedDate);

        //check the object was changed and equals our modified values
        assertEquals("Original record date not modified correctly.",
                RecordModifiedDate.getTime(), record.getDate().getTime());

        //save modification
        new ElasticsearchRecordController.SaveModifiedRecord().execute(record).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned record, hopefully modified
        Record returnedRecord = new ElasticsearchRecordController.GetRecordByRecordIDTask()
                .execute(record.getElasticSearchID()).get();

        //date is not exact, it seems to be rounded to nearest 1000 nsec
        assertTrue(abs(RecordModifiedDate.getTime() - returnedRecord.getDate().getTime()) <= 1000);

        //BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/199
        assertEquals("BUG: https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/199 " +
                        "record date on returned object not modified correctly.",
                RecordModifiedDate.getTime(), returnedRecord.getDate().getTime());


    }
}