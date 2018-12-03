/*
 * Class name: AddDeleteRecordControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 02/12/18 7:15 PM
 *
 * Last Modified: 02/12/18 7:15 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.BrowseProblemRecords;
import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;

import Activities.AddRecordActivity;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * AddDeleteRecordControllerTest
 * Tests the methods of AddDeleteRecordController
 * @version 1.0
 * @since   2018-12-01
 *
 */

public class AddDeleteRecordControllerTest {


    /**
     * Clears the online database
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * Clears the offline database
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddRecordActivity.getActivity().getBaseContext();
        ArrayList<PatientRecord> emptyPatientRecords = new ArrayList<>();
        new OfflineSaveController().savePatientRecordLIst(emptyPatientRecords,context);

        Context context1 = BrowseProblemRecords.getActivity().getBaseContext();
        new OfflineSaveController().savePatientRecordLIst(emptyPatientRecords,context1);
    }

    @Rule
    public ActivityTestRule<AddRecordActivity> AddRecordActivity=
            new ActivityTestRule<>(AddRecordActivity.class);

    @Rule
    public ActivityTestRule<BrowseProblemRecords> BrowseProblemRecords =
            new ActivityTestRule<>(BrowseProblemRecords.class);


    /**
     * testSaveRecord
     * tests the method saveRecord inside AddDeleteRecordController
     * @throws UserIDMustBeAtLeastEightCharactersException - User ID length must be >= 8
     * @throws ExecutionException -
     * @throws InterruptedException -
     * @throws TitleTooLongException - title is too long
     */
    @Test
    public void testSaveRecord() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException {

        Context context = AddRecordActivity.getActivity().getBaseContext();

        // Create new problem
        PatientRecord expectedPatientRecord = new PatientRecord("testSaveRecord","");

        // Test saveRecord
        new AddDeleteRecordController().saveRecord(context,expectedPatientRecord);

        /*new AddDeleteProblemController().saveAddProblem(context, expectedProblem);*/
        Thread.sleep(ControllerTestTimeout);

        // Compare 3 objects, convert to gson string since date is giving some problem
        PatientRecord gotRecordOnline = new ElasticsearchPatientRecordController.GetPatientRecordByPatientRecordUUIDTask().execute(expectedPatientRecord.getUUID()).get();
        PatientRecord gotRecordOffline = new OfflinePatientRecordController().getPatientRecord(context, expectedPatientRecord.getUUID());

        String expectedRecordString = new Gson().toJson(expectedPatientRecord);
        String gotRecordOnlineString = new Gson().toJson(gotRecordOnline);
        String gotRecordOfflineString = new Gson().toJson(gotRecordOffline);

        assertEquals("added record for online are not the same", expectedRecordString, gotRecordOnlineString);
        assertEquals("added record for offline are not the same", expectedRecordString, gotRecordOfflineString);

    }


    /**
     * testDeleteRecord
     * Tests the deleteRecord method inside AddDeleteRecordController.
     *
     * @throws TitleTooLongException - title is too long
     * @throws UserIDMustBeAtLeastEightCharactersException - user ID length must be >=8
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    @Test
    public void testDeleteRecord() throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        Context context = BrowseProblemRecords.getActivity().getBaseContext();

        // Add problem to both offline and online database
        PatientRecord expectedRecord = new PatientRecord("testDeleteRecord","");
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        patientRecords.add(expectedRecord);
        new OfflinePatientRecordController().addPatientRecord(context,expectedRecord);
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(expectedRecord).get();
        Thread.sleep(ControllerTestTimeout);

        // Test deleteRecord
        new AddDeleteRecordController().deleteRecord(context, expectedRecord);
        Thread.sleep(ControllerTestTimeout);

        // Compare
        PatientRecord gotRecordOnline = new ElasticsearchPatientRecordController.GetPatientRecordByPatientRecordUUIDTask().execute(expectedRecord.getUUID()).get();
        PatientRecord gotRecordOffline = new OfflinePatientRecordController().getPatientRecord(context, expectedRecord.getUUID());
        assertNull("record should not be found in online", gotRecordOnline);
        assertNull("record should not be found in offline", gotRecordOffline);
    }
}
