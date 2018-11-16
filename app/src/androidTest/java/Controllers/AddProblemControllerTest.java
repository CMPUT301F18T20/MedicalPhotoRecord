/*
 * Class name: AddProblemControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 1:00 PM
 *
 * Last Modified: 11/15/18 1:00 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.cmput301f18t20.medicalphotorecord.Provider;
import com.google.gson.Gson;

import Activities.AddProblemActivity;
import Activities.ProviderHomeMenuActivity;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;


public class AddProblemControllerTest {

    @Rule
    public ActivityTestRule<AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    @Test
    @UiThreadTest
    public void testsaveProblem() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new patient and problem
        Patient patient = new Patient("patientname","","");

        Problem expectedProblem = new Problem(patient.getUserID(), "problem_title");
        expectedProblem.setDescription("problem_descriptions");

        // Save to database
        new UserController().addPatient(context, patient);
        new AddProblemController().saveProblem("add", context, expectedProblem);

        // Get from database and Compare
        Patient gotPatient = new ModifyUserController().getPatient(context, patient.getUserID());
        Problem gotProblem = gotPatient.getProblem(gotPatient.getProblems().size()-1);

        // Compare 2 objects, convert to gson string since date is giving some problem
        String expectedProblemString = new Gson().toJson(expectedProblem);
        String gotProblemString = new Gson().toJson(gotProblem);
        assertEquals("added problems are not the same", expectedProblemString, gotProblemString);


    }
}
