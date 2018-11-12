package Controllers;

import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Enums.USER_TYPE;
import Exceptions.NoSuchUserException;

import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;

public class LoginController {

    //TODO actual query!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //in place of a proper query for now
    private static USER_TYPE DecidePatientProviderOrNone(
            ArrayList<Patient> patients, ArrayList<Provider> providers, String userID)
            throws NoSuchUserException {

        //in place of a proper query for now
        for (Patient patient: patients) {
            Log.d("LoginController", patient.getUserID() + " is not the same as " + userID);
            if (patient.getUserID().equals(userID)) {
                return PATIENT;
            }
        }
        for (Provider provider: providers) {
            if (provider.getUserID().equals(userID)) {
                return PROVIDER;
            }
        }

        //TODO if password is wrong, throw BadPasswordException

        throw new NoSuchUserException();
    }

    public static USER_TYPE WhichActivityToStartAfterLogin(String UserID)
            throws ExecutionException, InterruptedException, NoSuchUserException {
        ArrayList<Patient> patients;
        ArrayList<Provider> providers;

        patients = new ElasticsearchPatientController.GetPatientTask().execute().get();
        providers = new ElasticsearchProviderController.GetProviderTask().execute().get();

        return DecidePatientProviderOrNone(patients, providers, UserID);
    }

}
