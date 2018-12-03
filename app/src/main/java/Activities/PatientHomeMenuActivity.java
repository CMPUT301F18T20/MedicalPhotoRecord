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
import Enums.USER_TYPE;
import Exceptions.NoSuchUserException;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;

import static Controllers.IOLocalSecurityTokenController.loadSecurityTokenFromDisk;
import static Enums.USER_TYPE.PATIENT;
import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;

public class PatientHomeMenuActivity extends HomeMenuActivity {

    protected int getLayout() {
        return R.layout.activity_patient_home_menu;
    }

    protected Class<?> getModifyActivityClass() {
        return ModifyPatientActivity.class;
    }

    protected void FetchUserFile() throws NoSuchUserException {
        try {
            //get the user info for the signed in patient
            ArrayList<Patient> patients = new ElasticsearchPatientController.GetPatientTask().execute(UserID).get();

            //grab the first (and hopefully only) patient in the results
            this.user = patients.get(0);

        } catch (Exception e) {
            Toast.makeText(this, "Exception while fetching patient file from database",
                    LENGTH_LONG).show();

            throw new NoSuchUserException();
        }
    }

    public void onListOfProblemsClick(View v) {
        Intent intent = new Intent(this, BrowseProblemsActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    @Override
    protected USER_TYPE getUserType() {
        return PATIENT;
    }
}
