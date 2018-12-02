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

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchProblemController;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalTestSettings;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.changeToTestIndex;
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

    static Boolean correctlySetUpProblemsAlready = FALSE;
    String InitialUserIDInIntent = "UserIDForSearchTest";
    String keyword = "kumquat";
    String nonKeyword = "varia";
    String keywordsString = keyword + " more keyword for searching";
    List<String> keywords = Arrays.asList(keyword, "another", "keyword", "for", "search" );

    static Problem problemWithKeyword,
            problemWithoutKeyword;

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

        //put the user id into the intent and then start the activity
        Intent i = new Intent();
        i.putExtra("UserID", InitialUserIDInIntent);
        searchActivity.launchActivity(i);

        Thread.sleep(3000);

    }

    private void setUpProblems() throws TitleTooLongException,
            UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {
        //delete all problems
        new ElasticsearchProblemController.DeleteProblemsTask().execute().get();

        //create two new problems
        problemWithKeyword = new Problem(InitialUserIDInIntent, keyword);
        problemWithoutKeyword = new Problem(InitialUserIDInIntent, nonKeyword);

        //add new problems to elasticsearch
        new ElasticsearchProblemController.AddProblemTask().execute(problemWithKeyword).get();
        new ElasticsearchProblemController.AddProblemTask().execute(problemWithoutKeyword).get();

        //wait a little
        Thread.sleep(timeout);

        //fetch problems for that user ID
        ArrayList<Problem> problems = new ElasticsearchProblemController
                .GetProblemsCreatedByUserIDTask().execute(InitialUserIDInIntent).get();

        //ensure problems definitely exist in elasticsearch
        assert(problems != null);
        assert(problems.size() == 2);

        correctlySetUpProblemsAlready = TRUE;

    }

    @Test
    public void NoKeywordsDefaultFilterReturnsAssociatedProblemsWithKeywords() {

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure both problems pop up in the search
        onView(withText(problemWithoutKeyword.toString())).check(matches(isDisplayed()));
        onView(withText(problemWithKeyword.toString())).check(matches(isDisplayed()));
    }

    @Test
    public void WithKeywordsDefaultFilterReturnsAssociatedProblems() throws InterruptedException {

        //type in keywords
        onView(withId(R.id.SearchKeywords)).perform(typeText(keywordsString), closeSoftKeyboard());

        //click perform search button
        onView(withId(R.id.SearchButton)).perform(click());

        //make sure the problem pops up in the search
        onView(withText(problemWithKeyword.toString())).check(matches(isDisplayed()));

        //without keyword should not be showing in results
        onView(withText(problemWithoutKeyword.toString())).check(doesNotExist());


    }
}