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

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static Activities.ActivityBank.ClickSignUpAndEnterUserID;
import static Activities.ActivityBank.CommonSetUp;
import static Activities.ActivityBank.SignUpAsUser;
import static Activities.ActivityBank.SignUpAsUserAndLogin;
import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
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
    public void changeToTestIndexAndDeleteAllEntries() throws InterruptedException, ExecutionException {
        //set to the test index
        //delete all entries from the index
        CommonSetUp();
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
        SignUpAsUserAndLogin(PatientUserID, R.id.PatientCheckBox);

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

        //sign up as a provider
        SignUpAsUserAndLogin(ProviderUserID, R.id.ProviderCheckBox);

        //login button is now gone
        onView(withId(R.id.LoginButton)).check(doesNotExist());

        //provider's home view has list of patients button.  Should be visible.
        onView(withId(R.id.ListOfPatientsButton)).check(matches(isDisplayed()));

        //patient's home view has list of problems button.  Should not be visible.
        onView(withId(R.id.ListOfProblemsButton)).check(doesNotExist());
    }



    //TODO test to verify exceptions raised on improper login attempts
}
