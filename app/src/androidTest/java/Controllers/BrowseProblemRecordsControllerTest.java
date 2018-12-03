/*
 * Class name: BrowseProblemRecordsControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 02/12/18 8:59 PM
 *
 * Last Modified: 02/12/18 8:59 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;
import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;
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
import static org.junit.Assert.assertEquals;

/**
 * BrowseProblemRecordsControllerTest
 * Testing methods for BrowseProblemRecordsController
 *
 * @version 1.0
 * @since   2018-12-01
 *
 */

public class BrowseProblemRecordsControllerTest {

    /**
     * Clear online databases
     * @throws ExecutionException -
     * @throws InterruptedException -
     */

    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute().get();
        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * clears offline databases
     */

    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddRecordActivity.getActivity().getBaseContext();
        ArrayList<PatientRecord> emptyRecords = new ArrayList<>();
        new OfflineSaveController().savePatientRecordLIst(emptyRecords,context);

        Context context1 = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Problem> emptyProblems = new ArrayList<>();
        new OfflineSaveController().saveProblemList(emptyProblems, context1);
    }

    @Rule
    public ActivityTestRule<AddRecordActivity> AddRecordActivity =
            new ActivityTestRule<>(AddRecordActivity.class);

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(Activities.AddProblemActivity.class);

    /**
     * testGetPatientRecords
     * Tests the getPatientRecords methond inside BrowseProblemRecordsController
     * Checks if the expected records match the offline and online records.
     *
     * @throws UserIDMustBeAtLeastEightCharactersException - User ID length must be >= 8
     * @throws TitleTooLongException - title too long
     * @throws ExecutionException =
     * @throws InterruptedException -
     */
    @Test
    public void testGetPatientRecords() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ExecutionException, InterruptedException {

        Context context = AddRecordActivity.getActivity().getBaseContext();
        Context context1 = AddProblemActivity.getActivity().getBaseContext();

        Problem problem1 = new Problem("testGetPatientRecords", "");
        new ElasticsearchProblemController.AddProblemTask().execute(problem1).get();
        Thread.sleep(ControllerTestTimeout);
        // Add everything to online and offline databases
        PatientRecord record1 = new PatientRecord("testGetPatientRecords","");
        record1.setAssociatedProblemUUID(problem1.getUUID());
        PatientRecord record2 = new PatientRecord("testGetPatientRecords","");
        record2.setAssociatedProblemUUID(problem1.getUUID());
        PatientRecord record3 = new PatientRecord("testGetPatientRecords","");
        record3.setAssociatedProblemUUID(problem1.getUUID());

        ArrayList<PatientRecord> expectedRecords = new ArrayList<>();
        expectedRecords.add(record1);
        expectedRecords.add(record2);
        expectedRecords.add(record3);

        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem1);

        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(record1).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(record2).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(record3).get();
        Thread.sleep(ControllerTestTimeout);


        new OfflineSaveController().savePatientRecordLIst(expectedRecords, context);
        new OfflineSaveController().saveProblemList(problems, context1);

        // Test getPatientRecords()
        ArrayList<PatientRecord> OnlineRecord = new BrowseProblemRecordsController().getPatientRecords(context, problem1.getUUID(), "testGetPatientRecords");
        ArrayList<PatientRecord> offlineRecords = new BrowseProblemRecordsController().getPatientRecords(context,problem1.getUUID(),"testGetPatientRecords" );

        // Compare by converting objects to json string because of date issue
        for (int i = 0; i < 2; i ++){
            String p1 = new Gson().toJson(expectedRecords.get(i));
            String p2 = new Gson().toJson(OnlineRecord.get(i));
            String p3 = new Gson().toJson(offlineRecords.get(i));
            assertEquals("patient records list is not the same for online ", p2,p1);
            assertEquals("patient records list is not the same for online ", p3,p1);

        }
    }

}
