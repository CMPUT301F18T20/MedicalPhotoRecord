package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;

import java.util.ArrayList;
import java.util.UUID;

public class OfflinePatientController {

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

    public void addPatient(Context context, Patient patient){

        // Get list of patients, add, save list of patients
        ArrayList<Patient> patients = new OfflineLoadController().loadPatientList(context);
        patients.add(patient);
        new OfflineSaveController().savePatientList(patients, context);
    }

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

    public void modifyPatient(Context context, Patient patient){

        // Delete old patient with same uuid, add patient object back
        deletePatient(context, patient);
        addPatient(context, patient);
    }
}
