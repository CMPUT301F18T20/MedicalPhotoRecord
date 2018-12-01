package Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.AddPatientController;
import Controllers.BrowseUserController;
import Dialogs.AddPatientDialog;
import Exceptions.NoSuchUserException;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;


public class BrowseUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AddPatientDialog.AddPatientDialogListener {

    private ListView browse_user_list_view;
    private ArrayList<Patient> users;
    private BrowseUserController browseUserController = new BrowseUserController();
    private String providerId;
    private String patientId ;
    private FloatingActionButton addPatientButton;
    private Provider provider;

    private AddPatientController addPatientController = new AddPatientController();

    @Override
    protected void onStart(){
        super.onStart();

        // Get list of patients of that specific provider
        Intent intent = getIntent();
        this.providerId = intent.getStringExtra(USERIDEXTRA);
        this.users = browseUserController.getPatientListOfProvider(this,this.providerId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);

        this.browse_user_list_view = (ListView)findViewById(R.id.browse_user_id);
        this.browse_user_list_view.setOnItemClickListener(this);
        this.addPatientButton = (FloatingActionButton) findViewById(R.id.add_patient_button);

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
        try {
            addPatientController.addPatient(BrowseUserActivity.this, this.providerId, patientId);
        } catch (NoSuchUserException e) {
            Log.d("browseUserActivity","No such user exception thrown");
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        // Display list of users/ patients
        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this, R.layout.item_list,this.users);
        this.browse_user_list_view.setAdapter(adapter);
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id){
        // Get patient clicked
        this.patientId = browseUserController.getClickedPatientUserId(this.users, position);
        // Start new intent to view patient
        Intent intent = new Intent(BrowseUserActivity.this,ProviderBrowsePatientProblems.class);
        intent.putExtra(USERIDEXTRA, this.patientId);
        startActivity(intent);
    }

}
