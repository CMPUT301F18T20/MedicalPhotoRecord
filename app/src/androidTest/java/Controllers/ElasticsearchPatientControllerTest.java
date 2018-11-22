/*
 * Class name: ElasticsearchPatientControllerForTesting
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

import com.cmput301f18t20.medicalphotorecord.Patient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import io.searchbox.core.DeleteByQuery;

import static GlobalSettings.GlobalSettings.getIndex;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.*;

public class ElasticsearchPatientControllerTest {

    private String
            PatientIDToAddInAddTest = "ImFromThePatientAddTest",
            PatientIDToGetInGetTest = "ImFromThePatientGetTest";
    private String[] PatientIDsToRetrieveInGetAllTest = {
            "ImFromPatientGetAllTest1",
            "ImFromPatientGetAllTest2",
            "ImFromPatientGetAllTest3"
    };

    private String[] PatientIDsToRetrieveInGetAllBUGTest = {
            "ImFromPatientGetAllBUGTest1",
            "ImFromPatientGetAllBUGTest2",
            "ImFromPatientGetAllBUGTest3",
            "ImFromPatientGetAllBUGTest4",
            "ImFromPatientGetAllBUGTest5",
            "ImFromPatientGetAllBUGTest6",
            "ImFromPatientGetAllBUGTest7",
            "ImFromPatientGetAllBUGTest8",
            "ImFromPatientGetAllBUGTest9",
            "ImFromPatientGetAllBUGTest10",
            "ImFromPatientGetAllBUGTest11",
            "ImFromPatientGetAllBUGTest12",
            "ImFromPatientGetAllBUGTest13",
            "ImFromPatientGetAllBUGTest14",
    };

    private String
            PatientIDForModifyTest = "ImFromModifyTest",
            PatientOriginalEmail = "Original@gmail.com",
            PatientOriginalPhone = "780-555-1234",
            PatientModifiedEmail = "Modified@gmail.com",
            PatientModifiedPhone = "587-555-9876";


    //set index to testing index and remove all entries from Patient database
    @After
    @Before
    public void WipePatientsDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientController.DeletePatientsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @Test
    //pass
    public void AddPatientTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, IOException {

        //create new patient
        Patient newPatient = new Patient(PatientIDToAddInAddTest);

        //fetch patients from database
        ArrayList<Patient> patients =
                new ElasticsearchPatientController.GetPatientTask().execute().get();

        //for entry in patients:
        for (Patient patient : patients) {

            //assert entry doesn't have our userID
            assertNotEquals("UserID already in database",
                    patient.getUserID(), newPatient.getUserID());
        }

        //add new patient to the patient database
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the patient database
        patients = new ElasticsearchPatientController.GetPatientTask().execute(newPatient.getUserID()).get();

        //check that the new patient is now in the database
        boolean newPatientInDatabase = false;
        for (Patient patient : patients) {
            if (patient.getUserID().equals(newPatient.getUserID())) {
                newPatientInDatabase = true;
                break;
            }
        }

        assertEquals("New Patient not in database", newPatientInDatabase, true);
    }

    @Test
    //pass
    public void getPatientTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new patient
        Patient newPatient = new Patient(PatientIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new patient to the patient database
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the patient database
        ArrayList<Patient> patients = new ElasticsearchPatientController.GetPatientTask()
                .execute(newPatient.getUserID()).get();

        assertTrue("patients array not at least 1 member long", patients.size() >= 1);

        Patient fetchedPatient = patients.get(0);

        assertEquals("fetched patient userID not equal",
                newPatient.getUserID(), fetchedPatient.getUserID());

        assertEquals("fetched patient email not equal",
                newPatient.getEmail(), fetchedPatient.getEmail());

        assertEquals("fetched patient phone not equal",
                newPatient.getPhoneNumber(), fetchedPatient.getPhoneNumber());
    }

    @Test
    //pass
    public void getPatientsTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        AssertPatientsCanBeAddedAndThenBatchFetched(PatientIDsToRetrieveInGetAllTest);
    }

    @Test
    //fail
    public void getPatientsBUGTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        //Can't fetch more than 10 results right now, this checks for the existence of that bug
        AssertPatientsCanBeAddedAndThenBatchFetched(PatientIDsToRetrieveInGetAllBUGTest);
    }

    private void AssertPatientsCanBeAddedAndThenBatchFetched(@NonNull String[] suppliedUserIDs)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException {
        ArrayList<Patient> expectedPatients = new ArrayList<>();
        ArrayList<Boolean> expectedPatientInResults = new ArrayList<>();

        //add all expected users in
        for (String patientID : suppliedUserIDs) {
            Patient newPatient = new Patient(patientID);

            //add new patient to expected returns
            expectedPatients.add(newPatient);
            expectedPatientInResults.add(false);

            //add new patient to the patient database
            new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();
        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //make sure each of the added users is individually fetchable
        for (int i = 0; i < suppliedUserIDs.length; i++) {
            //fetch new Patient from the Patient database
            ArrayList<Patient> Patients = new ElasticsearchPatientController
                    .GetPatientTask().execute(suppliedUserIDs[i]).get();

            //grab the first Patient from the result (test will fail if there's no
            //results in output, which is good)
            Patient Patient = Patients.get(0);

            assertEquals("Fetched Patient was different from one added",
                    Patient.getUserID(), expectedPatients.get(i).getUserID());

        }

        //Get objects from database for all the entered patient IDs
        ArrayList<Patient> results = new ElasticsearchPatientController.GetPatientTask()
                .execute(suppliedUserIDs).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedUserIDs.length > 10 && results.size() == 10) {
            assertTrue("IF SEEN, REOPEN BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as patients we queried. We got exactly " +
                            "ten results instead of expected " + suppliedUserIDs.length,
                    results.size() == suppliedUserIDs.length);
        }

        assertTrue("there should be as many results as patients we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedUserIDs.length,
                results.size() == suppliedUserIDs.length);

        //get all users
        results = new ElasticsearchPatientController.GetPatientTask().execute().get();

        //compare results to what we expected to find.
        //The patients we added should now be there
        for (Patient patient : results) {
            for (int i = 0; i < expectedPatients.size(); i++) {

                //track which expected patients are seen in the results
                if (patient.getUserID().equals(expectedPatients.get(i).getUserID())) {
                    expectedPatientInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected patients in the results
        for (boolean patientSeenInResults : expectedPatientInResults) {
            assertTrue("Patient missing from results", patientSeenInResults);
        }
    }

    @Test
    public void modifyPatientSavesChanges() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException {
        Patient patient = new Patient(PatientIDForModifyTest,
                PatientOriginalEmail,
                PatientOriginalPhone);

        new ElasticsearchPatientController.AddPatientTask().execute(patient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //modify user
        patient.setEmail(PatientModifiedEmail);
        patient.setPhoneNumber(PatientModifiedPhone);

        //save modification
        new ElasticsearchPatientController.SaveModifiedPatient().execute(patient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned patient, hopefully modified
        Patient returnedPatient = new ElasticsearchPatientController.
                GetPatientTask().execute(patient.getUserID()).get().get(0);

        //check the object was changed
        assertEquals(returnedPatient.getEmail(), PatientModifiedEmail);
        assertEquals(returnedPatient.getPhoneNumber(), PatientModifiedPhone);
    }

}