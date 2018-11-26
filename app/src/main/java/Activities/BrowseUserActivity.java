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
<<<<<<< HEAD
=======

>>>>>>> f32a90acd45f60debf62c3e0bebce02defc10636
import java.util.ArrayList;

import Controllers.AddPatientController;
import Controllers.BrowseUserController;
import Dialogs.AddPatientDialog;

<<<<<<< HEAD
import static GlobalSettings.GlobalSettings.PROVIDERID;
=======
>>>>>>> f32a90acd45f60debf62c3e0bebce02defc10636
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);

        Intent intent = getIntent();
        this.providerId = intent.getStringExtra(USERIDEXTRA);
<<<<<<< HEAD
        this.users = browseProviderPatientsController.getPatientList(this,this.providerId);
=======
        this.users = browseUserController.getPatientListOfProvider(this,this.providerId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);
>>>>>>> f32a90acd45f60debf62c3e0bebce02defc10636

        this.browse_user_list_view = findViewById(R.id.browse_user_id);
        this.browse_user_list_view.setOnItemClickListener(this);

        registerForContextMenu(this.browse_user_list_view);

        this.adapter = new ArrayAdapter<>(this, R.layout.item_list,this.users);

        this.addPatientButton = findViewById(R.id.add_patient_button);
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPatientDialog();
            }
        });

        this.browse_user_list_view.setAdapter(adapter);
    }

    public void openAddPatientDialog(){
        AddPatientDialog addPatientDialog = new AddPatientDialog();
        addPatientDialog.show(getSupportFragmentManager(), "Add Patient Dialog");
    }

    @Override
    public void verifyUserID(String patientId) {
        this.patientId = patientId;
        addPatientController.addPatient(BrowseUserActivity.this, this.providerId, patientId);

<<<<<<< HEAD
=======

    @Override
    protected void onResume(){
        super.onResume();
        // Display list of users/ patients
        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this, R.layout.item_list,this.users);
        this.browse_user_list_view.setAdapter(adapter);
>>>>>>> f32a90acd45f60debf62c3e0bebce02defc10636
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
