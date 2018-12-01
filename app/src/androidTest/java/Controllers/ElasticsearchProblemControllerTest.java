/*
 * Class name: ElasticsearchProblemControllerTest
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
import java.util.Date;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.ProblemDescriptionTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import io.searchbox.core.DeleteByQuery;

import static Controllers.ElasticsearchProblemController.ddd;
import static Controllers.Utils.nameGen;
import static GlobalSettings.GlobalSettings.getIndex;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static GlobalSettings.GlobalTestSettings.timeout;
import static java.lang.Math.abs;
import static org.junit.Assert.*;


public class ElasticsearchProblemControllerTest {

    private String
            ProblemCreatedByUserIDInAddTest = "ImFromTheProblemAddTest",
            ProblemUserIDToGetInGetTest = "ImFromTheProblemGetTest",
            ProblemUserIDToRetrieveInGetAllTest = "ImFromProblemGetAllTest",
            ProblemUserIDToRetrieveInGetAllBUGTest = "ImFromProblemGetAllBUGTest",
            ProblemIDForModifyTest = "ImUserIDFromModifyTest",
            ProblemOriginalTitle = "Original@gmail.com",
            ProblemOriginalDescription = "780-555-1234",
            ProblemModifiedTitle = "Modified@gmail.com",
            ProblemModifiedDescription = "587-555-9876";

    private String[] ProblemTitlesToRetrieveInGetAllTest =
            nameGen("ImTitleProblemGetAllTest", 3);

    private String[] ProblemTitlesToRetrieveInGetAllBUGTest =
            nameGen("ImTitleProblemGetAllBUGTest", 50);

    //set index to testing index and remove all entries from Problem database
    @After
    @Before
    public void WipeProblemsDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

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
        Problem problem = new ElasticsearchProblemController.
                GetProblemByProblemUUIDTask().execute(newProblem.getUUID()).get();

        //check that the new problem is now in the database
        assertEquals("problems were not equal", problem.getCreatedByUserID(),
                newProblem.getCreatedByUserID());
    }


    @Test
    //pass
    public void GetProblemByProblemUUIDTaskTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        //create new problem
        Problem newProblem = new Problem(ProblemUserIDToGetInGetTest,"");

        //add new problem to the problem database
        new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the problem database
        Problem fetchedProblem = new ElasticsearchProblemController.
                GetProblemByProblemUUIDTask().execute(newProblem.getUUID()).get();


        assertEquals("fetched problem userID not equal",
                newProblem.getCreatedByUserID(), fetchedProblem.getCreatedByUserID());
    }

    @Test
    //pass
    public void getProblemsByCreatedByUserIDTest() throws ExecutionException, TitleTooLongException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        AssertProblemsCanBeAddedAndThenBatchFetched(ProblemUserIDToRetrieveInGetAllTest,
                ProblemTitlesToRetrieveInGetAllTest);
    }

    @Test
    //pass
    public void getProblemsByCreatedByUserIDBUGTest() throws ExecutionException, TitleTooLongException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        //check we can fetch more than 10 results at once
        AssertProblemsCanBeAddedAndThenBatchFetched(ProblemUserIDToRetrieveInGetAllBUGTest,
                ProblemTitlesToRetrieveInGetAllBUGTest);
    }

    private void AssertProblemsCanBeAddedAndThenBatchFetched(
            String suppliedUserID, String[] suppliedTitles)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, TitleTooLongException {
        ArrayList<Problem> expectedProblems = new ArrayList<>();
        ArrayList<Boolean> expectedProblemInResults = new ArrayList<>();

        //add all expected problems in
        for (String title : suppliedTitles) {

            Problem newProblem = new Problem(suppliedUserID, title);

            //add new problem to the problem database
            new ElasticsearchProblemController.AddProblemTask().execute(newProblem).get();

            //add new problem to expected returns
            expectedProblems.add(newProblem);
            expectedProblemInResults.add(false);
        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //make sure each of the added users is individually fetchable
        for (int i = 0; i < suppliedTitles.length; i++) {
            //fetch new Problem from the Problem database
            Problem problem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask()
                    .execute(expectedProblems.get(i).getUUID()).get();

            assertEquals("Fetched Problem had different UserID from one added",
                    problem.getCreatedByUserID(), suppliedUserID);

            assertEquals("Fetched Problem Title was different from one added",
                    problem.getTitle(), suppliedTitles[i]);
        }

        //Get objects from database created by specific user id
        ArrayList<Problem> results = new ElasticsearchProblemController.GetProblemsCreatedByUserIDTask()
                .execute(suppliedUserID).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedTitles.length > 10 && results.size() == 10) {
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as problems we queried. We got exactly " +
                            "ten results instead of expected " + suppliedTitles.length,
                    results.size() == suppliedTitles.length);
        }

        assertTrue("there should be as many results as problems we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedTitles.length,
                results.size() == suppliedTitles.length);

        //compare results to what we expected to find.
        //The problems we added should now be there
        for (Problem problem : results) {
            for (int i = 0; i < suppliedTitles.length; i++) {

                //track which expected problems are seen in the results
                if (problem.getTitle().equals(suppliedTitles[i])) {
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
    //pass
    public void modifyProblemSavesChanges() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException, TitleTooLongException,
            ProblemDescriptionTooLongException {

        //setup original problem
        Problem problem = new Problem(ProblemIDForModifyTest,ProblemOriginalTitle);
        problem.setDescription(ProblemOriginalDescription);

        //add problem
        new ElasticsearchProblemController.AddProblemTask().execute(problem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //modify problem
        problem.setTitle(ProblemModifiedTitle);
        problem.setDescription(ProblemModifiedDescription);

        //check the object was changed and equals our modified values
        assertEquals("Original problem title not modified correctly.",
                ProblemModifiedTitle, problem.getTitle());
        assertEquals("Original problem description not modified correctly.",
                ProblemModifiedDescription, problem.getDescription());

        //save modification
        new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned problem, hopefully modified
        Problem returnedProblem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask()
                .execute(problem.getUUID()).get();

        //check the object was changed and equals our modified values
        assertEquals("problem title on returned object not modified correctly.",
                ProblemModifiedTitle, returnedProblem.getTitle());
        assertEquals("problem description on returned object not modified correctly.",
                ProblemModifiedDescription, returnedProblem.getDescription());
    }

    //tests for existence of BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/199
    @Test
    //pass
    public void modifyProblemSavesDateChangesBUG() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException, TitleTooLongException,
            ProblemDescriptionTooLongException {

        //setup original problem
        Problem problem = new Problem(ProblemIDForModifyTest,ProblemOriginalTitle);
        problem.setDescription(ProblemOriginalDescription);

        //add problem
        new ElasticsearchProblemController.AddProblemTask().execute(problem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        Date ProblemModifiedDate = new Date(System.currentTimeMillis());

        //modify problem date
        problem.setDate(ProblemModifiedDate);

        //check the object was changed and equals our modified values
        assertEquals("Original problem date not modified correctly.",
                ProblemModifiedDate.getTime(), problem.getDate().getTime());

        //save modification
        new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned problem, hopefully modified
        Problem returnedProblem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask()
                .execute(problem.getUUID()).get();

        //date is not exact, it seems to be rounded to nearest 1000 nsec
        //BUG https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/199
        assertTrue("REOPEN BUG: https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/199 " +
                        "problem date on returned object not modified correctly.",
                abs(ProblemModifiedDate.getTime() - returnedProblem.getDate().getTime()) <= 1000);
    }

    @Test
    public void dddTest() throws TitleTooLongException, UserIDMustBeAtLeastEightCharactersException,
            ExecutionException, InterruptedException {

        //this tests for title, also have test for description, both title and
        //description and no matches with matching problems for a different createdByUserID
        String TitleForTest = "greatStuff";

        Problem problem = new Problem(ProblemUserIDToGetInGetTest, TitleForTest);

        new ElasticsearchProblemController.AddProblemTask().execute(problem).get();

        Thread.sleep(timeout);

        ArrayList<Problem> problemsReturned = ddd(ProblemUserIDToGetInGetTest, TitleForTest);

        assertNotEquals("No results from query", problemsReturned.size(), 0);

    }
}