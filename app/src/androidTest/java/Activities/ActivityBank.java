package Activities;

import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Rule;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProviderController;
import Enums.INDEX_TYPE;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.timeout;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ActivityBank {
    //can be started whenever if ActivityBank is inherited from
    @Rule
    public static final ActivityTestRule<Login> LOGINActivity =
            new ActivityTestRule<>(Login.class, false, false);

    public static void ClickSignUpAndEnterUserID(String UserID) {
        //click on sign up
        onView(withId(R.id.SignUpButton)).perform(click());

        //starts Signup activity
        //type in the userID and close keyboard
        onView(withId(R.id.UserIDBox)).perform(typeText(UserID), closeSoftKeyboard());
    }

    /** Assumes LOGINActivity has been started, goes through the process of logging the user in
     * @param UserID User ID to be used for signing up
     * @param Checkbox id of checkbox from R on sign up page to be clicked (ex: R.id.ProviderCheckbox)
     */
    public static void SignUpAsUser(String UserID, int Checkbox) {

        //go to sign up and enter a valid user id
        ClickSignUpAndEnterUserID(UserID);

        //click on PatientCheckBox to sign up as patient
        onView(withId(Checkbox)).perform(click());

        //click on sign up
        onView(withId(R.id.SignUpSaveButtton)).perform(click());
    }

    /** Assumes LOGINActivity has been started, goes through the process of logging the user in
     * @param UserID User ID to be used for signing up
     * @param Checkbox id of checkbox from R on sign up page to be clicked (ex: R.id.ProviderCheckbox)
     */
    public static void SignUpAsUserAndLogin(String UserID, int Checkbox) throws InterruptedException {

        SignUpAsUser(UserID, Checkbox);

        Thread.sleep(timeout);

        //click on Login
        onView(withId(R.id.LoginButton)).perform(click());

        Thread.sleep(timeout);
    }

    public static void changeToTestIndex() {
        //set to the test index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;
    }

    public static void WipeAllUsers() throws ExecutionException, InterruptedException {
        //delete all entries from the index
        new ElasticsearchPatientController.DeletePatientsTask().execute().get();
        new ElasticsearchProviderController.DeleteProvidersTask().execute().get();
    }

    public static void CommonSetUp() throws ExecutionException, InterruptedException {
        changeToTestIndex();
        WipeAllUsers();
    }


    public static void ClickOnViewProfileAndAssertCorrectActivityAndUser(String UserID) {
        //click on view contact info button
        onView(withId(R.id.ViewProfileButton)).perform(click());

        //check USERID box is visible
        onView(withId(R.id.UserIDBox)).check(matches(isDisplayed()));

        //verify user id box has our userid
        onView(withId(R.id.UserIDBox)).check(matches(withText(UserID)));
    }
}
