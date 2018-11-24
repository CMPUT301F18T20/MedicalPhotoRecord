/*
 * Class name: AddDeleteProblemControllerTest
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

import Activities.AddProblemActivity;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;


public class AddDeleteProblemControllerTest {

    @Rule
    public ActivityTestRule<AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    @Test
    @UiThreadTest
    public void testsaveProblemAdd() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new patient and problem
        Patient patient = new Patient("patientname","","");

        Problem expectedProblem = new Problem(patient.getUserID(), "problem_title");
        expectedProblem.setDescription("problem_descriptions");

        // Save to database
        new UserController().addPatient(context, patient);
        new AddDeleteProblemController().saveAddProblem(context, expectedProblem);

        // Get from database and Compare
        Patient gotPatient = new ModifyPatientController().getPatient(context, patient.getUserID());
        Problem gotProblem = gotPatient.getProblem(gotPatient.getProblems().size()-1);

        // Compare 2 objects, convert to gson string since date is giving some problem
        String expectedProblemString = new Gson().toJson(expectedProblem);
        String gotProblemString = new Gson().toJson(gotProblem);
        assertEquals("added problems are not the same", expectedProblemString, gotProblemString);

    }

    @Test
    public void testSaveProblemDelete() throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException {
        Context context = AddProblemActivity.getActivity().getBaseContext();

        Patient patient1 = new Patient("patient1nameunique","","");
        ArrayList<Problem> expectedProblems = new ArrayList<>();

        String[] problemIds = {
                "deleteProblem1",
                "deleteProblem2",
                "deleteProblem3",
                "deleteProblem4",
        };

        // Adding problems for expected Problem
        for (int i = 0; i < problemIds.length - 2; i ++){
            Problem pr1 = new Problem(patient1.getUserID(), problemIds[i]);
            expectedProblems.add(pr1);
        }

        // Adding problems for patient list of problem
        for (String prId : problemIds){
            Problem pr1 = new Problem(patient1.getUserID(), prId);
            patient1.addProblem(pr1);
        }

        // Save patient to database
        new UserController().addPatient(context, patient1);

        // Remove problem from patient list of problem
        Problem removedProblem = new Problem(patient1.getUserID(), "deleteProblem4");
        new AddDeleteProblemController().saveDeleteProblem(context,removedProblem);
        ArrayList<Problem> gotProblems = new BrowseProblemsController().getProblemList(context, patient1.getUserID());

        // Converting objects to json string because of date issue
        for (int i = 0; i < expectedProblems.size(); i ++){
            String p1 = new Gson().toJson(expectedProblems.get(i));
            String p2 = new Gson().toJson(gotProblems.get(i));
            assertEquals("compare removed problems size 4", p2,p1);
        }

    }
}
