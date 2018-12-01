package Controllers;

import android.content.Context;
import android.widget.Toast;
import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * AddPatientController
 * Adds the given patient into the given provider's list of patients
 * only if the given patientID is valid or not yet added to the list
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

import Exceptions.NoSuchUserException;

public class AddPatientController {
    Provider provider = null;
    Patient patient = null;

    /**
     * addPatient
     * This method gets the given provider and patient
     * it verifies if the given patient is valid or not yet added to the provider's list of patients
     * then adds the patient to the list.
     * @param context
     * @param providerID
     * @param patientID
     */

    public void addPatient(Context context, String providerID ,String patientID){
        try {
            ArrayList<Patient> patients = new ElasticsearchPatientController.GetPatientTask().execute(patientID).get();
            if (patients.size() != 0){
                patient = patients.get(0);
            }
        }catch (ExecutionException e){
            Toast.makeText(context, "THE PATIENT DOES NOT EXIST", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch (InterruptedException e){
            Toast.makeText(context, "THE PATIENT DOES NOT EXIST", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

       if (this.patient == null ) {
            Toast.makeText(context, "THE PATIENT DOES NOT EXIST", Toast.LENGTH_SHORT).show();
        } else {

            boolean verify = checkIfPatientAlreadyInList(providerID,this.patient);
            if (verify){
                patient.addAssociatedProviderID(providerID);
                try {
                    new ElasticsearchPatientController.SaveModifiedPatient().execute(patient).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "THE PATIENT SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "PATIENT ALREADY ADDED", Toast.LENGTH_SHORT).show();
            }
        }
}

    /**
     * checkIfPatientAlreadyInList
     * given a valid patient object, this method checks the
     * provider's list of patients and returns false
     * if a patient is already in the list.
     * true is returned if the patient does not exist in the list yet.
     *
     * @param patient - patient object
     * @return
     */

    public boolean checkIfPatientAlreadyInList(String providerID, Patient patient){
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            patients = new ElasticsearchPatientController.GetPatientsAssociatedWithProviderUserIDTask().execute(providerID).get();
        }catch (ExecutionException e){

        }catch (InterruptedException e){

        }
        for (Patient p: patients) {
            if (patient.getUserID().equals(p.getUserID())){
                return false;
            }
        }

        return true;
    }
}
