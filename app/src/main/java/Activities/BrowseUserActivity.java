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
import com.cmput301f18t20.medicalphotorecord.R;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Controllers.AddPatientController;
import Controllers.BrowseUserController;
import Controllers.ElasticsearchPatientController;
import Dialogs.AddPatientDialog;
import Exceptions.NoSuchUserException;

import static GlobalSettings.GlobalSettings.PROVIDERID;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

/**
 * BrowseUserActivity Shows a list of patients when a
 * provider is using the application, it allows the provider
 * to add a patient using a valid patientID
 * when a patientID is clicked on the list all their
 * problems will be shown.
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */


public class BrowseUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AddPatientDialog.AddPatientDialogListener {

    protected ListView browse_user_list_view;
    private ArrayList<Patient> users;
    private BrowseUserController browseUserController = new BrowseUserController();
    private String providerId;
    private String patientId ;
    protected FloatingActionButton addPatientButton;
    private AddPatientController addPatientController = new AddPatientController();
    protected ArrayAdapter<Patient> adapter;

    /**
     *  this creates and grabs all the attributes needed to form the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_user);

        Intent intent = getIntent();
        this.providerId = intent.getStringExtra(USERIDEXTRA);

        try {
            this.users = new ElasticsearchPatientController.GetPatientsAssociatedWithProviderUserIDTask().execute(providerId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    /** openAddPatientDialog
     * this opens an AlertDialog that prompts the user to enter a valid
     * patientID to be added to the provider's list of patients
     */

    public void openAddPatientDialog(){
        AddPatientDialog addPatientDialog = new AddPatientDialog();
        addPatientDialog.show(getSupportFragmentManager(), "Add Patient Dialog");
    }

    /**verifyUserID
     * this calls the AddPatientController where it verifies if
     * the given patientID is valid or already added in the list of patients the provider has.
     * A patient is added on the provider's list of patients
     * if their patienID is valid and is not yet on the list.
     * @param patientId - ID of patient
     */

    @Override
    public void verifyUserID(String patientId) {
        this.patientId = patientId;
        addPatientController.addPatient(BrowseUserActivity.this, this.providerId, patientId);
        restartActivity();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            this.users = new ElasticsearchPatientController.GetPatientsAssociatedWithProviderUserIDTask().execute(providerId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.adapter.notifyDataSetChanged();
        this.adapter.clear();
        this.adapter.addAll(this.users);
        this.browse_user_list_view.setAdapter(adapter);

    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /** onItemClick
     * this starts the ProviderBrowsePatientProblems activity
     * If a patient is clicked on the list, a list of patient's problems will be shown.
     * @param l - adapter list
     * @param v - view
     * @param position - position of item clicked
     * @param id - id
     */

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