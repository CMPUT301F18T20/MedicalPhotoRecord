package com.cmput301f18t20.medicalphotorecord;

import android.app.Activity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import Activities.Login;
import Activities.SignUp;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;


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
        //starts with login activity
        Assert.assertEquals(getActivityInstance().getClass(), Login.class);

        //click on sign up
        Espresso.onView(withId(R.id.SignUpButton)).perform(click());

        //launches sign up activity
        Assert.assertEquals(getActivityInstance().getClass(), SignUp.class);
    }

    private void commonCode() {
        //in Login activity
        assertEquals(getActivityInstance().getClass(), Login.class);

        //click on sign up
        onView(withId(R.id.SignUpButton)).perform(click());

        //starts Signup activity
        assertEquals(getActivityInstance().getClass(), SignUp.class);

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
        assertEquals(getActivityInstance().getClass(), Login.class);

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
        assertEquals(getActivityInstance().getClass(), Login.class);

        //make sure the user ID that was just entered for signing up is now filled in on Login
        onView(withId(R.id.UserIDText)).check(matches(withText("")));
    }

    //TODO test to verify exceptions raised on improper login attempts
    //TODO test to verify Patient login goes to patient login activity
    //TODO test to verify Provider login goes to provider login activity

    /* https://stackoverflow.com/a/42001476/7520564 */
    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }
}
