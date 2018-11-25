package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyPatientController {

    private ArrayList<Patient> patients;
    private BrowseUserController browseUserController = new BrowseUserController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();


    public Patient getPatient(Context context, String userId) {

        // Online
        Patient onlinePatient = null;
        try {
            this.patients = new ElasticsearchPatientController.GetPatientTask().execute(userId).get();
            if (this.patients.size() == 0){
                return onlinePatient;
            }else {
                onlinePatient = (new ElasticsearchPatientController.GetPatientTask().execute(userId).get()).get(0);
                return onlinePatient;
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
