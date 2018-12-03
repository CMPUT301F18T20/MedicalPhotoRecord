/*
 * Class name: ProviderRecordsControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 03/12/18 1:40 AM
 *
 * Last Modified: 03/12/18 1:40 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddRecordActivity;
import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;

/**
 * ProviderRecordsControllerTest
 * Testing methods for ProviderRecordsController
 *
 * @version 1.0
 * @since   2018-12-01
 *
 */


public class ProviderRecordsControllerTest {

    /**
     *  Clears online database for problems and records
     * @throws ExecutionException -
     * @throws InterruptedException -
     */

    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchRecordController.DeleteRecordsTask().execute().get();
        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * clears offline databeses for problems and records
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddRecordActivity.getActivity().getBaseContext();
        ArrayList<Record> emptyRecords = new ArrayList<>();
        new OfflineSaveController().saveRecordList(emptyRecords, context);

        Context context1 = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Problem> emptyProblems = new ArrayList<>();
        new OfflineSaveController().saveProblemList(emptyProblems, context1);
    }

    @Rule
    public ActivityTestRule<AddRecordActivity> AddRecordActivity =
            new ActivityTestRule<>(Activities.AddRecordActivity.class);

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(Activities.AddProblemActivity.class);

    /**
     * testGetRecords
     * Tests getRecords method inside ProviderRecordsController
     * @throws UserIDMustBeAtLeastEightCharactersException - User ID length must be >= 8
     * @throws TitleTooLongException - title too long
     * @throws ExecutionException -
     * @throws InterruptedException -
     */

    @Test
    public void testGetRecords() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ExecutionException, InterruptedException {

        Context context = AddRecordActivity.getActivity().getBaseContext();
        Context context1 = AddProblemActivity.getActivity().getBaseContext();

        Problem problem1 = new Problem("testGetRecords", "");
        new ElasticsearchProblemController.AddProblemTask().execute(problem1).get();
        Thread.sleep(ControllerTestTimeout);
        // Add everything to online and offline databases
        Record record1 = new Record("testGetRecords","");
        record1.setAssociatedProblemUUID(problem1.getUUID());
        Record record2 = new Record("testGetRecords","");
        record2.setAssociatedProblemUUID(problem1.getUUID());
        Record record3 = new Record("testGetRecordsProvider","");
        record3.setAssociatedProblemUUID(problem1.getUUID());

        ArrayList<Record> expectedRecords = new ArrayList<>();
        expectedRecords.add(record1);
        expectedRecords.add(record2);
        expectedRecords.add(record3);

        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem1);

        new ElasticsearchRecordController.AddRecordTask().execute(record1).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchRecordController.AddRecordTask().execute(record2).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchRecordController.AddRecordTask().execute(record3).get();
        Thread.sleep(ControllerTestTimeout);

        new OfflineSaveController().saveRecordList(expectedRecords, context);
        new OfflineSaveController().saveProblemList(problems, context1);

        // Test getPatientRecords()
        ArrayList<Record> onlineRecord = new ProviderRecordsController().getRecords(context, problem1.getUUID(), "testGetRecords");


        // Compare by converting objects to json string because of date issue
        for (int i = 0; i < 2; i ++){
            String p1 = new Gson().toJson(expectedRecords.get(i));
            String p2 = new Gson().toJson(onlineRecord.get(i));
            assertEquals("records list is not the same for online ", p2,p1);


        }
    }

}
