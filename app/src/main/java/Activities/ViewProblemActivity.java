/*
 * Class name: ViewProblemActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/11/18 3:08 PM
 *
 * Last Modified: 12/11/18 3:06 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientRecordController;
import Controllers.ElasticsearchProblemController;
import Controllers.ModifyProblemController;
import Controllers.ProviderRecordsController;
import Exceptions.NoSuchProblemException;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;

/**
 * ViewProblemActivity
 * Simply displays the details and information of a selected problem
 * Contains title,date,description, number of records,
 * buttons to view map, records and a photo slideshow
 * and a button that allows the patient to set a reminder
 *
 * @version 1.0
 * @since   2018-12-01
 */
public class ViewProblemActivity extends AppCompatActivity{

    protected TextView view_problem_title_text,
        view_problem_date_text,
        view_problem_description_text,
        view_problem_numRecords_text;

    protected Button setReminderButton,
        viewRecordsButton,
        viewMapButton,
        viewSlideshowButton;

    private Problem currentProblem;
    private String userId;
    protected int numRecords;
    protected String problemUUID;

    //for debug
    private static final String TAG = "ViewProblemActivity";

    /**
     * Show title, text, description, number of records
     * Button for view map, set reminder, iew records, view slide show photos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problem);

        //Extract selected problem object through intent and index of problem list
        Intent intent = getIntent();
        this.userId = intent.getStringExtra(USERIDEXTRA);
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);

        try {
            this.currentProblem = new ModifyProblemController().getProblem(this,this.problemUUID);

        } catch(NoSuchProblemException e){
            //if problem is not found, terminate this activity and return to previous
            Toast.makeText(this, "Couldn't find problem", LENGTH_LONG).show();
            finish();
        }

        //initialize TextViews and Buttons
        this.view_problem_title_text = findViewById(R.id.view_problem_title_id);
        this.view_problem_date_text = findViewById(R.id.view_problem_date);
        this.view_problem_description_text = findViewById(R.id.view_problem_description);
        this.view_problem_numRecords_text = findViewById(R.id.view_problem_numRecords);
        this.setReminderButton = findViewById(R.id.view_problem_setReminder);
        this.viewRecordsButton = findViewById(R.id.view_problem_viewRecords);
        this.viewMapButton =  findViewById(R.id.view_problem_viewMap);
        this.viewSlideshowButton = findViewById(R.id.view_problem_viewSlideshow);

        //set text for TextViews
        String tempString = "Problem: " + this.currentProblem.getTitle();
        this.view_problem_title_text.setText(tempString);

        tempString = "Date: " + this.currentProblem.getDate().toString();
        this.view_problem_date_text.setText(tempString);

        tempString = "Description: " + this.currentProblem.getDescription();
        this.view_problem_description_text.setText(tempString);

        numRecords = new ProviderRecordsController().getRecords(this,this.problemUUID,this.userId).size();
        tempString = "Number of Records: " + Integer.toString(this.numRecords);
        this.view_problem_numRecords_text.setText(tempString);
        
    }

    /**
     * This method is called when view_problem_setReminder is clicked
     * and starts the AddReminderActivity.
     * @param v - current view
     */
    public void onSetReminderClick(View v){
        Intent intent = new Intent(this, AddReminderActivity.class);
        startActivity(intent);
    }

    /**
     * This method is called when view_problem_viewMap is clicked
     * and starts the ViewGeoActivity
     * @param v - current view
     */
    public void onViewMapClick(View v){
        Intent intent = new Intent(this, ViewMapActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);
    }

    /**
     * This method is called when view_problem_viewRecords is clicked
     * and starts the BrowseProblemRecords Activity
     * passes the patient's userID and the current problems UUID
     * @param v - current view
     */
    public void onViewRecordsClick(View v){
        Intent intent = new Intent(this, BrowseProblemRecords.class);
        intent.putExtra(USERIDEXTRA,this.userId);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);
    }

    /**
     * This method is called when view_problem_viewSlideshow is clicked
     * and starts the SlideshowActivity
     * passes the current problem's UUID
     * @param v - current view
     */
    public void onViewSlideshowClick(View v){
        Intent intent = new Intent(this, SlideshowActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);
    }
}
