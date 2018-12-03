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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;

/**
 * BrowseUserProblemControllerTest
 * Testing for method in BrowseProblemsController
 * @version 1.0
 * @see BrowseProblemsController
 */
public class BrowseUserProblemControllerTest {

    /**
     * Clear online problem database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * Clear offline problem database
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Problem> emptyProblems = new ArrayList<>();
        new OfflineSaveController().saveProblemList(emptyProblems, context);
    }

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(Activities.AddProblemActivity.class);

    /**
     * Test if browse problem list return the actual list of problem for a patient
     * @throws UserIDMustBeAtLeastEightCharactersException
     * @throws TitleTooLongException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testGetProblemList() throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ExecutionException, InterruptedException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Add everything to online and offline databases
        Problem problem1 = new Problem("testGetProblemList","");
        Problem problem2 = new Problem("testGetProblemList","");
        Problem problem3 = new Problem("testGetProblemListWrongUser","");

        ArrayList<Problem> expectedProblems = new ArrayList<>();
        expectedProblems.add(problem1);
        expectedProblems.add(problem2);
        expectedProblems.add(problem3);

        new ElasticsearchProblemController.AddProblemTask().execute(problem1).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchProblemController.AddProblemTask().execute(problem2).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchProblemController.AddProblemTask().execute(problem3).get();
        Thread.sleep(ControllerTestTimeout);
        new OfflineSaveController().saveProblemList(expectedProblems, context);


        // Test getProblemList() (online and offline, will have to fix after syncing issue is done)
        ArrayList<Problem> gotOnlineProblems = new BrowseProblemsController().getProblemList(context,"testGetProblemList");
        ArrayList<Problem> gotOfflineProblems = new BrowseProblemsController().getProblemList(context, "testGetProblemList");

        // Compare by converting objects to json string because of date issue
        for (int i = 0; i < 2; i ++){
            String p1 = new Gson().toJson(expectedProblems.get(i));
            String p2 = new Gson().toJson(gotOnlineProblems.get(i));
            String p3 = new Gson().toJson(gotOfflineProblems.get(i));
            assertEquals("problem list is not the same for online ", p2,p1);
            assertEquals("problem list is not the same for offline ", p3,p1);
        }
    }
}
