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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Controllers.BrowseUserProblemsController;
import Controllers.ElasticsearchProblemController;
import Controllers.OfflineLoadController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ViewProblemActivity extends AppCompatActivity{

    protected TextView view_problem_title_text,
        view_problem_date_text,
        view_problem_description_text,
        view_problem_numRecords_text;

    protected Button setReminderButton,
        viewRecordsButton,
        viewMapButton,
        viewSlideshowButton;

    private int position;
    private ArrayList<Problem> problems;
    private Problem currentProblem;
    private String userId;
    private BrowseUserProblemsController problemController = new BrowseUserProblemsController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problem);

        //Extract selected problem object through intent and index of problem list
        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",0);
        this.userId = intent.getStringExtra(USERIDEXTRA); 
        this.problems = problemController.getProblemList(ViewProblemActivity.this,this.userId);
        this.currentProblem = this.problems.get(this.position);

        //initialize TextViews and Buttons
        this.view_problem_title_text = (TextView)findViewById(R.id.view_problem_title_id);
        this.view_problem_date_text = (TextView)findViewById(R.id.view_problem_date);
        this.view_problem_description_text = (TextView)findViewById(R.id.view_problem_description);
        this.view_problem_numRecords_text = (TextView)findViewById(R.id.view_problem_numRecords);
        this.setReminderButton = (Button)findViewById(R.id.view_problem_setReminder);
        this.viewRecordsButton = (Button)findViewById(R.id.view_problem_viewRecords);
        this.viewMapButton = (Button) findViewById(R.id.view_problem_viewMap);
        this.viewSlideshowButton = (Button)findViewById(R.id.view_problem_viewSlideshow);

        //set text for TextViews
        String tempString = "Problem: " + this.currentProblem.getTitle();
        this.view_problem_title_text.setText(tempString);

        tempString = "Date: " + this.currentProblem.getDate().toString();
        this.view_problem_date_text.setText(tempString);

        tempString = "Description: " + this.currentProblem.getDescription();
        this.view_problem_description_text.setText(tempString);

        tempString = "Number of Records: " + Integer.toString(this.currentProblem.getRecordCount());
        this.view_problem_numRecords_text.setText(tempString);
    }

    public void onSetReminderClick(View v){
        //TODO create intent and create activity class for setting alarm
    }
    public void onViewMapClick(View v){
        //TODO create intent and create activity class for viewing map
    }
    public void onViewRecordsClick(View v){
        Intent intent = new Intent(this, BrowseProblemRecords.class);
        intent.putExtra(USERIDEXTRA,this.userId);
        intent.putExtra("position",this.position);
        startActivity(intent);
    }
    public void onViewSlideshowClick(View v){
        //TODO create intent and create activity for viewing photo slideshow
    }
}
