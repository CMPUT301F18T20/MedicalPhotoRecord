package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchProblemController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderViewPatientProblem extends AppCompatActivity {

    protected String problemTitle,
    problemDate, problemDescription, problemNumRecords;

    protected TextView problemTitleTextView,
            problemDateTextView, problemDescriptionTextView, problemNumRecordsTextView;

    private Button viewRecordButton,
    viewMapButton, biewSlideShowButton;

    protected String problemUUID;
    protected String patientID;
    private Problem problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_view_patient_problem);

        Intent intent = getIntent();

        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        this.patientID = intent.getStringExtra(USERIDEXTRA);

        try {
            this.problem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(this.problemUUID).get();
        } catch(ExecutionException e){
            throw new RuntimeException(e);
        }catch (InterruptedException i){
            throw new RuntimeException(i);
        }

        this.problemTitle = problem.getTitle();
        this.problemDate = problem.getDate().toString();
        this.problemDescription = problem.getDescription();
        this.problemNumRecords = "" + problem.getRecordCount();

        this.problemTitleTextView = findViewById(R.id.provider_view_problem_title_id);
        this.problemDateTextView=findViewById(R.id.provider_view_problem_date);
        this.problemDescriptionTextView = findViewById(R.id.provider_view_problem_description);
        this.problemNumRecordsTextView = findViewById(R.id.provider_view_problem_numRecords);

        this.problemTitleTextView.setText(this.problemTitle);
        this.problemDateTextView.setText(this.problemDate);
        this.problemDescriptionTextView.setText(this.problemDescription);
        this.problemNumRecordsTextView.setText(this.problemNumRecords);


    }

    public void onViewRecordsClick(View v){
        Intent intent = new Intent(this, BrowseProblemRecords.class);
        intent.putExtra(USERIDEXTRA,this.patientID);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        startActivity(intent);
    }
}
