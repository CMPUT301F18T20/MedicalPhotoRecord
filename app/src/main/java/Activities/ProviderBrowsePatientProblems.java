package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.BrowseProblemsController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * ProviderBrowsePatientProblems
 * This shows the provider a list of problems owned by the selected patient.
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

public class ProviderBrowsePatientProblems extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView patient_problems_list_view;
    protected TextView patient_id_text_view;
    protected String patientId ;
    private ArrayList<Problem> problems;
    protected ArrayAdapter<Problem> adapter;
    private String providerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_browse_patient_problems);

        Intent intent = getIntent();

        // Get user id and corresponding problem list
        this.patientId = intent.getStringExtra(USERIDEXTRA);
        this.providerID = intent.getStringExtra(PROVIDERID);
        this.problems = new BrowseProblemsController().getProblemList(this, this.patientId);

        // Set all views, button, context menu
        this.patient_id_text_view = findViewById(R.id.provider_browse_patient_problems_textView_id);
        this.patient_problems_list_view = findViewById(R.id.provider_browse_patient_problems_list_view_id);
        this.patient_problems_list_view.setOnItemClickListener(this);
        String textViewString = "Patient: " + this.patientId;
        this.patient_id_text_view.setText(textViewString);
        registerForContextMenu(this.patient_problems_list_view);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Adapter for problem list view
        this.adapter = new ArrayAdapter<>(this, R.layout.item_list, this.problems);
        this.patient_problems_list_view.setAdapter(adapter);

    }
    
    /**
     * onItemClick
     * When a problem is clicked, ProviderViewPatientProblem activity is started
     * inside that activity shows the information about the chosen problem.
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        // Get clicked problem
        Intent intent = new Intent(this, ProviderViewPatientProblem.class);
        Problem chosenProblem = (Problem) l.getItemAtPosition(position);
        String problemUUID = chosenProblem.getUUID();
        intent.putExtra(PROBLEMIDEXTRA, problemUUID);
        intent.putExtra(USERIDEXTRA, this.patientId);
        intent.putExtra(PROVIDERID, this.providerID);
        startActivity(intent);

    }

}
