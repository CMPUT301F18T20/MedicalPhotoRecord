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

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchShortCodeController;
import Controllers.ShortCodeController;
import Enums.USER_TYPE;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static Activities.ActivityBank.ClickSignUpAndEnterUserID;
import static Activities.ActivityBank.CommonSetUp;
import static Activities.ActivityBank.SignUpAsUser;
import static Activities.ActivityBank.SignUpAsUserAndLogin;
import static Activities.ActivityBank.assertInPatientHomeMenu;
import static Activities.ActivityBank.assertInProviderHomeMenu;
import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public final class LoginInstrumentedTest {

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
    public void CanFetchSecurityTokenAndLoginAsPatientUsingShortCode() throws InterruptedException,
            ExecutionException {

        //login by fetching a mock security token from a mock short code
        LoginAsUserFromShortCode(PATIENT, PatientUserID);

        //make sure the procedure logged us into patient
        assertInPatientHomeMenu();

    }

    @Test
    //passes
    public void CanFetchSecurityTokenAndLoginAsProviderUsingShortCode() throws ExecutionException,
            InterruptedException {
        //login by fetching a mock security token from a mock short code
        LoginAsUserFromShortCode(PROVIDER, ProviderUserID);

        //make sure the procedure logged us into provider
        assertInProviderHomeMenu();
    }

    @Test
    //passes
    public void LoginButonPressGenerateCorrectExceptions() {
        fail("Not implemented");
    }

    private void LoginAsUserFromShortCode(USER_TYPE user_type, String UserID)
            throws ExecutionException, InterruptedException {
        
        //create security token and put it into the shortCode to be tracked
        SecurityToken securityToken = new SecurityToken(UserID, user_type);
        ShortCode shortCode = new ShortCode(securityToken);

        //put the code into elasticsearch
        new ElasticsearchShortCodeController.AddShortCodeTask().execute(shortCode).get();

        //wait for changes
        Thread.sleep(timeout);

        //enter the short code value into the code text bar
        onView(withId(R.id.CodeText)).perform(
                typeText(shortCode.getShortSecurityCode()),
                closeSoftKeyboard());

        //click login to fetch security code and login as that user
        onView(withId(R.id.LoginButton)).perform(click());
    }

}
