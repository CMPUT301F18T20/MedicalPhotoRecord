package com.cmput301f18t20.medicalphotorecord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Activities.SignUp;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public final class SignUpInstrumentedTest {

    @Rule
    public final ActivityTestRule<SignUp> SignUpActivityRule = new ActivityTestRule<>(SignUp.class);

    @Test
    public void onProviderClick() {
        //click on ProviderCheckBox
        onView(withId(R.id.ProviderCheckBox)).perform(click());

        //make sure it's now checked
        onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
    }

    @Test
    public void OneOfProviderOrPatientCanBeSelected() {
        //click on ProviderCheckBox
        onView(withId(R.id.ProviderCheckBox)).perform(click());

        //make sure it's now checked
        onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
        onView(withId(R.id.PatientCheckBox)).check(matches(isNotChecked()));

        //click on PatientCheckBox
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //results toggle
        onView(withId(R.id.PatientCheckBox)).check(matches(isChecked()));
        onView(withId(R.id.ProviderCheckBox)).check(matches(isNotChecked()));

        //click on ProviderCheckBox
        onView(withId(R.id.ProviderCheckBox)).perform(click());

        //results toggle again
        onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
        onView(withId(R.id.PatientCheckBox)).check(matches(isNotChecked()));

    }

    @Test
    public void onPatientClick() {
        //click on PatientCheckBox
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //make sure it's now checked
        onView(withId(R.id.PatientCheckBox)).check(matches(isChecked()));
    }

    @Test
    public void onSaveClickGeneratesCorrectToastMessagesForExceptions() {
        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());

        // https://stackoverflow.com/a/28606603/7520564
        // Check that the error message about not having selected one of
        // Patient or Provider is shown in a Toast message
        onView(withText("You must select either patient or provider"))
                .inRoot(withDecorView(not(is(
                        SignUpActivityRule.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));

        //click on PatientCheckBox
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());

        // https://stackoverflow.com/a/28606603/7520564
        // Check that the error message about having too short of a userID is displayed in Toast
        onView(withText("User ID must be at least 8 characters long"))
                .inRoot(withDecorView(not(is(
                        SignUpActivityRule.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));
    }

    //TODO make test that user is actually added
}