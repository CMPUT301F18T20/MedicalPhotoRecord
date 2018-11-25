package Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.R;
import java.util.ArrayList;

import Controllers.AddPatientController;
import Controllers.BrowseProviderPatientsController;
import Controllers.BrowseUserController;
import Dialogs.AddPatientDialog;

import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;


public class BrowseUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AddPatientDialog.AddPatientDialogListener {

    private ListView browse_user_list_view;
    private ArrayList<Patient> users;
    private BrowseUserController browseUserController = new BrowseUserController();
    private String providerId;
    private String patientId ;
    protected FloatingActionButton addPatientButton;
    private AddPatientController addPatientController = new AddPatientController();
    protected ArrayAdapter<Patient> adapter;

    private BrowseProviderPatientsController browseProviderPatientsController = new BrowseProviderPatientsController();

    @Override
    protected void onStart(){
        super.onStart();

        // Get list of patients of that specific provider
        Intent intent = getIntent();
        this.providerId = intent.getStringExtra(USERIDEXTRA);
        this.users = browseProviderPatientsController.getPatientList(this,this.providerId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);

        this.browse_user_list_view = findViewById(R.id.browse_user_id);
        this.browse_user_list_view.setOnItemClickListener(this);
        this.addPatientButton = findViewById(R.id.add_patient_button);

        registerForContextMenu(this.browse_user_list_view);

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPatientDialog();
            }
        });

    }

    public void openAddPatientDialog(){
        AddPatientDialog addPatientDialog = new AddPatientDialog();
        addPatientDialog.show(getSupportFragmentManager(), "Add Patient Dialog");
    }

    @Override
    public void verifyUserID(String patientId) {
        this.patientId = patientId;
        addPatientController.addPatient(BrowseUserActivity.this, this.providerId, patientId);
    }



    @Override
    protected void onResume(){
        super.onResume();
        // Display list of users/ patients
        this.adapter = new ArrayAdapter<>(this, R.layout.item_list,this.users);
        this.browse_user_list_view.setAdapter(adapter);
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id){
        // Get patient clicked
        this.patientId = browseUserController.getClickedPatientUserId(this.users, position);
        // Start new intent to view patient
        Intent intent = new Intent(BrowseUserActivity.this,ProviderBrowsePatientProblems.class);
        intent.putExtra(USERIDEXTRA, this.patientId);
        intent.putExtra(PROVIDERID, this.providerId);
        startActivity(intent);
    }

}
