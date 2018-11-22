package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.BrowseUserProblemsController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderViewPatientActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String patientId;
    private ArrayList<Problem> problems;
    private BrowseUserProblemsController browseUserProblemsController = new BrowseUserProblemsController();
    private ListView provider_view_patient_list_view;
    private ArrayAdapter<Problem> adapter;


    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        this.patientId = intent.getStringExtra(USERIDEXTRA);
        this.problems = browseUserProblemsController.getProblemList(ProviderViewPatientActivity.this, this.patientId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_view_patient);

        this.provider_view_patient_list_view = (ListView)findViewById(R.id.provider_view_patient_list_view_id);
        this.provider_view_patient_list_view.setOnItemClickListener(this);
        registerForContextMenu(this.provider_view_patient_list_view);
    }

    @Override
    protected void onResume(){
        super.onResume();

        this.adapter = new ArrayAdapter<Problem>(this, R.layout.item_list,this.problems);
        this.provider_view_patient_list_view.setAdapter(adapter);

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id){

        // Get position of clicked item and pass it on to Item Activity for later processing
        Intent intent = new Intent(ProviderViewPatientActivity.this,ViewProblemActivity.class);
        intent.putExtra("position", position);
        intent.putExtra(USERIDEXTRA,this.patientId);
        startActivity(intent);
    }

}
