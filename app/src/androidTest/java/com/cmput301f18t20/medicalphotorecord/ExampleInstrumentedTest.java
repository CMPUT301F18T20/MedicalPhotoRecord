package com.cmput301f18t20.medicalphotorecord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Activities.Login;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public final class ExampleInstrumentedTest {

    @Rule
    public final ActivityTestRule<Login> mainActivity = new ActivityTestRule<>(Login.class);

    @Test
    public void canClickSignUp() {
        mainActivity.getActivity();
        Espresso.onView(withId(R.id.SignUpButton)).perform(click());
    }
}
