/*
 * Class name: BrowseUserProblemControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 12:51 PM
 *
 * Last Modified: 11/15/18 12:51 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import Activities.AddProblemActivity;
import Activities.BrowseUserProblems;
import Activities.ProviderHomeMenuActivity;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;

public class BrowseUserProblemControllerTest {

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(Activities.AddProblemActivity.class);

    @Test
    public void testGetProblemList() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new providers with patients
        String[] problemTitles = {
                "getProbp1",
                "getProbp2",
                "getProbp3",
                "getProbp4",
                "getProbp5",

        };

        Patient p = new Patient("getNewProblemPatient", "", "");
        ArrayList<Problem> expectedP1Problems = new ArrayList<>();

        // Add problems for patient
        for (int i = 0; i < problemTitles.length; i++){
            Problem pl = new Problem(p.getUserID(), problemTitles[i]);
            p.addProblem(pl);
            expectedP1Problems.add(pl);
        }

        // Save them to database
        new UserController().addPatient(context, p);

        // Get them from database
        ArrayList<Problem> gotP1Problems= new BrowseUserProblemsController().getProblemList(context, p.getUserID());

        // Check
        // Converting objects to json string because of date issue
        for (int i = 0; i < expectedP1Problems.size(); i ++){
            String p1 = new Gson().toJson(expectedP1Problems.get(i));
            String p2 = new Gson().toJson(gotP1Problems.get(i));
            assertEquals("compare patient list of problems size 5", p2,p1);
        }
    }
}
