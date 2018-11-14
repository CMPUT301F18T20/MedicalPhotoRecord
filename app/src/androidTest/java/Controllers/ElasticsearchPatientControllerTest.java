/*
 * Class name: ElasticsearchPatientControllerForTesting
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 13/11/18 7:56 PM
 *
 * Last Modified: 13/11/18 7:50 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import com.cmput301f18t20.medicalphotorecord.Patient;

import org.junit.Before;
import org.junit.Test;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.core.Delete;

import static org.junit.Assert.*;

//add a delete index method to the controllers for use with testing
class ElasticsearchPatientControllerForTesting extends ElasticsearchPatientController {
    //TODO REFACTOR INDEX CHOICE TO GLOBALSETTINGS

    public void DeletePatients() throws IOException {
        setClient();
        client.execute(new Delete.Builder("1")
                .index("cmput301f18t20test") //TODO set by global settings
                .type("Patient")
                .build());
    }
}

public class ElasticsearchPatientControllerTest {

    private String PatientIDToAddInAddTest = "ImFromThePatientAddTest";
    private String PatientIDToGetInGetTest = "ImFromTheGetTest";
    private String[] PatientIDsToRetrieveInGetAllTest = {
            "ImFromGetAllTest1",
            "ImFromGetAllTest2",
            "ImFromGetAllTest3"
    };

    ElasticsearchPatientControllerForTesting controller =
            new ElasticsearchPatientControllerForTesting();

    //@Before
    //TODO can't seem to get this to work.. does it HAVE to be an async task?
    public void WipePatientsDatabase() throws IOException {
        controller.DeletePatients();
    }

    @Test
    public void AddPatientTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {

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
    public void PatientsHaveUniqueIDs() throws ExecutionException, InterruptedException {
        //should be executed on main index as that should have more examples
        ArrayList<Patient> patients =
                new ElasticsearchPatientController.GetPatientTask().execute().get();

        //patientSet will contain only the unique elements of patients
        HashSet<Patient> patientsSet = new HashSet<>(patients);

        assertEquals("patients and patientsSet were not the same size",
                patients.size(), patientsSet.size());

        fail("This should not be passing yet and the fact that it is makes me suspicious");

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