package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import Controllers.ElasticsearchPatientController;

import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class PatientHomeMenuActivity extends AppCompatActivity {

    private String UserID;
    private Patient patient = null;

    protected Button EditContactInfoButton,
            ListOfProblemsButton,
            ViewProfileButton,
            DeleteProfileButton,
            LogOutButton;

    protected TextView UserIDWelcomeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_menu);

        //set up the buttons and text edit
        EditContactInfoButton = findViewById(R.id.EditContactInfoButton);
        ListOfProblemsButton = findViewById(R.id.ListOfProblemsButton);
        ViewProfileButton = findViewById(R.id.ViewProfileButton);
        DeleteProfileButton = findViewById(R.id.DeleteProfile);
        LogOutButton = findViewById(R.id.LogOutButton);
        UserIDWelcomeBox = findViewById(R.id.UserIDWelcomeBox);

        //extract the user id from the intent
        Intent intent = getIntent();
        UserID = intent.getStringExtra(USERIDEXTRA);
        String newText = "Welcome " + UserID;

        UserIDWelcomeBox.setText(newText);

        FetchPatientFile();
    }

    private void FetchPatientFile() {
        try {
            //get the user info for the signed in patient
            ArrayList<Patient> patients = new ElasticsearchPatientController.GetPatientTask().execute(UserID).get();

            //grab the first (and hopefully only) patient in the results
            patient = patients.get(0);

        } catch (Exception e) {
            Toast.makeText(this, "Exception while fetching patient file from database",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(this, ModifyPatientActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onListOfProblemsClick(View v) {
        Intent intent = new Intent(this, BrowseProblemsActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onViewProfileClick(View v) {
        FetchPatientFile();

        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        intent.putExtra(EMAILEXTRA, patient.getEmail());
        intent.putExtra(PHONEEXTRA, patient.getPhoneNumber());
        startActivity(intent);
    }

    public void onDeleteClick(View v) {
        //create fragment that comes up and asks if the user is sure
    }

    //generate login code using security token
    public void onGenerateCodeClick(View v) {
        /*
        SecurityTokenAndUserIDPair securityTokenAndUserIDPair = null;

        try {
            //load from offline
            securityTokenAndUserIDPair = IOLocalSecurityTokenAndIDPairController
                    .loadSecurityTokenAndUserIDPairFromDisk(context);

        //not an issue if file isn't found, we'll go looking in elasticsearch
        } catch (FileNotFoundException e) {
            //maybe user cleared it for some reason, try elasticsearch
            securityTokenAndUserIDPair = ElasticsearchSecurityTokenAndUserIDPairController
                    .getByUserIDTask().execute(this.UserID).get();
        }

        if (securityTokenAndUserIDPair == null) {
            Toast.makeText(this, "Unable to load a security token for this user").show();
            //return FALSE or maybe generate an exception
        } else {
            //add security token and small generated code to the elasticsearch
            //TODO code that I'm writing right now!
        }
        */

    }


}
