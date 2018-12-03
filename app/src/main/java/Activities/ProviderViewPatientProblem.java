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

import Controllers.ElasticsearchProblemController;
import Controllers.ModifyProblemController;
import Controllers.ProviderRecordsController;
import Exceptions.NoSuchProblemException;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * ProviderViewPatientProblem Activity
 * simply displays the information of a problem to the provider
 * A provider can view/add the problems records
 * A provider can view the photos in a slideshow
 * A provider can view the geolocation of a problem
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

public class ProviderViewPatientProblem extends AppCompatActivity {

    protected String problemTitle,
    problemDate, problemDescription, problemNumRecords;

    protected TextView problemTitleTextView,
            problemDateTextView, problemDescriptionTextView, problemNumRecordsTextView;


    protected Button viewRecordButton,
    viewMapButton, viewSlideShowButton;

    protected String problemUUID;
    protected String patientID;
    protected Problem problem;
    protected  String providerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_view_patient_problem);

        Intent intent = getIntent();

        this.providerID = intent.getStringExtra(PROVIDERID);
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        this.patientID = intent.getStringExtra(USERIDEXTRA);

        try {
            this.problem = new ModifyProblemController().getProblem(this,this.problemUUID);
        } catch(NoSuchProblemException e){
            Toast.makeText(this, "Problem does not exist", Toast.LENGTH_LONG).show();
        }

        int numRecords = new ProviderRecordsController().getRecords(this,this.problemUUID,this.patientID).size();

        this.problemTitle = problem.getTitle();
        this.problemDate = problem.getDate().toString();
        this.problemDescription = problem.getDescription();
        this.problemNumRecords = "" + String.valueOf(numRecords);

        this.problemTitleTextView = findViewById(R.id.provider_view_problem_title_id);
        this.problemDateTextView=findViewById(R.id.provider_view_problem_date);
        this.problemDescriptionTextView = findViewById(R.id.provider_view_problem_description);
        this.problemNumRecordsTextView = findViewById(R.id.provider_view_problem_numRecords);

        this.problemTitleTextView.setText(this.problemTitle);
        this.problemDateTextView.setText(this.problemDate);
        this.problemDescriptionTextView.setText(this.problemDescription);
        this.problemNumRecordsTextView.setText(this.problemNumRecords);

        this.viewMapButton = findViewById(R.id.provider_view_problem_viewMap);
        this.viewRecordButton = findViewById(R.id.provider_view_problem_viewRecords);
        this.viewSlideShowButton = findViewById(R.id.provider_view_problem_viewSlideshow);


    }

    /**
     * onProviderViewRecordsClick
     * This method is called when the provider_view_problem_viewRecords button is clicked
     * ProviderBrowseProblemRecords activity is started
     *
     * @param v - current view
     */
    public void onProviderViewRecordsClick(View v){
        Intent intent = new Intent(ProviderViewPatientProblem.this, ProviderBrowseProblemRecords.class);
        intent.putExtra(USERIDEXTRA,this.patientID);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra(PROVIDERID,this.providerID);
        startActivity(intent);
    }

    /**
     * onProviderViewMapButtonClick
     * This method is called when the provider_view_problem_viewMap button is clicked
     * ViewMapsActivity activity is started which displays the map
     * and places where records have been saved
     *
     * @param v - current view
     */

    // TODO link the map
    public void onProviderViewMapButtonClick(View v){
        Intent intent = new Intent(ProviderViewPatientProblem.this, ViewMapActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);
    }

    /**
     * onProviderViewSlideshowClick
     * This method is called when the provider_view_problem_viewSlideshow button is clicked
     *  activity is started which displays a slideshow of pictures that were recorded
     *
     *
     * @param v - current view
     */
    public void onProviderViewSlideshowClick(View v){
        //TODO create intent and create activity for viewing photo slideshow
        Intent intent = new Intent(this, SlideshowActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);
    }
}
