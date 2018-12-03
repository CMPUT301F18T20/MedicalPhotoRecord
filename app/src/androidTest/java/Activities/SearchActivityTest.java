/*
 * Class name: SearchActivityTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 02/12/18 2:15 PM
 *
 * Last Modified: 02/12/18 2:15 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Controllers.AddCommentRecordController;
import Controllers.ElasticsearchPatientRecordController;
import Controllers.ElasticsearchProblemController;
import Controllers.ElasticsearchRecordController;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalTestSettings;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PATIENT;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static GlobalSettings.GlobalSettings.USERTYPEEXTRA;
import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.*;

public class SearchActivityTest {

    static Boolean correctlySetUpProblemsAlready = FALSE,
            correctlySetUpRecordsAlready = FALSE,
            correctlySetUpPatientRecordsAlready = FALSE;
    String InitialUserIDInIntent = "UserIDForSearchTest";
    String ProviderUserID = "ProviderForSearchTest";
    String PatientUserID = "PatientForSearchTest";
    String keyword = "kumquat";
    String nonKeyword = "varia";
    String keywordsString = keyword + " more keyword for searching";
    List<String> keywords = Arrays.asList(keyword, "another", "keyword", "for", "search" );

    Filter onlyRecordsFilter = new Filter(FALSE, FALSE, FALSE, TRUE, FALSE);
    Filter onlyPatientRecordsFilter = new Filter(FALSE, FALSE, FALSE, FALSE, TRUE);
    Filter allTypesFilter = new Filter(FALSE, FALSE, TRUE, TRUE, TRUE);

    static Problem problemWithKeyword,
            problemWithoutKeyword;

    static Record recordWithKeyword,
            recordWithoutKeyword;

    static PatientRecord patientRecordWithKeyword,
            patientRecordWithoutKeyword;

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<SearchActivity> searchActivity =
            new ActivityTestRule<>(SearchActivity.class, false, false);


    @Before
    public void setUp() throws ExecutionException, InterruptedException, TitleTooLongException,
            UserIDMustBeAtLeastEightCharactersException {
        //change to testing index
        changeToTestIndex();

        if (correctlySetUpProblemsAlready == FALSE) {
            //add in test problems
            setUpProblems();
        }

        if (correctlySetUpRecordsAlready == FALSE) {
            //add in test records
            setUpRecords();
        }

        if (correctlySetUpPatientRecordsAlready == FALSE) {
            //add in test patient records
            setUpPatientRecords();
        }


        //put the patient user id into the intent and then start the activity
        Intent i = new Intent();
        i.putExtra(USERIDEXTRA, PatientUserID);
        i.putExtra(USERTYPEEXTRA, PATIENT);
        searchActivity.launchActivity(i);

        Thread.sleep(3000);

    }

    private void setUpProblems() throws TitleTooLongException,
            UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {
        //delete all problems
        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //create two new problems
        problemWithKeyword = new Problem(PatientUserID, keyword);
        problemWithoutKeyword = new Problem(PatientUserID, nonKeyword);

        //add new problems to elasticsearch
        new ElasticsearchProblemController.AddProblemTask().execute(problemWithKeyword).get();
        new ElasticsearchProblemController.AddProblemTask().execute(problemWithoutKeyword).get();

        //wait a little
        Thread.sleep(timeout);

        //fetch problems for that user ID
        ArrayList<Problem> problems = new ElasticsearchProblemController
                .GetProblemsCreatedByUserIDTask().execute(PatientUserID).get();

        //ensure problems definitely exist in elasticsearch
        assert(problems != null);
        assert(problems.size() == 2);

        correctlySetUpProblemsAlready = TRUE;

    }

    private void setUpRecords() throws TitleTooLongException,
            UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {
        //delete all records
        new ElasticsearchRecordController.DeleteRecordsTask().execute().get();

        //create two new records
        recordWithKeyword = new Record(ProviderUserID, keyword);
        recordWithoutKeyword = new Record(ProviderUserID, nonKeyword);

        //set the associated patient user ID
        recordWithKeyword.setAssociatedPatientUserID(PatientUserID);
        recordWithoutKeyword.setAssociatedPatientUserID(PatientUserID);

        //add new records to elasticsearch
        new ElasticsearchRecordController.AddRecordTask().execute(recordWithKeyword).get();
        new ElasticsearchRecordController.AddRecordTask().execute(recordWithoutKeyword).get();

        //wait a little
        Thread.sleep(timeout);

        //fetch records for the patient ID
        ArrayList<Record> records = new ElasticsearchRecordController
                .GetRecordsByAssociatedPatientUserIDTask().execute(PatientUserID).get();

        //ensure records definitely exist in elasticsearch
        assert(records != null);
        assert(records.size() == 2);

        correctlySetUpRecordsAlready = TRUE;
    }

    private void setUpPatientRecords() throws TitleTooLongException,
            UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {
        //delete all patientRecords
        new ElasticsearchPatientRecordController.DeletePatientRecordsTask().execute().get();

        //create two new patientRecords
        patientRecordWithKeyword = new PatientRecord(PatientUserID, keyword);
        patientRecordWithoutKeyword = new PatientRecord(PatientUserID, nonKeyword);

        //add new patientRecords to elasticsearch
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(patientRecordWithKeyword).get();
        new ElasticsearchPatientRecordController.AddPatientRecordTask().execute(patientRecordWithoutKeyword).get();

        //wait a little
        Thread.sleep(timeout);

        //fetch patientRecords for that user ID
        ArrayList<PatientRecord> patientRecords = new ElasticsearchPatientRecordController
                .GetPatientRecordsCreatedByUserIDTask().execute(PatientUserID).get();

        //ensure patientRecords definitely exist in elasticsearch
        assert(patientRecords != null);
        assert(patientRecords.size() == 2);

        correctlySetUpPatientRecordsAlready = TRUE;
    }

    @Test
    public void NoKeywordsDefaultFilterReturnsAssociatedProblems() {

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure both problems pop up in the search
        onView(withText(problemWithoutKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(problemWithKeyword.toString())).check(matches(isDisplayed()));
    }

    @Test
    public void WithKeywordsDefaultFilterReturnsAssociatedProblems() {

        //type in keywords
        onView(withId(R.id.SearchKeywords)).perform(typeText(keywordsString), closeSoftKeyboard());

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure the problem pops up in the search
        onView(withText(problemWithKeyword.toString())).check(matches(isDisplayed()));

        //without keyword should not be showing in results
        onView(withText(problemWithoutKeyword.toString())).check(doesNotExist());
    }

    @Test
    public void NoKeywordsDefaultFilterReturnsNoRecords() {

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure neither records pop up in the search
        onView(withText(recordWithKeyword.toString())).check(doesNotExist());
        onView(withText(recordWithoutKeyword.toString())).check(doesNotExist());
    }

    @Test
    public void NoKeywordsModifiedFilterReturnsAssociatedRecords() {

        //set new filter to only records
        searchActivity.getActivity().setFilter(onlyRecordsFilter);

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure both records pop up in the search
        onView(withText(recordWithKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(recordWithoutKeyword.toString())).check(matches(isDisplayed()));
    }

    @Test
    public void WithKeywordsModifiedFilterReturnsAssociatedRecords() {

        //set new filter to only records
        searchActivity.getActivity().setFilter(onlyRecordsFilter);

        //type in keywords
        onView(withId(R.id.SearchKeywords)).perform(typeText(keywordsString), closeSoftKeyboard());

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure the record pops up in the search
        onView(withText(recordWithKeyword.toString())).check(matches(isDisplayed()));

        //without keyword should not be showing in results
        onView(withText(recordWithoutKeyword.toString())).check(doesNotExist());
    }


    @Test
    public void NoKeywordsDefaultFilterReturnsNoPatientRecords() {

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure neither patientRecords pop up in the search
        onView(withText(patientRecordWithKeyword.toString())).check(doesNotExist());
        onView(withText(patientRecordWithoutKeyword.toString())).check(doesNotExist());
    }

    @Test
    public void NoKeywordsModifiedFilterReturnsAssociatedPatientRecords() {

        //set new filter to only patientRecords
        searchActivity.getActivity().setFilter(onlyPatientRecordsFilter);

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure both patientRecords pop up in the search
        onView(withText(patientRecordWithKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(patientRecordWithoutKeyword.toString())).check(matches(isDisplayed()));
    }

    @Test
    public void WithKeywordsModifiedFilterReturnsAssociatedPatientRecords() {

        //set new filter to only patientRecords
        searchActivity.getActivity().setFilter(onlyPatientRecordsFilter);

        //type in keywords
        onView(withId(R.id.SearchKeywords)).perform(typeText(keywordsString), closeSoftKeyboard());

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure the patient record pops up in the search
        onView(withText(patientRecordWithKeyword.toString())).check(matches(isDisplayed()));

        //without keyword should not be showing in results
        onView(withText(patientRecordWithoutKeyword.toString())).check(doesNotExist());
    }

    @Test
    public void NoKeywordsModifiedFilterReturnsAssociatedPatientRecordsProblemsAndRecords() {

        //set new filter to show all types of result
        searchActivity.getActivity().setFilter(allTypesFilter);

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure both problems pop up in the search
        onView(withText(problemWithoutKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(problemWithKeyword.toString())).check(matches(isDisplayed()));

        //make sure both records pop up in the search
        onView(withText(recordWithKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(recordWithoutKeyword.toString())).check(matches(isDisplayed()));

        //make sure both patientRecords pop up in the search
        onView(withText(patientRecordWithKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(patientRecordWithoutKeyword.toString())).check(matches(isDisplayed()));

    }

    @Test
    public void WithKeywordsModifiedFilterReturnsAssociatedPatientRecordsProblemsAndRecords() {

        //set new filter to show all types of result
        searchActivity.getActivity().setFilter(allTypesFilter);

        //type in keywords
        onView(withId(R.id.SearchKeywords)).perform(typeText(keywordsString), closeSoftKeyboard());

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure the matching objects pop up in the search
        onView(withText(problemWithKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(recordWithKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(patientRecordWithKeyword.toString())).check(matches(isDisplayed()));

        //without keyword should not be showing in results
        onView(withText(problemWithoutKeyword.toString())).check(doesNotExist());
        onView(withText(recordWithoutKeyword.toString())).check(doesNotExist());
        onView(withText(patientRecordWithoutKeyword.toString())).check(doesNotExist());
    }

}