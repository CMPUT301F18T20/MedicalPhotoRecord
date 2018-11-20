package Controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.SignUp;
import Exceptions.NoConnectionInSignUpException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class SignUpController {
    public static void addProvider(String UserID, String Email, String Phone, Context context)
            throws UserIDMustBeAtLeastEightCharactersException, NoConnectionInSignUpException {

        //make sure we have an internet connection
        checkIfConnected(context);

        //create new provider
        Provider provider = new Provider(UserID, Email, Phone);

        // Offline saving for provider
        //load the most up to date results from the elasticsearch database
        //add new provider in and save it to disk
        //TODO Make sure you have a unique UserID
        //TODO use .onPostExecute() instead of .get()
        
            //get all the providers
            ArrayList<Provider> providers = new
                    OfflineLoadController().loadProviderList(context);
        
            //add new provider
            providers.add(provider);

            //commit to offline storage
            new OfflineSaveController().saveProviderList(providers, context);

       

        //add to elasticsearch
        //TODO Make sure you have a unique UserID
        new ElasticsearchProviderController.AddProviderTask().execute(provider);
    }

    public static void addPatient(String UserID, String Email, String Phone, Context context)
            throws UserIDMustBeAtLeastEightCharactersException, NoConnectionInSignUpException {

        //make sure we have an internet connection
        checkIfConnected(context);

        //create new patient
        Patient patient = new Patient(UserID, Email, Phone);

        // Offline saving for patient
        //load the most up to date results from the elasticsearch database
        //add new patient in and save it to disk
        //TODO Make sure you have a unique UserID
        //TODO use .onPostExecute() instead of .get()
       
            //get all the patients
            ArrayList<Patient> patients = new
                    OfflineLoadController().loadPatientList(context);

            //add new patient
            patients.add(patient);

            //commit to offline storage
            new OfflineSaveController().savePatientList(patients, context);

 

        //add to elasticsearch
        //TODO Make sure you have a unique UserID
        new ElasticsearchPatientController.AddPatientTask().execute(patient);
    }

    private static boolean checkIfConnected(Context context) throws NoConnectionInSignUpException  {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return false;
        }
        throw new NoConnectionInSignUpException();
    }
}
