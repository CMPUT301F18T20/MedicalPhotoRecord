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

import com.cmput301f18t20.medicalphotorecord.Patient;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import io.searchbox.core.DeleteByQuery;

import static GlobalSettings.GlobalSettings.getIndex;
import static org.junit.Assert.*;

//add a delete type method to the controllers for use with testing
class ElasticsearchPatientControllerForTesting extends ElasticsearchPatientController {

    public void DeletePatients() throws IOException {
        setClient();

        client.execute(new DeleteByQuery.Builder(matchAllquery)
                .addIndex(getIndex())
                .addType("Patient")
                .build());
    }
}

public class ElasticsearchPatientControllerTest {

    private String
            PatientIDToAddInAddTest = "ImFromThePatientAddTest",
            PatientIDToGetInGetTest = "ImFromThePatientGetTest",
            PatientIDForUniquenessTest = "ImFromThePatientUniquenessTest";
    private String[] PatientIDsToRetrieveInGetAllTest = {
            "ImFromPatientGetAllTest1",
            "ImFromPatientGetAllTest2",
            "ImFromPatientGetAllTest3",
            "ImFromPatientGetAllTest4",
            "ImFromPatientGetAllTest5",
            "ImFromPatientGetAllTest6",
            "ImFromPatientGetAllTest7",
            "ImFromPatientGetAllTest8",
            "ImFromPatientGetAllTest9",
            "ImFromPatientGetAllTest10",
            "ImFromPatientGetAllTest11",
            "ImFromPatientGetAllTest12",
            "ImFromPatientGetAllTest13",
            "ImFromPatientGetAllTest14",
    };

    //set index to testing index and remove all entries from Patient database
    @Before
    public void WipePatientsDatabase() throws IOException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientControllerForTesting().DeletePatients();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);
    }

    @Test
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
        Thread.sleep(5000);

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
    public void PatientsHaveUniqueIDs() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        Patient newPatient = new Patient(PatientIDForUniquenessTest);

        //add same patient twice
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

        //fetch patients
        ArrayList<Patient> patients =
                new ElasticsearchPatientController.GetPatientTask().execute().get();

        assertEquals("Should only be one entry in the results",
                1, patients.size());
    }

    @Test
    public void getPatientTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new patient
        Patient newPatient = new Patient(PatientIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new patient to the patient database
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

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
    public void getPatientsTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {
        ArrayList<Patient> expectedPatients = new ArrayList<>();
        ArrayList<Boolean> expectedPatientInResults = new ArrayList<>();

        //add all expected users in
        for (String patientID : PatientIDsToRetrieveInGetAllTest) {
            Patient newPatient = new Patient(patientID);

            //add new patient to expected returns
            expectedPatients.add(newPatient);
            expectedPatientInResults.add(false);

            //add new patient to the patient database
            new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

            //Ensure database has time to reflect the change
            Thread.sleep(5000);
        }

        //Get objects from database for all the entered patient IDs
        ArrayList<Patient> results = new ElasticsearchPatientController.GetPatientTask()
                .execute(PatientIDsToRetrieveInGetAllTest).get();

        assertTrue("there should be as many results as patients we queried. We got " +
                        results.size() + " results instead of expected " +
                        PatientIDsToRetrieveInGetAllTest.length,
                results.size() == PatientIDsToRetrieveInGetAllTest.length);

        //get all users
        results = new ElasticsearchPatientController.GetPatientTask().execute().get();

        //compare results to what we expected to find.
        //The three patients we added should now be there
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

}