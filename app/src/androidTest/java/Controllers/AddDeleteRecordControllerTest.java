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

import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;

import Activities.AddRecordActivity;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.assertEquals;

public class AddDeleteRecordControllerTest {

    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddRecordActivity.getActivity().getBaseContext();
        ArrayList<PatientRecord> emptyPatientRecords = new ArrayList<>();
        new OfflineSaveController().savePatientRecordLIst(emptyPatientRecords,context);
    }

    @Rule
    public ActivityTestRule<AddRecordActivity> AddRecordActivity=
            new ActivityTestRule<>(AddRecordActivity.class);


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
}