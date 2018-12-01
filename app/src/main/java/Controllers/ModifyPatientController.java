package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.NoSuchUserException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyPatientController {

    private ArrayList<Patient> patients;
    private BrowseUserController browseUserController = new BrowseUserController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();


    public Patient getPatient(Context context, String userId) throws NoSuchUserException {

        // Online
        Patient onlinePatient = null;
        try {
            // Check if online patient exists
            ArrayList<Patient> onlinePatients = new ElasticsearchPatientController.GetPatientTask().execute(userId).get();
            if (onlinePatients.size() > 0){
                onlinePatient = onlinePatients.get(0);
            }else{
                throw new NoSuchUserException();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        Patient offlinePatient = new OfflinePatientController().getPatient(context, userId);
        if (offlinePatient == null){
            throw new NoSuchUserException();
        }

        // Sync issue
        Patient actualPatient = onlinePatient;
        return actualPatient;
    }

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
