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

    public static USER_TYPE checkPatientOrProviderExists(String UserID) throws NoSuchUserException,
            ExecutionException, InterruptedException {
        ArrayList<Patient> patients;
        ArrayList<Provider> providers;

        patients = new ElasticsearchPatientController.GetPatientTask().execute(UserID).get();
        providers = new ElasticsearchProviderController.GetProviderTask().execute(UserID).get();

        if (patients.size() >= 1) {
            return PATIENT;
        } else if (providers.size() >= 1) {
            return PROVIDER;
        }

        //TODO if password is wrong, throw BadPasswordException

        throw new NoSuchUserException();
    }

    public static USER_TYPE WhichActivityToStartAfterLogin(String UserID)
            throws ExecutionException, InterruptedException, NoSuchUserException {
        return checkPatientOrProviderExists(UserID);
    }

}
