package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;

import java.util.ArrayList;
import java.util.UUID;

/**
 * OfflinePatientController
 * Can get patient object from userId Offline
 * Can add patient object to database Offline
 * Can delete patient object to database Offline
 * Can modify patient object to database Offline
 * @version 2.0
 * @see Patient
 */
public class OfflinePatientController {

    /**
     * Get patient object from offline database using userId
     * @param context: activity to be passed for offline save and load
     * @param userID
     * @return patient object if found, null if not found
     */
    public Patient getPatient(Context context, String userID){

        // Compare uuid to every patient's uuid to get patient
        ArrayList<Patient> patients = new OfflineLoadController().loadPatientList(context);
        for (Patient p : patients) {
            if (userID.equals(p.getUserID())) {
                return p;
            }
        }
        // If not found
        return null;
    }

    /**
     * Add patient object to offine database
     * @param context: activity to be passed for offline save and load
     * @param patient
     */
    public void addPatient(Context context, Patient patient){

        // Get list of patients, add, save list of patients
        ArrayList<Patient> patients = new OfflineLoadController().loadPatientList(context);
        patients.add(patient);
        new OfflineSaveController().savePatientList(patients, context);
    }

    /**
     * Delete patient object from offline database
     * @param context: activity to be passed for offline save and load
     * @param patient
     */
    public void deletePatient(Context context, Patient patient){

        // Get list of patients, delete, save list of patients
        ArrayList<Patient> patients = new OfflineLoadController().loadPatientList(context);
        for (Patient p : new ArrayList<>(patients)){
            if (p.getUserID().equals(patient.getUserID())){
                patients.remove(p);
            }
        }
        new OfflineSaveController().savePatientList(patients, context);
    }

    /**
     * Modify patient object from offline database (delete old patient object using userId, then add patient object back)
     * @param context: activity to be passed for offline save and load
     * @param patient
     */
    public void modifyPatient(Context context, Patient patient){

        // Delete old patient with same uuid, add patient object back
        deletePatient(context, patient);
        addPatient(context, patient);
    }
}
