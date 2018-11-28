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
import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchController;
import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchSecurityTokenController;
import Controllers.ElasticsearchShortCodeController;
import Controllers.IOLocalSecurityTokenController;
import Controllers.ShortCodeController;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;

import static Controllers.IOLocalSecurityTokenController.loadSecurityTokenFromDisk;
import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;

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
                    LENGTH_LONG).show();
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

    //generate login code for another device to use for login using security token
    public void onGenerateCodeClick(View v) {
        try {
            ShortCodeController.AddCode(this.UserID, this);
        } catch(failedToFetchSecurityTokenException e) {
            Toast.makeText(this,
                    "Unable to load a security token for this user", LENGTH_LONG).show();
        } catch(failedToAddShortCodeException e) {
            Toast.makeText(this, "Unable to add the short code", LENGTH_LONG).show();
        }
    }
}
