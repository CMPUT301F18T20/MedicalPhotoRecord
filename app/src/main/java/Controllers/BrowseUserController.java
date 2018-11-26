package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.User;
import com.google.android.gms.common.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class BrowseUserController {

    private ArrayList<Patient> patients;
    private ArrayList<Provider> providers;
    private Provider provider;
    private ElasticsearchPatientController elasticsearchPatientController = new ElasticsearchPatientController();
    private OfflineLoadController offlineLoadController = new OfflineLoadController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();

    // Get all patients
    public ArrayList<Patient> getPatientList(Context context) {

        // Offline
        ArrayList<Patient> offlinePatients = offlineLoadController.loadPatientList(context);

        // Online
        ArrayList<Patient> onlinePatients = null;
        try {
            onlinePatients = new ElasticsearchPatientController.GetPatientTask().execute().get();
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
        ArrayList<Patient> actualPatients = onlinePatients;
        return actualPatients;
    }

    // Get all providers
    public ArrayList<Provider> getProviderList(Context context){

        // Offline
        ArrayList<Provider> offlineProviders = offlineLoadController.loadProviderList(context);

        // Online
        ArrayList<Provider> onlineProviders = null;
        try {
            onlineProviders = new ElasticsearchProviderController.GetProviderTask().execute().get();
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
        ArrayList<Provider> actualProviders = onlineProviders;
        return actualProviders;
    }

    // Get all patients of certain provider
    public ArrayList<Patient> getPatientListOfProvider(Context context, String providerId){

        // Online
        ArrayList<Patient> onlinePatientsForProvider = null;
        try {
            onlinePatientsForProvider = new ElasticsearchPatientController.
                    GetPatientsAssociatedWithProviderUserIDTask().execute(providerId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Offline (kind of backward but oh well)
        ArrayList<Patient> patients = new OfflineLoadController().loadPatientList(context);
        ArrayList<Patient> offlinePatientsForProvider = new ArrayList<>();

        // Loop through all existing patient and check if their list of associated provider ids contain provider id
        for (Patient p:patients){
            try{
                ArrayList<String> patientsListofProviders = p.getAssociatedProviderIDs();
                if (patientsListofProviders.contains(providerId)){
                    offlinePatientsForProvider.add(p);
                }
            }catch (Exception e){
                Log.d("BrowseUserController","for those earlier patients who do not have associated provider id list");
            }
        }
        
        // Syncing
        ArrayList<Patient> actualPatientsForProvider = onlinePatientsForProvider;
        return actualPatientsForProvider;
    }

    // Get user id of clicked patient
    public String getClickedPatientUserId(ArrayList<Patient> patients, int position){
        return patients.get(position).getUserID();
    }
}
