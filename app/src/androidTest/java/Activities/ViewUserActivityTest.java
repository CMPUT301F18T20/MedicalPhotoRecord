/*
 * Class name: ViewUserActivityTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 11:43 AM
 *
 * Last Modified: 15/11/18 11:43 AM
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

import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class ViewUserActivityTest {
    String InitialUserIDInIntent = "TestingUserID",
        InitialEmailInIntent = "Testing@email.com",
        InitialPhoneInIntent = "7805551234";

    //making argument 3 false means the activity won't immediately start, we have to launchActivity
    @Rule
    public final ActivityTestRule<ViewUserActivity> Activity =
            new ActivityTestRule<>(ViewUserActivity.class,
                    false, false);

    //put the values into the intent and then start the activity
    @Before
    public void setUp() {
        Intent i = new Intent();
        i.putExtra(USERIDEXTRA, InitialUserIDInIntent);
        i.putExtra(EMAILEXTRA, InitialEmailInIntent);
        i.putExtra(PHONEEXTRA, InitialPhoneInIntent);
        Activity.launchActivity(i);
    }


    @Test
    //passes
    public void CorrectInformationDisplayed() {
        //User ID matches
        onView(withId(R.id.UserIDBox)).check(matches(withText(InitialUserIDInIntent)));

        //Email matches
        onView(withId(R.id.EmailBox)).check(matches(withText(InitialEmailInIntent)));

        //Phone matches
        onView(withId(R.id.PhoneBox)).check(matches(withText(InitialPhoneInIntent)));


    }

    @Test
    //fails
    public void onBackClick() {
        fail("Needs to be tested from Provider and Patient home activities");
    }
}