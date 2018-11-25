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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

import Activities.AddProblemActivity;
import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;


public class AddDeleteProblemControllerTest {

    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Problem> emptyProblems = new ArrayList<>();
        new OfflineSaveController().saveProblemList(emptyProblems, context);
    }

    @Rule
    public ActivityTestRule<AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    @Test
    public void testSaveAddProblem() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException, TitleTooLongException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Create new problem
        Problem expectedProblem = new Problem("testsaveProblemAdd","");

        // Test saveAddProblem
        new AddDeleteProblemController().saveAddProblem(context, expectedProblem);
        Thread.sleep(ControllerTestTimeout);

        // Compare 3 objects, convert to gson string since date is giving some problem
        Problem gotProblemOnline = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(expectedProblem.getUUID()).get();
        Problem gotProblemOffline = new OfflineProblemController().getProblem(context, expectedProblem.getUUID());

        String expectedProblemString = new Gson().toJson(expectedProblem);
        String gotProblemOnlineString = new Gson().toJson(gotProblemOnline);
        String gotProblemOfflineString = new Gson().toJson(gotProblemOffline);

        assertEquals("added problems for online are not the same", expectedProblemString, gotProblemOnlineString);
        assertEquals("added problems for offline are not the same", expectedProblemString, gotProblemOfflineString);

    }

    @Test
    public void testSaveDeleteProblem() throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Add problem to both offline and online database
        Problem expectedProblem = new Problem("testsaveProblemDelete","");
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(expectedProblem);
        new OfflineProblemController().addProblem(context, expectedProblem);
        new ElasticsearchProblemController.AddProblemTask().execute(expectedProblem).get();
        Thread.sleep(ControllerTestTimeout);

        // Test saveDeleteProblem
        new AddDeleteProblemController().saveDeleteProblem(context, expectedProblem);
        Thread.sleep(ControllerTestTimeout);

        // Compare
        Problem gotProblemOnline = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(expectedProblem.getUUID()).get();
        Problem gotProblemOffline = new OfflineProblemController().getProblem(context, expectedProblem.getUUID());
        assertNull("problem should not be found in online", gotProblemOnline);
        assertNull("problem should not be found in offline", gotProblemOffline);
    }
}
