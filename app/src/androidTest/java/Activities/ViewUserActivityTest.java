package Activities;

import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

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
        i.putExtra( ViewUserActivity.UserIDExtra, InitialUserIDInIntent);
        i.putExtra(ViewUserActivity.EmailExtra, InitialEmailInIntent);
        i.putExtra(ViewUserActivity.PhoneExtra, InitialPhoneInIntent);
        Activity.launchActivity(i);
    }


    @Test
    public void CorrectInformationDisplayed() {
        //User ID matches
        onView(withId(R.id.UserIDBox)).check(matches(withText(InitialUserIDInIntent)));

        //Email matches
        onView(withId(R.id.EmailBox)).check(matches(withText(InitialEmailInIntent)));

        //Phone matches
        onView(withId(R.id.PhoneBox)).check(matches(withText(InitialPhoneInIntent)));


    }

    @Test
    public void onBackClick() {
        fail("Needs to be tested from Provider and Patient home activities");
    }
}