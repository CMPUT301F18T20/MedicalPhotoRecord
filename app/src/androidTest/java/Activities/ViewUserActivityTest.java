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

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import Controllers.SignUpController;
import GlobalSettings.GlobalTestSettings;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.ClickOnViewProfileAndAssertCorrectActivityAndUser;
import static Activities.ActivityBank.CommonSetUp;
import static Activities.ActivityBank.LOGINActivity;
import static Activities.ActivityBank.SignUpAsUser;
import static Activities.ActivityBank.SignUpAsUserAndLogin;
import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class ViewUserActivityTest {
    String InitialUserIDInIntent = "TestingUserID",
        InitialEmailInIntent = "Testing@email.com",
        InitialPhoneInIntent = "7805551234";

    //making argument 3 false means the activity won't immediately start, we have to launchActivity
    @Rule
    public final ActivityTestRule<ViewUserActivity> ViewUser =
            new ActivityTestRule<>(ViewUserActivity.class,
                    false, false);

    /**
     * This test launches the view user activity with all the correct values in the intent.
     * It then checks the correct boxes are displaying the correct values
     */
    @Test
    //passes
    public void CorrectInformationDisplayed() {

        //put the values into the intent and then start the activity
        Intent i = new Intent();
        i.putExtra(USERIDEXTRA, InitialUserIDInIntent);
        i.putExtra(EMAILEXTRA, InitialEmailInIntent);
        i.putExtra(PHONEEXTRA, InitialPhoneInIntent);
        ViewUser.launchActivity(i);
        
        //User ID matches
        onView(withId(R.id.UserIDBox)).check(matches(withText(InitialUserIDInIntent)));

        //Email matches
        onView(withId(R.id.EmailBox)).check(matches(withText(InitialEmailInIntent)));

        //Phone matches
        onView(withId(R.id.PhoneBox)).check(matches(withText(InitialPhoneInIntent)));
    }

    /** This test makes sure that hitting the back button on the View User Activity button
     * returns the user to the home menu
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    //passes
    public void onBackClick() throws ExecutionException, InterruptedException {

        //change to test index, wipe all users
        CommonSetUp();

        //start login activity
        LOGINActivity.launchActivity(new Intent());

        //sign up user as a provider and login
        SignUpAsUserAndLogin(InitialUserIDInIntent, R.id.ProviderCheckBox);
        

        //click on view profile and verify we are in the right task
        ClickOnViewProfileAndAssertCorrectActivityAndUser(InitialUserIDInIntent);

        //press back
        pressBack();

        Thread.sleep(timeout);

        //we're back at provider home
        onView(withId(R.id.ViewProfileButton)).check(matches(isDisplayed()));
    }
}