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


    public Patient getUser(Context context, String userId) {

        // Initialize a stand by user in case user is not found (which is unlikely)
        Patient userNotFound = null;

        // Get user List, get user from userId
        this.patients = browseUserController.getUserList(context);
        for (Patient user : this.patients) {
            if (userId.equals(user.getUserID())) {
                return user;
            }
        }
        return userNotFound;
    }

    public void saveUser(Context context, String userId, String gotEmail, String gotPhone) {

        // Get most updated list of patients
        this.patients = this.browseUserController.getUserList(context);

        // Modify (Remove old user from user list, both offline and online) (offline: may not need this when syncing)
        for (Patient u : new ArrayList<Patient>(this.patients)) {
            if (userId.equals(u.getUserID())) {
                this.patients.remove(u);
                new ElasticsearchPatientController.DeletePatientTask().execute(u);
            }
        }

        // Create new user to be added
        Patient user = null;
        try {
            user = new Patient(userId, gotEmail, gotPhone);

            // Offline saves (may not need this when syncing)
            this.patients.add(user);
            offlineSaveController.savePatientList(patients, context);

            // Elastic search Saves
            new ElasticsearchPatientController.AddPatientTask().execute(user);

            Toast.makeText(context, "Your infos have been saved locally and online", Toast.LENGTH_LONG).show();

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(context, "User id has to be longer than 8 characters", Toast.LENGTH_LONG).show();
        }
    }
}
