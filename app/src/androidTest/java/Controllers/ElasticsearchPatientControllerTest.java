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

    //set index to testing index and remove all entries from Patient database
    @After
    @Before
    public void WipePatientsDatabase() throws IOException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientControllerForTesting().DeletePatients();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @Test
    //pass
    public void AddPatientTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, IOException {

        //create new Patient
        Patient newPatient = new Patient(PatientIDToAddInAddTest);

        //fetch Patients from database
        ArrayList<Patient> Patients =
                new ElasticsearchPatientController.GetPatientTask().execute().get();

        //for entry in Patients:
        for (Patient Patient : Patients) {

            //assert entry doesn't have our userID
            assertNotEquals("UserID already in database",
                    Patient.getUserID(), newPatient.getUserID());
        }

        //add new Patient to the Patient database
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the Patient database
        Patients = new ElasticsearchPatientController.GetPatientTask().execute(newPatient.getUserID()).get();

        //check that the new Patient is now in the database
        boolean newPatientInDatabase = false;
        for (Patient Patient : Patients) {
            if (Patient.getUserID().equals(newPatient.getUserID())) {
                newPatientInDatabase = true;
                break;
            }
        }

        assertEquals("New Patient not in database", newPatientInDatabase, true);
    }

    @Test
    //fail
    public void PatientsHaveUniqueIDs() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        Patient newPatient = new Patient(PatientIDForUniquenessTest);

        //add same Patient twice
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch Patients
        ArrayList<Patient> Patients =
                new ElasticsearchPatientController.GetPatientTask().execute().get();

        assertEquals("Should only be one entry in the results",
                1, Patients.size());
    }

    @Test
    //pass
    public void getPatientTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new Patient
        Patient newPatient = new Patient(PatientIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new Patient to the Patient database
        new ElasticsearchPatientController.AddPatientTask().execute(newPatient).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the Patient database
        ArrayList<Patient> Patients = new ElasticsearchPatientController.GetPatientTask()
                .execute(newPatient.getUserID()).get();

        assertTrue("Patients array not at least 1 member long", Patients.size() >= 1);

        Patient fetchedPatient = Patients.get(0);

        assertEquals("fetched Patient userID not equal",
                newPatient.getUserID(), fetchedPatient.getUserID());

        assertEquals("fetched Patient email not equal",
                newPatient.getEmail(), fetchedPatient.getEmail());

        assertEquals("fetched Patient phone not equal",
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
        for (String PatientID : suppliedUserIDs) {
            Patient newPatient = new Patient(PatientID);

            //add new Patient to expected returns
            expectedPatients.add(newPatient);
            expectedPatientInResults.add(false);

            //add new Patient to the Patient database
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

        //Get objects from database for all the entered Patient IDs
        ArrayList<Patient> results = new ElasticsearchPatientController.GetPatientTask()
                .execute(suppliedUserIDs).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedUserIDs.length > 10 && results.size() == 10) {
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as Patients we queried. We got exactly" +
                            "ten results instead of expected " + suppliedUserIDs.length,
                    results.size() == suppliedUserIDs.length);
        }

        assertTrue("there should be as many results as Patients we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedUserIDs.length,
                results.size() == suppliedUserIDs.length);

        //get all users
        results = new ElasticsearchPatientController.GetPatientTask().execute().get();

        //compare results to what we expected to find.
        //The Patients we added should now be there
        for (Patient Patient : results) {
            for (int i = 0; i < expectedPatients.size(); i++) {

                //track which expected Patients are seen in the results
                if (Patient.getUserID().equals(expectedPatients.get(i).getUserID())) {
                    expectedPatientInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected Patients in the results
        for (boolean PatientSeenInResults : expectedPatientInResults) {
            assertTrue("Patient missing from results", PatientSeenInResults);
        }
    }

}