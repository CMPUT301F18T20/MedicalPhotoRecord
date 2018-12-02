package Activities;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.ElasticsearchPatientController;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;

/**
 * PatientHomeMenuActivity
 * Home menu for a patient, this activity contains the
 * options for a patient to edit their contact information,
 * view their profile's details, view a list of their problems,
 * generate a code used for logging into a different device.
 *
 * @version 1.0
 * @since   2018-12-01
 */

public class PatientHomeMenuActivity extends HomeMenuActivity {

    protected int getLayout() {
        return R.layout.activity_patient_home_menu;
    }

    protected Class<?> getModifyActivityClass() {
        return ModifyPatientActivity.class;
    }

    protected void FetchUserFile() {
        try {
            //get the user info for the signed in patient
            ArrayList<Patient> patients = new ElasticsearchPatientController.GetPatientTask().execute(UserID).get();

            //grab the first (and hopefully only) patient in the results
            this.user = patients.get(0);

        } catch (Exception e) {
            Toast.makeText(this, "Exception while fetching patient file from database",
                    LENGTH_LONG).show();
        }
    }

    /**
     * This method is called when ListOfProblemsButton is clicked
     * passes the patient's userID and starts the BrowseProblemsActivity
     * @param v - current view
     */
    public void onListOfProblemsClick(View v) {
        Intent intent = new Intent(this, BrowseProblemsActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }
}
