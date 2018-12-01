/*
 * Class name: ModifyPatientControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 11:39 AM
 *
 * Last Modified: 11/15/18 11:39 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddProblemActivity;
import Enums.INDEX_TYPE;
import Exceptions.NoSuchUserException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;

public class ModifyPatientControllerTest {

    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientController.DeletePatientsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Patient> emptyPatients = new ArrayList<>();
        new OfflineSaveController().savePatientList(emptyPatients, context);
    }

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    @Test
    public void testGetPatient() throws
            UserIDMustBeAtLeastEightCharactersException, ExecutionException,
            InterruptedException, NoSuchUserException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new patient
        Patient patient = new Patient("testGetUserLonger","patientemail","1111111111");

        // Save them to online and offline database
        new ElasticsearchPatientController.AddPatientTask().execute(patient).get();
        Thread.sleep(ControllerTestTimeout);
        new OfflinePatientController().addPatient(context, patient);

        // Test getPatient (online and offline, will have to fix after syncing issue is done)
        Patient gotOnlinePatient = new ModifyPatientController().getPatient(context, patient.getUserID());
        Patient gotOfflinePatient = new ModifyPatientController().getPatient(context, patient.getUserID());

        // Compare 3 objects, convert to gson string since date is giving some problem
        String patientString = new Gson().toJson(patient);
        String gotOnlinePatientString = new Gson().toJson(gotOnlinePatient);
        String gotOfflinePatientString = new Gson().toJson(gotOfflinePatient);
        assertEquals("patient got from online database is not the same", patientString, gotOnlinePatientString);
        assertEquals("patient got from offline database is not the same", patientString, gotOfflinePatientString);

    }

    @Test
    public void testSaveModifyPatient() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new patient and modified patient
        Patient patient = new Patient("testSaveModifyPatient","","");
        String modEmail = "modEmail";
        String modPhoneNumber = "2222222222";

        // Save them to online and offline database
        new ElasticsearchPatientController.AddPatientTask().execute(patient).get();
        Thread.sleep(ControllerTestTimeout);
        new OfflinePatientController().addPatient(context, patient);

        // Test saveModifyPatient
        new ModifyPatientController().saveModifyPatient(context,patient, modEmail, modPhoneNumber);
        Thread.sleep(ControllerTestTimeout);

        // Compare 3 objects, convert to gson string since date is giving some problem
        patient.setEmail(modEmail);
        patient.setPhoneNumber(modPhoneNumber);

        Patient gotOnlinePatient = (new ElasticsearchPatientController.GetPatientTask().execute(patient.getUserID()).get()).get(0);
        Patient gotOfflinePatient = new OfflinePatientController().getPatient(context, patient.getUserID());

        String modPatientString = new Gson().toJson(patient);
        String gotOnlinePatientString = new Gson().toJson(gotOnlinePatient);
        String gotOfflinePatientString = new Gson().toJson(gotOfflinePatient);
        assertEquals("modified online patients are not the same", modPatientString, gotOnlinePatientString);
        assertEquals("modified offline patients are not the same", modPatientString, gotOfflinePatientString);
    }
}
