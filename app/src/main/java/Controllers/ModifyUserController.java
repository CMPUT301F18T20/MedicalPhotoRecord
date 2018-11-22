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

        // Online
        try {
            Patient onlinePatient = (new ElasticsearchPatientController.GetPatientTask().execute(userId).get()).get(0);
            return onlinePatient;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Initialize a stand by user in case user is not found (which is unlikely)
        Patient userNotFound = null;

        // Offline
        this.patients = browseUserController.getPatientList(context);
        for (Patient user : this.patients) {
            if (userId.equals(user.getUserID())) {
                return user;
            }
        }
        return userNotFound;
    }

    public Patient createNewPatient(Context context, String userId, String email, String phoneNumber) throws UserIDMustBeAtLeastEightCharactersException {

        Patient patient = null;
        // Get old patient
        try {
            Patient old_patient = (new ElasticsearchPatientController.GetPatientTask().execute(userId).get()).get(0);

            // Create new patient
            patient = new Patient(userId, email, phoneNumber);
            patient.setElasticSearchID(old_patient.getElasticSearchID());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return patient;
    }

    public void savePatient(Context context, Patient newPatient) {

        // Get most updated list of patients
        this.patients = this.browseUserController.getPatientList(context);

        // Modify (Remove old user from user list, both offline and online) (offline: may not need this when syncing)
        for (Patient u : new ArrayList<Patient>(this.patients)) {
            if (newPatient.getUserID().equals(u.getUserID())) {
                this.patients.remove(u);
            }
        }

        // Offline saves (may not need this when syncing)
        this.patients.add(newPatient);
        offlineSaveController.savePatientList(patients, context);

        // Online Saves
        try {
            new ElasticsearchPatientController.SaveModifiedPatient().execute(newPatient).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //new ElasticsearchPatientController.DeletePatientsTask().execute(newPatient.getUserID());
        //new ElasticsearchPatientController.AddPatientTask().execute(newPatient);
    }
}
