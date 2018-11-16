/*
 * Class name: PatientHomeMenuActivityTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 11:43 AM
 *
 * Last Modified: 15/11/18 11:26 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

public class PatientHomeMenuActivityTest {

    //TODO initialize this user in the database once (make a @Before method that adds it if a variable
    //TODO is not null, and set the variable to non null once you have added the user
    private String InitialUserIDInIntent = "PatientHomeMenuActivityTestUser";

    private String
            NewUserID = "NewUserIDForPatient",
            NewEmail = "NewUserIDForPatient@gmail.com",
            NewPhone = "7805551234";

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<PatientHomeMenuActivity> PatientActivity =
            new ActivityTestRule<>(PatientHomeMenuActivity.class,
                    false, false);

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<Login> LoginActivity =
            new ActivityTestRule<>(Login.class, false, false);

    //put the user id into the intent and then start the activity
    @Before
    public void setUp() {
        Intent i = new Intent();
        i.putExtra("UserID", InitialUserIDInIntent);
        PatientActivity.launchActivity(i);
    }

    @Test
    //Passes
    public void CorrectUserIDDisplayed() {
        //verifies the intent is read correctly and the user id is displayed on screen

        //User ID should be displayed on screen along with Welcome
        onView(withId(R.id.UserIDWelcomeBox)).check(matches(withText("Welcome " + InitialUserIDInIntent)));
    }

    @Test
    //Fails
    public void onEditClickBringsUpEditUserActivityWithCorrectUserID() {
        //verifies that the intent carried the user id to the activity and it was correctly read

        //click on edit contact info button
        onView(withId(R.id.EditContactInfoButton)).perform(click());

        //check userid_edit_id box is visible
        onView(withId(R.id.user_text_id)).check(matches(isDisplayed()));

        //make sure the User ID that we got from the Login intent has been loaded into the
        //intent when transitioning into the edit user id activity.
        onView(withId(R.id.user_text_id)).check(matches(withText(InitialUserIDInIntent)));

        //TODO make sure that phone number and email are correct for that user id as well
    }

    @Test
    //Passes
    public void onListOfProblemsClick() {
        //view a list of problems for this user
        onView(withId(R.id.ListOfProblemsButton)).perform(click());

        //check add problem button from problem list view is visible
        onView(withId(R.id.add_problem_button_id)).check(matches(isDisplayed()));

        //TODO create dummy problems and make sure they are shown

    }

    @Test
    //Passes
    public void onViewProfileClickLoadsCorrectUserID() {
        //verifies that the intent carried the user id to the activity and it was correctly read

        //click on view contact info button
        onView(withId(R.id.ViewProfileButton)).perform(click());

        //check userid_edit_id box is visible
        onView(withId(R.id.UserIDBox)).check(matches(isDisplayed()));

        //make sure the User ID that we got from the Login intent has been loaded into the
        //intent when transitioning into the edit user id activity.
        onView(withId(R.id.UserIDBox)).check(matches(withText(InitialUserIDInIntent)));

        //TODO make sure that phone number and email are correct for that user id as well
    }

    @Test
    //Fails
    public void onDeleteClickRemovesUserProfile() {
        fail("Not completely implemented");
    }

    @Test
    //Passes.... very finicky, not sure why
    public void onLogoutClickLogsUserOut() throws InterruptedException {
        
        //Start login activity
        LoginActivity.launchActivity(new Intent());

        //sign up as patient
        LoginInstrumentedTest.SignUpAsUser(NewUserID, R.id.PatientCheckBox);

        //click on login
        onView(withId(R.id.LoginButton)).perform(click());

        //wait for activity to change
        Thread.sleep(timeout);

        //click on logout button
        onView(withId(R.id.LogOutButton)).perform(click());

        //wait for activity to change
        Thread.sleep(timeout);

        //check the login button is displayed
        onView(withId(R.id.LoginButton)).check(matches(isDisplayed()));
    }

    @Test
    //Fails
    public void EditingAUserMaintainsChangesWhileOnline() {
        //TODO perform check that we are online by using (I think) the Sign up controller?
        //TODO maybe move that function to another controller?

        //click on edit contact info button
        onView(withId(R.id.EditContactInfoButton)).perform(click());

        //replace the values
        onView(withId(R.id.user_text_id)).perform(replaceText(NewUserID));
        onView(withId(R.id.email_edit_id)).perform(replaceText(NewEmail));
        onView(withId(R.id.phone_edit_id)).perform(replaceText(NewPhone));

        //save changes
        onView(withId(R.id.save_button_id)).perform(click());

        //go back TODO do we want to press back? or have it return immediately?
        pressBack();

        //click on view contact info button
        onView(withId(R.id.ViewProfileButton)).perform(click());

        //check the views have the correct data
        //TODO find the user id box in the view user activity to check
        //onView(withId(R.id.userid_)).check(matches(withText(NewUserID)));
        //onView(withId(R.id.email_)).check(matches(withText(NewEmail)));
        //onView(withId(R.id.phone_)).check(matches(withText(NewPhone)));

        fail("Not completely implemented");
    }
}