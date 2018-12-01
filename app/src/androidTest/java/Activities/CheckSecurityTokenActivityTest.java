package Activities;

import android.content.Context;
import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import Controllers.IOLocalSecurityTokenController;
import Enums.USER_TYPE;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.LOGINActivity;
import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class CheckSecurityTokenActivityTest {

    String UserID = "CheckSecurityTokenUserID";


    //doesn't start automatically, must be started in each test
    @Rule
    public ActivityTestRule<CheckSecurityToken> checkSecurityTokenActivityTestRule
            = new ActivityTestRule<>(CheckSecurityToken.class,
            false, false);

    @Before
    public void setUp() {
        LOGINActivity.launchActivity(new Intent());
        Context context = LOGINActivity.getActivity().getBaseContext();

        //delete the security token from disk
        IOLocalSecurityTokenController.deleteSecurityTokenOnDisk(context);

        LOGINActivity.finishActivity();
    }

    @Test
    public void StartsLoginActivityIfNoSecurityToken() {

        //start activity
        checkSecurityTokenActivityTestRule.launchActivity(new Intent());

        //make sure login page is displayed by checking for sign up button
        Espresso.onView(ViewMatchers.withId(R.id.SignUpButton)).check(matches(isDisplayed()));

    }

    @Test
    public void StartsPatientHomeMenuActivityIfPatientInUserType() {
        assertStartsCorrectHomeMenuActivity(PATIENT, R.id.ListOfProblemsButton);
    }

    @Test
    public void StartsProviderHomeMenuActivityIfPatientInUserType() {
        assertStartsCorrectHomeMenuActivity(PROVIDER, R.id.ListOfPatientsButton);
    }

    private void assertStartsCorrectHomeMenuActivity(USER_TYPE user_type, int buttonToCheckFor) {

        //add a token to disk
        addSecurityTokenToDisk(user_type);

        //start activity
        checkSecurityTokenActivityTestRule.launchActivity(new Intent());

        //make sure correct home menu is displayed by checking
        //for list of problems or patients button
        Espresso.onView(ViewMatchers.withId(buttonToCheckFor)).check(matches(isDisplayed()));
    }

    private void addSecurityTokenToDisk(USER_TYPE user_type) {
        LOGINActivity.launchActivity(new Intent());
        Context context = LOGINActivity.getActivity().getBaseContext();

        //add security token
        SecurityToken securityToken = new SecurityToken(UserID, user_type);
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(securityToken, context);

        LOGINActivity.finishActivity();
    }
}