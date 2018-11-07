package com.cmput301f18t20.medicalphotorecord;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import Activities.SignUp;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import androidx.test.espresso.*;

import android.support.test.espresso.Espresso;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpTest {

    @Rule
    public ActivityTestRule<SignUp> ActivityRule =
            new ActivityTestRule<>(SignUp.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void onProviderClick() {
        //click on ProviderCheckBox
        Espresso.onView(withId(R.id.ProviderCheckBox)).perform(click());

        //make sure it's now checked
        Espresso.onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
    }

    @Test
    public void OneOfProviderOrPatientCanBeSelected() {

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
        //click on PatientCheckBox
        Espresso.onView(withId(R.id.PatientCheckBox)).perform(click());

        //make sure it's now checked
        Espresso.onView(withId(R.id.PatientCheckBox)).check(matches(isChecked()));
    }

    @Test
    public void onSaveClick() {
    }
}