package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BrowseUserController {

    private ArrayList<Patient> patients;
    private ElasticsearchPatientController elasticsearchPatientController = new ElasticsearchPatientController();
    private OfflineLoadController offlineLoadController = new OfflineLoadController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();

    public ArrayList<Patient> getUserList(Context context) {

        // Offline
        this.patients = offlineLoadController.loadPatientList(context);

        // Online
        try {
            this.patients = new ElasticsearchPatientController.GetPatientTask().execute().get();
        } catch (InterruptedException e) {
            Log.d("ElasticsearchProviderCo",
                    "Computation threw an exception. " + context.toString());
            Log.d("ElasticsearchProviderCo", e.getStackTrace().toString());
        } catch (ExecutionException e) {
            Log.d("ElasticsearchProviderCo",
                    "Current thread was interrupted while waiting. " + context.toString());
            Log.d("ElasticsearchProviderCo", e.getStackTrace().toString());
        }

        // Some kind of controller for getting the most updated list of patients (issue 102)
        return this.patients;
    }
}
