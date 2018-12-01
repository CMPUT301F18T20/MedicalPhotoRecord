/*
 * Class name: PatientHomeMenuActivityTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 11:43 AM
 *
 * Last Modified: 15/11/18 11:26 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProviderController;
import Controllers.ElasticsearchSecurityTokenController;
import Controllers.ElasticsearchShortCodeController;
import Controllers.IOLocalSecurityTokenController;
import Controllers.SignUpController;
import Enums.INDEX_TYPE;
import Enums.USER_TYPE;
import Exceptions.NoConnectionInSignUpException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.ClickOnViewProfileAndAssertCorrectActivityAndUser;
import static Activities.ActivityBank.LOGINActivity;
import static Activities.ActivityBank.SignUpAsUser;
import static Activities.ActivityBank.WipeAllUsers;
import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PATIENT;
import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class PatientHomeMenuActivityTest {

    //TODO initialize this user in the database once (make a @Before method that adds it if a variable
    //TODO is not null, and set the variable to non null once you have added the user
    private String InitialUserIDInIntent = "PatientHomeMenuActivityTestUser";
    private SecurityToken securityToken;

    private static Boolean UserAdded = FALSE;

    private String
            NewEmail = "NewUserIDForPatient@gmail.com",
            NewPhone = "7805551234";

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<PatientHomeMenuActivity> PatientActivity =
            new ActivityTestRule<>(PatientHomeMenuActivity.class,
                    false, false);

    @Before
    public void setUp() throws ExecutionException, InterruptedException {
        //change to testing index
        changeToTestIndex();

        //sign up user if they aren't already
        if (UserAdded != TRUE) {
            SignUpUser(InitialUserIDInIntent, R.id.PatientCheckBox);
        }

        //put the user id into the intent and then start the activity
        Intent i = new Intent();
        i.putExtra("UserID", InitialUserIDInIntent);
        PatientActivity.launchActivity(i);
    }

    public void SignUpUser(String UserID, int CheckBox) throws InterruptedException, ExecutionException {
        //erase all users from elasticsearch
        WipeAllUsers();

        //Start login activity
        LOGINActivity.launchActivity(new Intent());
        
        //sign up as specified user
        SignUpAsUser(UserID, CheckBox);

        //If we get here without exceptions, then everything has succeeded
        UserAdded = TRUE;

        //finished with activity
        LOGINActivity.finishActivity();
    }

    @Test
    //Passes
    public void CorrectUserIDDisplayed() {
        //verifies the intent is read correctly and the user id is displayed on screen

        //User ID should be displayed on screen along with Welcome
        onView(withId(R.id.UserIDWelcomeBox)).check(matches(withText("Welcome " + InitialUserIDInIntent)));
    }

    @Test
    //Passes
    public void onEditClickBringsUpEditUserActivityWithCorrectUserID() {
        //verifies that the intent carried the user id to the activity and it was correctly read

        //click on edit contact info button
        onView(withId(R.id.EditContactInfoButton)).perform(click());

        //check userid_edit_id box is visible
        onView(withId(R.id.user_text_id)).check(matches(isDisplayed()));

        //make sure the User ID that we got from the Login intent has been loaded into the
        //intent when transitioning into the edit user id activity.
        onView(withId(R.id.user_text_id)).check(matches(withText(InitialUserIDInIntent)));
    }

    @Test
    //Passes
    public void onListOfProblemsClick() {
        //view a list of problems for this user
        onView(withId(R.id.ListOfProblemsButton)).perform(click());

        //check add problem button from problem list view is visible
        onView(withId(R.id.add_problem_button_id)).check(matches(isDisplayed()));
    }

    @Test
    //Passes
    public void onViewProfileClickLoadsCorrectUserID() {
        //verifies that the intent carried the user id to the activity and it was correctly read

        ClickOnViewProfileAndAssertCorrectActivityAndUser(InitialUserIDInIntent);
    }

    @Test
    //Passes
    public void onGenerateCodeClickBehavesAsExpected()
            throws InterruptedException, ExecutionException {

        Context context = PatientActivity.getActivity().getBaseContext();

        //store old security token
        try {
            this.securityToken = IOLocalSecurityTokenController.loadSecurityTokenFromDisk(context);
        } catch (FileNotFoundException e) {
            //no issue here, we didn't have an old token
        }

        //delete all security tokens in local storage and elasticsearch
        new ElasticsearchShortCodeController.DeleteByShortSecurityCodeTask().execute().get();
        IOLocalSecurityTokenController.deleteSecurityTokenOnDisk(context);

        //delete all codes from elasticsearch
        new ElasticsearchSecurityTokenController.DeleteSecurityTokenByUserIDTask().execute().get();

        //click on GenerateLoginCodeButton button to create a code
        onView(withId(R.id.GenerateLoginCodeButton)).perform(click());

        //should produce an error about not being able to load security token
        onView(withText("Unable to load a security token for this user"))
                .inRoot(withDecorView(not(is(
                        PatientActivity.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));

        //so lets create a security token
        SecurityToken securityToken = new SecurityToken(InitialUserIDInIntent, PATIENT);

        //and add it to the local storage
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(securityToken, context);

        //click on GenerateLoginCodeButton button to create a code
        onView(withId(R.id.GenerateLoginCodeButton)).perform(click());

        //fetch the short code that's generated
        String shortSecurityCode = PatientActivity.getActivity()
                .getShortCode().getShortSecurityCode();

        //check that a toast message with the code is shown
        onView(withText("Added code " + shortSecurityCode))
                .inRoot(withDecorView(not(is(
                        PatientActivity.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    //Passes
    public void EditingAUserMaintainsChangesWhileOnline() {
        //TODO perform check that we are online by using (I think) the Sign up controller?
        //TODO maybe move that function to another controller?

        //click on edit contact info button
        onView(withId(R.id.EditContactInfoButton)).perform(click());

        //replace the values
        onView(withId(R.id.email_edit_id)).perform(replaceText(NewEmail));
        onView(withId(R.id.phone_edit_id)).perform(replaceText(NewPhone));

        //save changes
        onView(withId(R.id.save_button_id)).perform(click());

        //go back TODO do we want to press back? or have it return immediately?
        pressBack();

        //click on view contact info button
        onView(withId(R.id.ViewProfileButton)).perform(click());

        //check the views have the correct data
        onView(withId(R.id.EmailBox)).check(matches(withText(NewEmail)));
        onView(withId(R.id.PhoneBox)).check(matches(withText(NewPhone)));
    }
/*
    @Test
    //fails
    public void EditingAUserMaintainsChangesWhileOffline()
            throws ExecutionException, InterruptedException {
        //TODO perform check that we are online by using (I think) the Sign up controller?
        //TODO maybe move that function to another controller?

        setWifiStatus(FALSE);

        Thread.sleep(3000);

        //click on edit contact info button
        onView(withId(R.id.EditContactInfoButton)).perform(click());

        //replace the values
        onView(withId(R.id.email_edit_id)).perform(replaceText(NewEmail));
        onView(withId(R.id.phone_edit_id)).perform(replaceText(NewPhone));

        //save changes
        onView(withId(R.id.save_button_id)).perform(click());

        //go back TODO do we want to press back? or have it return immediately?
        pressBack();

        //click on view contact info button
        onView(withId(R.id.ViewProfileButton)).perform(click());

        //check the views have the correct data
        onView(withId(R.id.EmailBox)).check(matches(withText(NewEmail)));
        onView(withId(R.id.PhoneBox)).check(matches(withText(NewPhone)));
    }
*/
    private void setWifiStatus(Boolean status) {
        //start activity and get context
        LOGINActivity.launchActivity(new Intent());
        Context context = LOGINActivity.getActivity().getBaseContext();

        //change status of the wifi
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(status);

        //terminate activity
        LOGINActivity.finishActivity();
    }
}