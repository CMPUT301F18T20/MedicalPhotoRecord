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
import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import Activities.ProviderHomeMenuActivity;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;

public class ModifyPatientControllerTest {

    @Rule
    public ActivityTestRule<ProviderHomeMenuActivity> ProviderActivity =
            new ActivityTestRule<>(ProviderHomeMenuActivity.class);

    @Test
    @UiThreadTest
    public void testGetUser() throws UserIDMustBeAtLeastEightCharactersException {

        Context context = ProviderActivity.getActivity().getBaseContext();

        // Create new patient
        Patient patient = new Patient("testGetUser()","patientemail","1111111111");

        // Save them to database
        new UserController().addPatient(context, patient);

        // Get patient and compare
        Patient gotPatient = new ModifyPatientController().getPatient(context, patient.getUserID());

        // Compare 2 objects, convert to gson string since date is giving some problem
        String patientString = new Gson().toJson(patient);
        String gotPatientString = new Gson().toJson(gotPatient);
        assertEquals("patient got from database is not the same", patientString, gotPatientString);

    }

    @Test
    @UiThreadTest
    public void testSaveUser() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        Context context = ProviderActivity.getActivity().getBaseContext();

        // Create new patient and modified patient
        Patient expectedModPatient = new Patient("anothername","pati","1111111111");
        String modEmail = "modpatientemail";
        String modPhoneNumber = "2222222222";

        // Save them to database
        new UserController().addPatient(context, expectedModPatient);

        // Modify and compare
        new ModifyPatientController().saveModifyPatient(context,expectedModPatient, modEmail, modPhoneNumber);
        Patient gotModPatient = new ModifyPatientController().getPatient(context, expectedModPatient.getUserID());

        // Compare 2 objects, convert to gson string since date is giving some problem
        String expectedModPatientString = new Gson().toJson(expectedModPatient);
        String gotModPatientString = new Gson().toJson(gotModPatient);
        assertEquals("modified patients are not the same", expectedModPatientString, gotModPatientString);
    }
}
