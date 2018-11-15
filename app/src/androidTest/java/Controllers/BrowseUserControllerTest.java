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

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import org.junit.Test;

import java.util.ArrayList;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static junit.framework.TestCase.assertEquals;

public class BrowseUserControllerTest {

    // Test for getting the most updated user list for certain provider
    @Test
    public void testGetUserListProvider() throws UserIDMustBeAtLeastEightCharactersException {

        // Create new providers with patients
        String[] patientIds = {
                "getUserListProviderPatient1",
                "getUserListProviderPatient2",
                "getUserListProviderPatient3",
                "getUserListProviderPatient4",
                "getUserListProviderPatient5",
        };

        Provider pr1 = new Provider("getUserListProviderProvider1", "", "");
        Provider pr2 = new Provider("getUserListProviderProvider2", "", "");

        ArrayList<Patient> expectedPr1Patients = new ArrayList<>();
        ArrayList<Patient> expectedPr2Patients = new ArrayList<>();

        // Add patients for provider 1
        for (int i = 0; i < patientIds.length; i++){
            Patient p = new Patient(patientIds[i]);
            pr1.assignPatient(p);
            expectedPr1Patients.add(p);
        }

        // Save them to database (online for now)
        ArrayList<Patient> gotPr1Patients = new BrowseUserController().getUserListProvider(null,pr1.getUserID());
        ArrayList<Patient> gotPr2Patients = new BrowseUserController().getUserListProvider(null, pr2.getUserID());

        // Check
        assertEquals("provider 1 list of patients size 5", expectedPr1Patients, gotPr1Patients);
        assertEquals("provider 2 list of patients size 0", expectedPr2Patients, gotPr2Patients);
    }
}
