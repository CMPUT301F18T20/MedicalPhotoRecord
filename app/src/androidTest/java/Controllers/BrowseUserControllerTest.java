/*
 * Class name: BrowseUserControllerTest
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

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import Activities.AddProblemActivity;
import Activities.BrowseUserActivity;
import Activities.ProviderHomeMenuActivity;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;

public class BrowseUserControllerTest {

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    // Test for getting the most updated user list for certain provider
    @Test
    @UiThreadTest
    public void testGetUserListProvider() throws UserIDMustBeAtLeastEightCharactersException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new providers with patients
        String[] patientIds = {
                "Patient1000",
                "Patient2000",
                "Patient3000",
                "Patient4000",
                "Patient5000",
        };

        Provider pr1 = new Provider("Provider1000", "", "");
        Provider pr2 = new Provider("Provider2000", "", "");

        ArrayList<Patient> expectedPr1Patients = new ArrayList<>();
        ArrayList<Patient> expectedPr2Patients = new ArrayList<>();

        // Add patients for provider 1
        for (int i = 0; i < patientIds.length; i++){
            Patient p = new Patient(patientIds[i],"","");

            // assign to provider, add to expected list, save to database
            pr1.assignPatient(p);
            expectedPr1Patients.add(p);
            new UserController().addPatient(context, p);
        }

        // Save them to database
        new UserController().addProvider(context, pr1);
        new UserController().addProvider(context, pr2);

        // Get them from database
        ArrayList<Patient> gotPr1Patients = new BrowseUserController().getPatientListOfProvider(context,pr1.getUserID());
        ArrayList<Patient> gotPr2Patients = new BrowseUserController().getPatientListOfProvider(context, pr2.getUserID());

        // Converting objects to json string because of date issue
        for (int i = 0; i < expectedPr1Patients.size(); i ++){
            String p1 = new Gson().toJson(expectedPr1Patients.get(i));
            String p2 = new Gson().toJson(gotPr1Patients.get(i));
            assertEquals("compare each patient of provider 1 list of patients size 5", p2,p1);
        }
        assertEquals("provider 2 list of patients size 0", expectedPr2Patients.size(), gotPr2Patients.size());
    }
}
