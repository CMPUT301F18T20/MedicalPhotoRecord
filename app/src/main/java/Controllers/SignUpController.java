package Controllers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.CheckSecurityToken;
import Activities.Login;
import Enums.USER_TYPE;
import Exceptions.NoConnectionInSignUpException;
import Exceptions.NoSuchUserException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static Controllers.SecurityTokenController.addSecurityToken;
import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;

/**
 * Sign Up Controller
 *  Allows a user to create their own profile.  Saves the user's file and a 
 *  security token, then logs the user in.
 *
 * @author  Members of T20
 * @see Login
 * @see CheckSecurityToken
 * @see SecurityToken
 * @see User
 */

public class SignUpController {

    /** adds a Provider to the system.  Also generates a security token for this new Provider and
     * saves a copy to disk
     * @param UserID UserID of new Provider
     * @param Email Email of new Provider
     * @param Phone Phone number of new Provider
     * @param context
     * @throws UserIDMustBeAtLeastEightCharactersException UserID must be >= 8 chars
     * @throws NoConnectionInSignUpException If no connection, cannot ensure unique UserIDs
     * @throws InterruptedException 
     * @throws ExecutionException
     * @throws UserAlreadyExistsException UserID is already in use
     */
    public static void addProvider(String UserID, String Email, String Phone, Context context)
            throws UserIDMustBeAtLeastEightCharactersException, NoConnectionInSignUpException,
            InterruptedException, ExecutionException, UserAlreadyExistsException {

        //make sure we have an internet connection
        checkIfConnected(context);

        //create new provider
        Provider provider = new Provider(UserID, Email, Phone);

        try {
            checkPatientOrProviderExists(UserID);
            throw new UserAlreadyExistsException();

        } catch(NoSuchUserException nsue) {
            // this means that the user did not exist when we queried, so we know we can add them now

            // Offline saving for provider
            //load the most up to date results from the elasticsearch database
            //add new provider in and save it to disk

            //TODO Do we need to fetch all users here or should we do it in the sync controllers?
            //get all the providers
            ArrayList<Provider> providers = new
                    ElasticsearchProviderController.GetProviderTask().execute().get();

            //add new provider
            providers.add(provider);

            //commit to offline storage
            new OfflineSaveController().saveProviderList(providers, context);

            //add to elasticsearch
            new ElasticsearchProviderController.AddProviderTask().execute(provider);

            //add the security token
            addSecurityToken(provider.getUserID(), PROVIDER, context);
        }
    }

    /** adds a Patient to the system.  Also generates a security token for this new Patient and
     * saves a copy to disk
     * @param UserID UserID of new Patient
     * @param Email Email of new Patient
     * @param Phone Phone number of new Patient
     * @param context
     * @throws UserIDMustBeAtLeastEightCharactersException UserID must be >= 8 chars
     * @throws NoConnectionInSignUpException If no connection, cannot ensure unique UserIDs
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws UserAlreadyExistsException UserID is already in use
     */
    public static void addPatient(String UserID, String Email, String Phone, Context context)
            throws UserIDMustBeAtLeastEightCharactersException, NoConnectionInSignUpException,
            InterruptedException, ExecutionException, UserAlreadyExistsException {

        //make sure we have an internet connection
        checkIfConnected(context);

        //create new patient
        Patient patient = new Patient(UserID, Email, Phone);

        try {
            checkPatientOrProviderExists(UserID);
            throw new UserAlreadyExistsException();


        } catch(NoSuchUserException nsue) {
            // this means that the user did not exist when we queried, so we know we can add them now

            // Offline saving for patient
            //load the most up to date results from the elasticsearch database
            //add new patient in and save it to disk

            //TODO Do we need to fetch all users here or should we do it in the sync controllers?
            //get all the patients
            ArrayList<Patient> patients = new
                    ElasticsearchPatientController.GetPatientTask().execute().get();

            //add new patient
            patients.add(patient);

            //commit to offline storage
            new OfflineSaveController().savePatientList(patients, context);

            //add to elasticsearch
            new ElasticsearchPatientController.AddPatientTask().execute(patient);

            //add the security token
            addSecurityToken(patient.getUserID(), PATIENT, context);
        }
    }

    /** checks if the phone has wifi
     * @param context
     * @return false if not connected, else generates NoConnectionInSignUpException
     * @throws NoConnectionInSignUpException
     */
    private static boolean checkIfConnected(Context context) throws NoConnectionInSignUpException  {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return false;
        }
        throw new NoConnectionInSignUpException();
    }


    /** checks if a user using the provided userID already exists
     * @param UserID UserID to use for the check
     * @return type of user the UserID is (provider or patient)
     * @throws NoSuchUserException generated if no UserID matches the one provided
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static USER_TYPE checkPatientOrProviderExists(String UserID) throws NoSuchUserException,
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

        throw new NoSuchUserException();
    }
}
