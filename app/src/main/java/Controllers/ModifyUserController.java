package Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.BrowseUserActivity;
import Activities.ModifyUserActivity;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyUserController {

    private ArrayList<Patient> patients;
    private BrowseUserController browseUserController = new BrowseUserController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();


    public Patient getPatient(Context context, String userId) {

        // Initialize a stand by user in case user is not found (which is unlikely)
        Patient userNotFound = null;

        // Get user List, get user from userId
        this.patients = browseUserController.getPatientList(context);
        for (Patient user : this.patients) {
            if (userId.equals(user.getUserID())) {
                return user;
            }
        }
        return userNotFound;
    }

    public Patient createNewPatient(Context context, String userId, String email, String phoneNumber) {
        // Create new user to be added
        Patient patient = null;
        try {
            patient = new Patient(userId, email, phoneNumber);
        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(context, "User id has to be longer than 8 characters", Toast.LENGTH_LONG).show();
        }
        return patient;
    }

    public void savePatient(Context context, Patient patient) {

        // Get most updated list of patients
        this.patients = this.browseUserController.getPatientList(context);

        // Modify (Remove old user from user list, both offline and online) (offline: may not need this when syncing)
        for (Patient u : new ArrayList<Patient>(this.patients)) {
            if (patient.getUserID().equals(u.getUserID())) {
                this.patients.remove(u);
                // new ElasticsearchPatientController.DeletePatientTask().execute(u);
            }
        }

        // Offline saves (may not need this when syncing)
        this.patients.add(patient);
        offlineSaveController.savePatientList(patients, context);

        // Elastic search Saves
        new ElasticsearchPatientController.AddPatientTask().execute(patient);
        Toast.makeText(context, "Your user info have been saved", Toast.LENGTH_LONG).show();

    }
}
