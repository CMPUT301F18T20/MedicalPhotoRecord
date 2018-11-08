package com.cmput301f18t20.medicalphotorecord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Activities.Login;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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

    @Rule
    public final ActivityTestRule<Login> mainActivity = new ActivityTestRule<>(Login.class);

    @Test
    public void ClickSignUpStartsActivity() {

        //click on sign up
        Espresso.onView(withId(R.id.SignUpButton)).perform(click());

        //now the login activity (sign up button included) should be gone
        onView(withId(R.id.SignUpButton)).check(doesNotExist());
    }

    private void commonCode() {

        //click on sign up
        onView(withId(R.id.SignUpButton)).perform(click());

        //starts Signup activity
        //type in the userID and close keyboard
        onView(withId(R.id.UserIDBox)).perform(typeText(EnteredUserID), closeSoftKeyboard());
    }

    @Test
    public void SignUpFillsInUserIDInPreviousScreen() {
        //go to sign up and enter a valid user id
        commonCode();

        //click on PatientCheckBox to sign up as patient
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());

        //returns to Login activity
        //make sure the user ID that was just entered for signing up is now filled in on Login
        onView(withId(R.id.UserIDText)).check(matches(withText(EnteredUserID)));
    }

    @Test
    public void DoesntFillInUserIDOnSignUpBackPress() {
        //go to sign up and enter a valid user id
        commonCode();

        //decide not to sign up
        pressBack();

        //returns to Login activity
        //make sure the user ID that was just entered for signing up is now filled in on Login
        onView(withId(R.id.UserIDText)).check(matches(withText("")));
    }

    //TODO test to verify exceptions raised on improper login attempts
    //TODO test to verify Patient login goes to patient login activity
    //TODO test to verify Provider login goes to provider login activity
}
