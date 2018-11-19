package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.User;

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
        try {
            ArrayList<Patient> onlinePatients = new ElasticsearchPatientController.GetPatientTask().execute().get();
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
        this.patients = offlinePatients;
        return this.patients;
    }

    // Get all providers
    public ArrayList<Provider> getProviderList(Context context){

        // Offline
        ArrayList<Provider> offlineProviders = offlineLoadController.loadProviderList(context);

        // Online
        try {
            ArrayList<Provider> onlineProviders = new ElasticsearchProviderController.GetProviderTask().execute().get();
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

        return offlineProviders;
    }

    // Get all patients of certain provider
    public ArrayList<Patient> getPatientListOfProvider(Context context, String providerId){

        // Find the provider from list of providers
        this.providers = getProviderList(context);

        for (Provider pr: new ArrayList<>(this.providers)){
            if (providerId.equals(pr.getUserID())){
                this.provider = pr;
            }
        }

        // Online
        //this.provider = (new ElasticsearchProviderController.GetProviderTask().execute(providerId).get()).get(0);

        // Get provider's list of patients
        return this.provider.getPatients();
    }

    // Get user id of clicked patient
    public String getClickedPatientUserId(ArrayList<Patient> patients, int position){
        return patients.get(position).getUserID();
    }
}
