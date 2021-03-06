/*
 * Class name: BrowseUserActivityTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 15/11/18 11:43 AM
 *
 * Last Modified: 15/11/18 11:18 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Activities.BrowseUserActivity;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class BrowseUserActivityTest {
    //note, if this user is not in the database, this Activity will crash
    private static String UserID = "ImFromBrowseUserActivityTest";

    @Rule
    public final ActivityTestRule<BrowseUserActivity> mainActivity =
            new ActivityTestRule<>(BrowseUserActivity.class, false, false);

    @Before
    public void startActivity() {
        Intent intent = new Intent();
        intent.putExtra(USERIDEXTRA, UserID);
        mainActivity.launchActivity(intent);
    }

    //@Test
    //Fails
    public void onResume() {
        fail("Not implemented");
    }

    //@Test
    //Fails
    public void onItemClick() {
        fail("Not implemented");
    }
}