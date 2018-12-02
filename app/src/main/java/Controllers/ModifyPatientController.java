package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.NoSuchUserException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

/**
 * ModifyPatientController
 * Can get patient object from userID
 * Can save modified patient object to online and offline database
 * @version 2.0
 * @see Patient
 */
public class ModifyPatientController {

    private ArrayList<Patient> patients;
    private BrowseUserController browseUserController = new BrowseUserController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();


    /**
     * Get patient object from appropriate database (online when there's wifi, offline when there's no wifi)
     * @param context: activity to be passed for offline save and load
     * @param userId
     * @return actualPatient object correspond to userID
     * @throws NoSuchUserException: if patient is not found in databases
     */
    public Patient getPatient(Context context, String userId) throws NoSuchUserException {

        // Online
        Patient onlinePatient = null;
        try {
            // Check if online patient exists
            ArrayList<Patient> onlinePatients = new ElasticsearchPatientController.GetPatientTask().execute(userId).get();
            if (onlinePatients.size() > 0){
                onlinePatient = onlinePatients.get(0);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        Patient offlinePatient = new OfflinePatientController().getPatient(context, userId);

        // Sync issue
        Patient actualPatient = onlinePatient;
        if (actualPatient == null){
            throw new NoSuchUserException();
        }
        return actualPatient;
    }

    /**
     * Takes in old patient, new modified email, new modified phone number
     * Sets new information to patient object
     * Save patient object to both online and offline database
     * @param context: activity to be passed for offline save and load
     * @param patient
     * @param email
     * @param phoneNumber
     */
    public void saveModifyPatient(Context context, Patient patient, String email, String phoneNumber) {

        // Modify
        patient.setEmail(email);
        patient.setPhoneNumber(phoneNumber);

        // Online
        try {
            new ElasticsearchPatientController.SaveModifiedPatient().execute(patient).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        new OfflinePatientController().modifyPatient(context, patient);
    }
}
