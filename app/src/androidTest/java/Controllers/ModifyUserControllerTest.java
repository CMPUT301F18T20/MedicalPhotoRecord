/*
 * Class name: ModifyUserControllerTest
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

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.assertEquals;

public class ModifyUserControllerTest {

    @Test
    public void testGetUser() throws UserIDMustBeAtLeastEightCharactersException {

        // Create new patient
        Patient patient = new Patient("patientname","patientemail","1111111111");

        // Save them to database
        new ElasticsearchPatientController.AddPatientTask().execute(patient);

        // Get patient and compare
        Patient gotPatient = new ModifyUserController(context).getUser(patient.getUserID());
        assertEquals("patient got from database is not the same", patient, gotPatient);

    }

    @Test
    public void testSaveUser() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        // Create new patient and modified patient
        Patient patient = new Patient("patientname","patientemail","1111111111");
        String modEmail = "modpatientemail";
        String modPhoneNumber = "2222222222";
        Patient expectedModPatient = new Patient("patientname", modEmail, modPhoneNumber);

        // Save them to database


        // Modify and compare
        new ModifyUserController(context).saveUser(null, "patientname", modEmail, modPhoneNumber);
        Patient gotModPatient = (new ElasticsearchPatientController.GetPatientTask().execute("patientname").get()).get(0);
        assertEquals("modified patients are not the same", expectedModPatient, gotModPatient);
    }
}
