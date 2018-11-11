package Activities;

import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

public class PatientHomeMenuActivityTest {

    private String InitialUserIDInIntent;

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<PatientHomeMenuActivity> PatientActivity =
            new ActivityTestRule<>(PatientHomeMenuActivity.class,
                    false, false);

    //put the user id into the intent and then start the activity
    @Before
    public void setUp() {
        InitialUserIDInIntent = "PatientHomeMenuActivityTestUser";
        Intent i = new Intent();
        i.putExtra("UserID", InitialUserIDInIntent);
        PatientActivity.launchActivity(i);
    }

    @Test
    public void CorrectUserIDDisplayed() {
        //verifies the intent is read correctly and the user id is displayed on screen

        //User ID should be displayed on screen along with Welcome
        onView(withId(R.id.UserIDWelcomeBox)).check(matches(withText("Welcome " + InitialUserIDInIntent)));
    }

    @Test
    public void onEditClickBringsUpEditUserActivityWithCorrectUserID() {
        //verifies that the intent carried the user id to the activity and it was correctly read

        //click on edit contact info button
        onView(withId(R.id.EditContactInfoButton)).perform(click());

        //check userid_edit_id box is visible
        onView(withId(R.id.userid_edit_id)).check(matches(isDisplayed()));

        //make sure the User ID that we got from the Login intent has been loaded into the
        //intent when transitioning into the edit user id activity.
        onView(withId(R.id.userid_edit_id)).check(matches(withText(InitialUserIDInIntent)));
    }

    @Test
    public void onListOfProblemsClick() {
        fail("Not completely implemented");
    }

    @Test
    public void onViewProfileClick() {
        //verifies that the intent carried the user id to the activity and it was correctly read

        //click on view contact info button
        onView(withId(R.id.ViewProfileButton)).perform(click());

        //check userid_edit_id box is visible
        //TODO find the user id box in the view user activity to check
        //onView(withId(R.id.userid_edit_id)).check(matches(isDisplayed()));

        //make sure the User ID that we got from the Login intent has been loaded into the
        //intent when transitioning into the edit user id activity.
        //TODO find the user id box in the view user activity to check
        //onView(withId(R.id.userid_edit_id)).check(matches(withText(InitialUserIDInIntent)));

        fail("Not completely implemented");
    }

    @Test
    public void onDeleteClick() {
        fail("Not completely implemented");
    }

    @Test
    public void onLogoutClickLogsUserOut() {
        //TODO will likely need to login from login page and then click logout
        //TODO as logging out returns to the previous activity on the stack
        //Should log the user out back to the Login page

        //click on logout button
        onView(withId(R.id.LogOutButton)).perform(click());

        fail("Not completely implemented");

    }
}