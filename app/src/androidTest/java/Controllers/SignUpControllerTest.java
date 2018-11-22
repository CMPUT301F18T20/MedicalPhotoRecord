/*
 * Class name: SignUpControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 16/11/18 9:40 AM
 *
 * Last Modified: 16/11/18 9:40 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.SignUp;
import Exceptions.NoConnectionInSignUpException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static Enums.INDEX_TYPE.TEST;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class SignUpControllerTest {

    @Rule
    public ActivityTestRule<SignUp> SignUpActivity =
            new ActivityTestRule<>(SignUp.class);

    private String
            PatientIDForUniquenessTest = "ImFromThePatientUniquenessTest",
            ProviderIDForUniquenessTest = "ImFromTheProviderUniquenessTest",
            EmailForUniquenessTest = "User@gmail.com",
            PhoneForUniquenessTest = "780-555-7666";

    @Before
    public void setUp() throws ExecutionException, InterruptedException {
        //switch to test index
        GlobalSettings.INDEXTYPE = TEST;

        new ElasticsearchProviderController.DeleteProvidersTask().execute().get();
        new ElasticsearchPatientController.DeletePatientsTask().execute().get(); //TODO clear this type once the delete function is merged

    }

    @Test
    //passes
    public void PatientsHaveUniqueIDs() throws UserIDMustBeAtLeastEightCharactersException,
            NoConnectionInSignUpException, InterruptedException, ExecutionException,
            UserAlreadyExistsException {

        Context context = SignUpActivity.getActivity().getBaseContext();

        //sign up
        SignUpController.addPatient(PatientIDForUniquenessTest,
                EmailForUniquenessTest,
                PhoneForUniquenessTest,
                context);

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        try {
            //try to add same patient again
            SignUpController.addPatient(PatientIDForUniquenessTest,
                    EmailForUniquenessTest,
                    PhoneForUniquenessTest,
                    context);
            fail("No UserAlreadyExistsException raised");

        } catch (UserAlreadyExistsException e) {
            //expected that exception will be thrown when adding user twice

            //Ensure database has time to reflect the change
            Thread.sleep(ControllerTestTimeout);

            //fetch patients
            ArrayList<Patient> patients =
                    new ElasticsearchPatientController.GetPatientTask().execute().get();

            assertEquals("Should only be one entry in the results",
                    1, patients.size());
        }
    }

    @Test
    //passes
    public void ProvidersHaveUniqueIDs() throws UserIDMustBeAtLeastEightCharactersException,
            NoConnectionInSignUpException, InterruptedException, ExecutionException,
            UserAlreadyExistsException {

        Context context = SignUpActivity.getActivity().getBaseContext();

        //sign up
        SignUpController.addProvider(ProviderIDForUniquenessTest,
                EmailForUniquenessTest,
                PhoneForUniquenessTest,
                context);

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        try {
            //try to add same provider again
            SignUpController.addProvider(ProviderIDForUniquenessTest,
                    EmailForUniquenessTest,
                    PhoneForUniquenessTest,
                    context);
            fail("No UserAlreadyExistsException raised");

        } catch (UserAlreadyExistsException e) {
            //expected that exception will be thrown when adding user twice

            //Ensure database has time to reflect the change
            Thread.sleep(ControllerTestTimeout);

            //fetch providers
            ArrayList<Provider> providers =
                    new ElasticsearchProviderController.GetProviderTask().execute().get();

            assertEquals("Should only be one entry in the results",
                    1, providers.size());
        }
    }
}