package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.BrowseUserProblemsController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderViewProblemActivity extends AppCompatActivity {

    protected TextView problem_title_text,
            problem_date_text,
            problem_description_text,
            problem_numRecords_text;

    protected Button
            viewRecordsButton,
            viewMapButton,
            viewSlideshowButton;

    private int position;
    private ArrayList<Problem> problems;
    private Problem currentProblem;
    private String patientId;
    private BrowseUserProblemsController problemController = new BrowseUserProblemsController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_view_problem);

        //Extract selected problem object through intent and index of problem list
        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",0);
        this.patientId = intent.getStringExtra(USERIDEXTRA);
        this.problems = problemController.getProblemList(ProviderViewProblemActivity.this,this.patientId);
        this.currentProblem = this.problems.get(this.position);

        //initialize TextViews and Buttons
        this.problem_title_text = (TextView)findViewById(R.id.provider_view_problem_title_id);
        this.problem_date_text = (TextView)findViewById(R.id.provider_view_problem_date);
        this.problem_description_text = (TextView)findViewById(R.id.provider_view_problem_description);
        this.problem_numRecords_text = (TextView)findViewById(R.id.provider_view_problem_numRecords);

        this.viewRecordsButton = (Button)findViewById(R.id.provider_view_problem_viewRecords);
        this.viewMapButton = (Button) findViewById(R.id.provider_view_problem_viewMap);
        this.viewSlideshowButton = (Button)findViewById(R.id.provider_view_problem_viewSlideshow);


        //set text for TextViews
        String tempString = "Problem: " + this.currentProblem.getTitle();
        this.problem_title_text.setText(tempString);

        tempString = "Date: " + this.currentProblem.getDate().toString();
        this.problem_date_text.setText(tempString);

        tempString = "Description: " + this.currentProblem.getDescription();
        this.problem_description_text.setText(tempString);

        tempString = "Number of Records: " + Integer.toString(this.currentProblem.getRecordCount());
        this.problem_numRecords_text.setText(tempString);
    }

    public void onViewMapClick(View v){
        Intent intent = new Intent(this, ViewMapsActivity.class);
        startActivity(intent);
    }
    public void onViewRecordsClick(View v){
        Intent intent = new Intent(this, BrowseProblemRecords.class);
        intent.putExtra(USERIDEXTRA,this.patientId);
        intent.putExtra("position",this.position);
        startActivity(intent);
    }
    public void onViewSlideshowClick(View v){
        //TODO create intent and create activity for viewing photo slideshow
    }
}
