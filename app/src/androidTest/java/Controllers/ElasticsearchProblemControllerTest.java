/*
 * Class name: ElasticsearchProblemControllerForTesting
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 13/11/18 8:00 PM
 *
 * Last Modified: 13/11/18 6:48 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.support.annotation.NonNull;

import com.cmput301f18t20.medicalphotorecord.Problem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import io.searchbox.core.DeleteByQuery;

import static GlobalSettings.GlobalSettings.getIndex;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.*;


public class ElasticsearchProblemControllerTest {

    private String
            ProblemCreatedByUserIDInAddTest = "ImFromTheProblemAddTest",
            ProblemIDToGetInGetTest = "ImFromTheProblemGetTest",
            ProblemIDForUniquenessTest = "ImFromTheProblemUniquenessTest";
    private String[] ProblemIDsToRetrieveInGetAllTest = {
            "ImFromProblemGetAllTest1",
            "ImFromProblemGetAllTest2",
            "ImFromProblemGetAllTest3"
    };

    private String[] ProblemIDsToRetrieveInGetAllBUGTest = {
            "ImFromProblemGetAllBUGTest1",
            "ImFromProblemGetAllBUGTest2",
            "ImFromProblemGetAllBUGTest3",
            "ImFromProblemGetAllBUGTest4",
            "ImFromProblemGetAllBUGTest5",
            "ImFromProblemGetAllBUGTest6",
            "ImFromProblemGetAllBUGTest7",
            "ImFromProblemGetAllBUGTest8",
            "ImFromProblemGetAllBUGTest9",
            "ImFromProblemGetAllBUGTest10",
            "ImFromProblemGetAllBUGTest11",
            "ImFromProblemGetAllBUGTest12",
            "ImFromProblemGetAllBUGTest13",
            "ImFromProblemGetAllBUGTest14",
    };

    private String
            ProblemIDForModifyTest = "ImFromModifyTest",
            ProblemOriginalEmail = "Original@gmail.com",
            ProblemOriginalPhone = "780-555-1234",
            ProblemModifiedEmail = "Modified@gmail.com",
            ProblemModifiedPhone = "587-555-9876";
/*

    //set index to testing index and remove all entries from Problem database
    @After
    @Before
    public void WipeProblemsDatabase() throws IOException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProblemControllerForTesting().DeleteProblems();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }
*/
    @Test
    //pass
    public void AddProblemTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        //create new problem
        Problem newProblem = new Problem(ProblemCreatedByUserIDInAddTest, "");

        //add new problem to the problem database
        new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the problem database
        Problem problem = new ElasticsearchProblemController.GetProblemsByProblemIDsTask()
                .execute(newProblem.getElasticSearchID()).get().get(0);

        //check that the new problem is now in the database
        assertEquals("problems were not equal", problem.getCreatedByUserID(),
                newProblem.getCreatedByUserID());
    }
/*
    @Test
    //fail
    public void ProblemsHaveUniqueIDs() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        Problem newProblem = new Problem(ProblemIDForUniquenessTest);

        //add same problem twice
        new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch problems
        ArrayList<Problem> problems =
                new ElasticsearchProblemController.GetProblemTask().execute().get();

        assertEquals("Should only be one entry in the results",
                1, problems.size());
    }

    @Test
    //pass
    public void getProblemTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new problem
        Problem newProblem = new Problem(ProblemIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new problem to the problem database
        new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the problem database
        ArrayList<Problem> problems = new ElasticsearchProblemController.GetProblemTask()
                .execute(newProblem.getUserID()).get();

        assertTrue("problems array not at least 1 member long", problems.size() >= 1);

        Problem fetchedProblem = problems.get(0);

        assertEquals("fetched problem userID not equal",
                newProblem.getUserID(), fetchedProblem.getUserID());

        assertEquals("fetched problem email not equal",
                newProblem.getEmail(), fetchedProblem.getEmail());

        assertEquals("fetched problem phone not equal",
                newProblem.getPhoneNumber(), fetchedProblem.getPhoneNumber());
    }

    @Test
    //pass
    public void getProblemsTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        AssertProblemsCanBeAddedAndThenBatchFetched(ProblemIDsToRetrieveInGetAllTest);
    }

    @Test
    //fail
    public void getProblemsBUGTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        //Can't fetch more than 10 results right now, this checks for the existence of that bug
        AssertProblemsCanBeAddedAndThenBatchFetched(ProblemIDsToRetrieveInGetAllBUGTest);
    }

    private void AssertProblemsCanBeAddedAndThenBatchFetched(@NonNull String[] suppliedUserIDs)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException {
        ArrayList<Problem> expectedProblems = new ArrayList<>();
        ArrayList<Boolean> expectedProblemInResults = new ArrayList<>();

        //add all expected users in
        for (String problemID : suppliedUserIDs) {
            Problem newProblem = new Problem(problemID);

            //add new problem to expected returns
            expectedProblems.add(newProblem);
            expectedProblemInResults.add(false);

            //add new problem to the problem database
            new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();
        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //make sure each of the added users is individually fetchable
        for (int i = 0; i < suppliedUserIDs.length; i++) {
            //fetch new Problem from the Problem database
            ArrayList<Problem> Problems = new ElasticsearchProblemController
                    .GetProblemTask().execute(suppliedUserIDs[i]).get();

            //grab the first Problem from the result (test will fail if there's no
            //results in output, which is good)
            Problem Problem = Problems.get(0);

            assertEquals("Fetched Problem was different from one added",
                    Problem.getUserID(), expectedProblems.get(i).getUserID());

        }

        //Get objects from database for all the entered problem IDs
        ArrayList<Problem> results = new ElasticsearchProblemController.GetProblemTask()
                .execute(suppliedUserIDs).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedUserIDs.length > 10 && results.size() == 10) {
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as problems we queried. We got exactly" +
                            "ten results instead of expected " + suppliedUserIDs.length,
                    results.size() == suppliedUserIDs.length);
        }

        assertTrue("there should be as many results as problems we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedUserIDs.length,
                results.size() == suppliedUserIDs.length);

        //get all users
        results = new ElasticsearchProblemController.GetProblemTask().execute().get();

        //compare results to what we expected to find.
        //The problems we added should now be there
        for (Problem problem : results) {
            for (int i = 0; i < expectedProblems.size(); i++) {

                //track which expected problems are seen in the results
                if (problem.getUserID().equals(expectedProblems.get(i).getUserID())) {
                    expectedProblemInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected problems in the results
        for (boolean problemSeenInResults : expectedProblemInResults) {
            assertTrue("Problem missing from results", problemSeenInResults);
        }
    }

    @Test
    public void modifyProblemSavesChanges() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException {
        Problem problem = new Problem(ProblemIDForModifyTest,
                ProblemOriginalEmail,
                ProblemOriginalPhone);

        new ElasticsearchProblemController.AddProblemTask().execute(problem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //modify user
        problem.setEmail(ProblemModifiedEmail);
        problem.setPhoneNumber(ProblemModifiedPhone);

        //save modification
        new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned problem, hopefully modified
        Problem returnedProblem = new ElasticsearchProblemController.
                GetProblemTask().execute(problem.getUserID()).get().get(0);

        //check the object was changed
        assertEquals(returnedProblem.getEmail(), ProblemModifiedEmail);
        assertEquals(returnedProblem.getPhoneNumber(), ProblemModifiedPhone);
    }
*/
}