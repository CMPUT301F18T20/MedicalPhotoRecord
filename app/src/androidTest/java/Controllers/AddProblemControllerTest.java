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

import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Activities.ProviderHomeMenuActivity;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;

public class AddProblemControllerTest {

    @Rule
    public ActivityTestRule<ProviderHomeMenuActivity> ProviderActivity =
            new ActivityTestRule<>(ProviderHomeMenuActivity.class);
    @Test
    public void testsaveProblem() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException {

        Context context = ProviderActivity.getActivity().getBaseContext();

        // Create new patient and problem
        Patient patient = new Patient("patientname","","");
        Problem expectedProblem = new Problem(patient.getUserID(), "problem_title");
        expectedProblem.setDate(new Date());
        expectedProblem.setDescription("problem_descriptions");

        // Save to database
        new AddProblemController().saveProblem("add", context, patient.getUserID(),
                expectedProblem.getTitle(), expectedProblem.getDate(), expectedProblem.getDescription());

        // Get from database and Compare
        Patient gotPatient = new ModifyUserController().getPatient(context, "patientname");
        Problem gotProblem = gotPatient.getProblem(0);
        assertEquals("added problems are not the same", expectedProblem, gotProblem);

    }
}
