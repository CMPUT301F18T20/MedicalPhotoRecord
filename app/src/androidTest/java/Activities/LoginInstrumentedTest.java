/*
 * Class name: LoginInstrumentedTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 11:43 AM
 *
 * Last Modified: 15/11/18 11:33 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import Activities.Login;
import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProviderController;
import Enums.INDEX_TYPE;
import GlobalSettings.GlobalSettings;
import GlobalSettings.GlobalTestSettings;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static Activities.LoginInstrumentedTest.ClickSignUpAndEnterUserID;
import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public final class LoginInstrumentedTest {

    private final String EnteredUserID = "newUserIDForTest";
    private final String PatientUserID = "newPatientForTest";
    private final String ProviderUserID = "newProviderForTest";

    @Rule
    public final ActivityTestRule<Login> mainActivity = new ActivityTestRule<>(Login.class);

    @Before
    public void changeToTestIndex() throws InterruptedException, ExecutionException {
        //set to the test index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        //delete all entries from the index
        new ElasticsearchPatientController.DeletePatientsTask().execute().get();
        new ElasticsearchProviderController.DeleteProvidersTask().execute().get();
    }

    @Test
    //passes
    public void ClickSignUpStartsActivity() {

        //click on sign up
        Espresso.onView(ViewMatchers.withId(R.id.SignUpButton)).perform(click());

        //now the login activity (login button included) should be gone
        onView(withId(R.id.LoginButton)).check(doesNotExist());

        //sign up button in sign up activity is present
        onView(withId(R.id.SignUpSaveButtton)).check(matches(isDisplayed()));
    }

    public static void ClickSignUpAndEnterUserID(String UserID) {
        //click on sign up
        onView(withId(R.id.SignUpButton)).perform(click());

        //starts Signup activity
        //type in the userID and close keyboard
        onView(withId(R.id.UserIDBox)).perform(typeText(UserID), closeSoftKeyboard());
    }

    public static void SignUpAsUser(String UserID, int Checkbox) {
        //go to sign up and enter a valid user id
        ClickSignUpAndEnterUserID(UserID);

        //click on PatientCheckBox to sign up as patient
        onView(withId(Checkbox)).perform(click());

        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());
    }

    @Test
    //passes
    public void SignUpFillsInUserIDInPreviousScreen() {

        SignUpAsUser(EnteredUserID, R.id.PatientCheckBox);

        //returns to Login activity
        //make sure the user ID that was just entered for signing up is now filled in on Login
        onView(withId(R.id.UserIDText)).check(matches(withText(EnteredUserID)));
    }

    @Test
    //passes
    public void DoesntFillInUserIDOnSignUpBackPress() {
        //go to sign up and enter a valid user id
        ClickSignUpAndEnterUserID(EnteredUserID);

        //decide not to sign up
        pressBack();

        //returns to Login activity
        //make sure the user ID that was just entered for signing up is now filled in on Login
        onView(withId(R.id.UserIDText)).check(matches(withText("")));
    }

    @Test
    //passes
    public void CanLoginAsPatient() throws InterruptedException {

        //sign up as a patient
        SignUpAsUser(PatientUserID, R.id.PatientCheckBox);

        //user ID will be filled in from signup
        //wait for activity to change
        Thread.sleep(timeout);

        //click on login
        onView(withId(R.id.LoginButton)).perform(click());

        //wait for activity to change
        Thread.sleep(timeout);

        //login button is now gone
        onView(withId(R.id.LoginButton)).check(doesNotExist());

        //provider's home view has list of patients button.  Should not be visible.
        onView(withId(R.id.ListOfPatientsButton)).check(doesNotExist());

        //patient's home view has list of problems button.  Should be visible.
        onView(withId(R.id.ListOfProblemsButton)).check(matches(isDisplayed()));
    }

    @Test
    //passes
    public void CanLoginAsProvider() throws InterruptedException {

        //sign up as a patient
        SignUpAsUser(ProviderUserID, R.id.ProviderCheckBox);

        //user ID will be filled in from signup
        //wait for activity to change
        Thread.sleep(timeout);

        //click on login
        onView(withId(R.id.LoginButton)).perform(click());

        //wait for activity to change
        Thread.sleep(timeout);

        //login button is now gone
        onView(withId(R.id.LoginButton)).check(doesNotExist());

        //provider's home view has list of patients button.  Should be visible.
        onView(withId(R.id.ListOfPatientsButton)).check(matches(isDisplayed()));

        //patient's home view has list of problems button.  Should not be visible.
        onView(withId(R.id.ListOfProblemsButton)).check(doesNotExist());
    }



    //TODO test to verify exceptions raised on improper login attempts
}
