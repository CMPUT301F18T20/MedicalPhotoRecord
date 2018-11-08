package com.cmput301f18t20.medicalphotorecord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Activities.BrowseUserActivity;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class BrowseUserActivityTest {

    @Rule
    public final ActivityTestRule<BrowseUserActivity> mainActivity =
            new ActivityTestRule<>(BrowseUserActivity.class);

    @Test
    public void onResume() {
    }

    @Test
    public void onItemClick() {
    }
}