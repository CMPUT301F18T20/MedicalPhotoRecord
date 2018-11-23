/*
 * Class name: SignUpInstrumentedTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 11:43 AM
 *
 * Last Modified: 15/11/18 11:41 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import Activities.SignUp;
import GlobalSettings.GlobalSettings;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static GlobalSettings.GlobalTestSettings.timeout;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
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

    @After
    @Before
    public void enableWifi() {
        //https://stackoverflow.com/a/12345627/7520564
        //get context, get the wifi manager from the context, enable the wifi service
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
    }

    @Test
    //passes
    public void onProviderClick() {
        //click on ProviderCheckBox
        onView(withId(R.id.ProviderCheckBox)).perform(click());

        //make sure it's now checked
        onView(withId(R.id.ProviderCheckBox)).check(matches(isChecked()));
    }

    @Test
    //passes
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
    //passes
    public void onPatientClick() {
        //click on PatientCheckBox
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //make sure it's now checked
        onView(withId(R.id.PatientCheckBox)).check(matches(isChecked()));
    }

    @Test
    //passes
    public void onSaveClickGeneratesCorrectToastMessagesForExceptions() throws InterruptedException {
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

        Thread.sleep(1500);

        //click on PatientCheckBox
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());

        Thread.sleep(100);

        // https://stackoverflow.com/a/28606603/7520564
        // Check that the error message about having too short of a userID is displayed in Toast
        onView(withText("User ID must be at least 8 characters long"))
                .inRoot(withDecorView(not(is(
                        SignUpActivityRule.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));

    }

    @Test
    //passes
    public void NoConnectionException() throws InterruptedException {
        /* NOTE Data must be disabled for this test */

        //https://stackoverflow.com/a/12345627/7520564
        //get context, get the wifi manager from the context, DISABLE the wifi service
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);

        //wait for wifi to come down
        Thread.sleep(timeout);

        //click on PatientCheckBox
        onView(withId(R.id.PatientCheckBox)).perform(click());

        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());

        // https://stackoverflow.com/a/28606603/7520564
        // Check that the error message about having too short of a userID is displayed in Toast
        onView(withText("Device is offline"))
                .inRoot(withDecorView(not(is(
                        SignUpActivityRule.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));
    }

    //TODO make test that user is actually added
}