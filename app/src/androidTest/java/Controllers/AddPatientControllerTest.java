/*
 * Class name: AddPatientControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 11:42 AM
 *
 * Last Modified: 11/15/18 11:42 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddProblemActivity;
import Activities.BrowseUserActivity;
import Activities.ProviderHomeMenuActivity;
import Enums.INDEX_TYPE;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;

/**
 * AddPatientControllerTest
 * Testing for method in AddPatientController
 * @version 1.0
 * @see BrowseUserController
 */
public class AddPatientControllerTest {

    /**
     * Clear out online provider database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPatientController.DeletePatientsTask().execute().get();
        new ElasticsearchProviderController.DeleteProvidersTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    /**
     * Test for adding patient to be associated to a provider
     * @throws UserIDMustBeAtLeastEightCharactersException
     */
    @Test
    @UiThreadTest
    public void testGetUserListProvider() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new providers with patients
        String[] patientIds = {
                "Patient1000",
                "Patient2000",
                "Patient3000",
                "Patient4000",
        };

        Provider pr1 = new Provider("Provider1000", "", "");
        new ElasticsearchProviderController.AddProviderTask().execute(pr1).get();
        ArrayList<Patient> expectedPr1Patients = new ArrayList<>();

        // Add patients for provider 1 and save them to online database
        for (int i = 0; i < patientIds.length; i++){
            Patient p = new Patient(patientIds[i],"","");
            new ElasticsearchPatientController.AddPatientTask().execute(p).get();
            Thread.sleep(ControllerTestTimeout);

            new AddPatientController().addPatient(context, pr1.getUserID(), p.getUserID());
            Thread.sleep(ControllerTestTimeout);

            p.addAssociatedProviderID(pr1.getUserID());
            expectedPr1Patients.add(p);
        }

        // Get them from online database
        ArrayList<Patient> gotPr1Patients = new ElasticsearchPatientController.GetPatientsAssociatedWithProviderUserIDTask().execute(pr1.getUserID()).get();

        // Compare
        assertEquals("compare size of arraylist",expectedPr1Patients.size(), gotPr1Patients.size());

        // Has to find patient since order of list could be different
        for (int i = 0; i < expectedPr1Patients.size(); i ++){
            Patient expectedPatient = expectedPr1Patients.get(i);

            for (int j = 0; j < gotPr1Patients.size(); j++){
                Patient gotPatient = gotPr1Patients.get(i);

                // Compare the 2 objects by converting to Json string
                if (expectedPatient.getUUID() == gotPatient.getUUID()){
                    String p1 = new Gson().toJson(expectedPatient);
                    String p2 = new Gson().toJson(gotPatient);
                    assertEquals("compare each patient of patient list of provider", p1,p2);
                }
            }
        }


        // Adding non existent patient test
        new AddPatientController().addPatient(context, pr1.getUserID(), "");
        gotPr1Patients = new ElasticsearchPatientController.GetPatientsAssociatedWithProviderUserIDTask().execute(pr1.getUserID()).get();
        for (Patient p:gotPr1Patients){
            if (p.getUserID().equals("")){
                assertEquals("patient should not be added",0,1);
            }
        }
    }
}
