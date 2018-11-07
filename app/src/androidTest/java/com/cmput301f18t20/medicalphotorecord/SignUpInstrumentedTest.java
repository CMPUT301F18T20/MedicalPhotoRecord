package com.cmput301f18t20.medicalphotorecord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Activities.SignUp;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public final class SignUpInstrumentedTest {

    @Rule
    public final ActivityTestRule<SignUp> ActivityRule = new ActivityTestRule<>(SignUp.class);

    @Test
    public void onProviderClick() {
        ActivityRule.getActivity();
        //click on ProviderCheckBox
        Espresso.onView(withId(R.id.ProviderCheckBox)).perform(click());

        //make sure it's now checked
        Espresso.onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
    }

    @Test
    public void OneOfProviderOrPatientCanBeSelected() {
        ActivityRule.getActivity();

        //click on ProviderCheckBox
        Espresso.onView(withId(R.id.ProviderCheckBox)).perform(click());

        //make sure it's now checked
        Espresso.onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
        Espresso.onView(withId(R.id.PatientCheckBox)).check(matches(isNotChecked()));

        //click on PatientCheckBox
        Espresso.onView(withId(R.id.PatientCheckBox)).perform(click());

        //results toggle
        Espresso.onView(withId(R.id.PatientCheckBox)).check(matches(isChecked()));
        Espresso.onView(withId(R.id.ProviderCheckBox)).check(matches(isNotChecked()));

        //click on ProviderCheckBox
        Espresso.onView(withId(R.id.ProviderCheckBox)).perform(click());

        //results toggle again
        Espresso.onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
        Espresso.onView(withId(R.id.PatientCheckBox)).check(matches(isNotChecked()));

    }

    @Test
    public void onPatientClick() {
        ActivityRule.getActivity();

        //click on PatientCheckBox
        Espresso.onView(withId(R.id.PatientCheckBox)).perform(click());

        //make sure it's now checked
        Espresso.onView(withId(R.id.PatientCheckBox)).check(matches(isChecked()));
    }

    @Test
    public void onSaveClick() {
    }
}