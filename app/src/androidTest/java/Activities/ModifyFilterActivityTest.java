/*
 * Class name: ModifyFilterActivityTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 03/12/18 10:03 AM
 *
 * Last Modified: 03/12/18 10:03 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.Filter;
import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static Enums.USER_TYPE.PATIENT;
import static GlobalSettings.GlobalSettings.FILTEREXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static GlobalSettings.GlobalSettings.USERTYPEEXTRA;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;

public class ModifyFilterActivityTest {

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<ModifyFilterActivity> ModifyFilterActivity =
            new ActivityTestRule<>(ModifyFilterActivity.class,
                    false, false);

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<SearchActivity> searchActivity =
            new ActivityTestRule<>(SearchActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(FILTEREXTRA, new Filter(FALSE, FALSE, FALSE, FALSE, FALSE));
        ModifyFilterActivity.launchActivity(intent);
    }

    public void setUpSearchActivity() {
        //stop previous activity
        ModifyFilterActivity.finishActivity();

        Intent intent = new Intent();
        intent.putExtra(USERIDEXTRA, "notimportant");
        intent.putExtra(USERTYPEEXTRA, PATIENT);
        searchActivity.launchActivity(intent);
    }

    @Test
    public void onLocationClick() {

        //check them and make sure they're checked
        checkProblemsAndRecords();

        //check location
        onView(withId(R.id.IncludeLocation)).perform(click());

        //make sure these are now not checked
        assertRecordsAndProblemsNotChecked();
    }

    @Test
    public void onBodyLocationClick() {

        //check them and make sure they're checked
        checkProblemsAndRecords();

        //check body location
        onView(withId(R.id.IncludeBodyLocation)).perform(click());

        //make sure these are now not checked
        assertRecordsAndProblemsNotChecked();
    }

    @Test
    public void onSearchProblemsClick() {

        //check them and make sure they're checked
        checkLocationAndBodyLocation();

        //check problems
        onView(withId(R.id.SearchForProblems)).perform(click());

        //make sure these are now not checked
        assertLocationAndBodyLocationNotChecked();
    }

    @Test
    public void onSearchRecordsClick() {

        //check them and make sure they're checked
        checkLocationAndBodyLocation();

        //check records
        onView(withId(R.id.SearchForRecords)).perform(click());

        //make sure these are now not checked
        assertLocationAndBodyLocationNotChecked();
    }

    @Test
    public void testChangingFilterSettingsCarriesOverToSearchActivity() throws InterruptedException {
        setUpSearchActivity();

        //go to change filter settings
        onView(withId(R.id.QuerySettings)).perform(click());

        //make sure these are in their default setting
        onView(withId(R.id.IncludeBodyLocation)).check(matches(isNotChecked()));
        onView(withId(R.id.IncludeLocation)).check(matches(isNotChecked()));
        onView(withId(R.id.SearchForProblems)).check(matches(isChecked()));
        onView(withId(R.id.SearchForPatientRecord)).check(matches(isNotChecked()));
        onView(withId(R.id.SearchForRecords)).check(matches(isNotChecked()));

        checkManyAndMakeSureTheyreChecked(R.id.SearchForPatientRecord, R.id.SearchForRecords);

        //save filter and return to query activity
        onView(withId(R.id.saveFilter)).perform(click());

        //press search button so we know we've switched activities
        onView(withId(R.id.SearchButton)).perform(click());

        //get filter
        Filter filterSettings = searchActivity.getActivity().filter;

        //make sure the values match what we expected
        assertEquals("geo was not false", FALSE, filterSettings.GeoIncluded());
        assertEquals("bodyLocation was not false", FALSE, filterSettings.BodyLocationIncluded());
        assertEquals("Problems was not true", TRUE, filterSettings.SearchForProblems());
        assertEquals("records was not true", TRUE, filterSettings.SearchForRecords());
        assertEquals("patient records was not true", TRUE, filterSettings.SearchForPatientRecords());
    }

    private void checkManyAndMakeSureTheyreChecked(int... Checkboxes) {
        for (int checkbox : Checkboxes) {
            //check
            onView(withId(checkbox)).perform(click());

            //make sure it's checked
            onView(withId(checkbox)).check(matches(isChecked()));
        }
    }

    private void MakeSureTwoArentChecked(int firstCheckbox, int secondCheckbox) {
        //make sure these are now not checked
        onView(withId(firstCheckbox)).check(matches(isNotChecked()));
        onView(withId(secondCheckbox)).check(matches(isNotChecked()));
    }

    private void checkProblemsAndRecords() {
        checkManyAndMakeSureTheyreChecked(R.id.SearchForRecords, R.id.SearchForProblems);
    }

    private void checkLocationAndBodyLocation() {
        checkManyAndMakeSureTheyreChecked(R.id.IncludeBodyLocation, R.id.IncludeLocation);
    }

    private void assertRecordsAndProblemsNotChecked() {
        MakeSureTwoArentChecked(R.id.SearchForRecords, R.id.SearchForProblems);
    }

    private void assertLocationAndBodyLocationNotChecked() {
        MakeSureTwoArentChecked(R.id.IncludeBodyLocation, R.id.IncludeLocation);
    }
}