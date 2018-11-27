package Controllers;

import android.content.Context;
import android.widget.Toast;
import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;

/**
 * AddPatientController
 * Adds the given patient into the given provider's list of patients
 * only if the given patientID is valid or not yet added to the list
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 */

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
        provider = new ModifyProviderController().getProvider(context, providerID);
        patient = new ModifyPatientController().getPatient(context, patientID);


        if (this.patient == null ) {
            Toast.makeText(context, "THE PATIENT DOES NOT EXIST", Toast.LENGTH_SHORT).show();
        } else {

            boolean verify = checkIfPatientAlreadyInList(this.patient);
            if (verify){
            provider.assignPatient(this.patient);
            Toast.makeText(context, "THE PATIENT SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "PATIENT ALREADY ADDED", Toast.LENGTH_SHORT).show();
            }
        }

        new ModifyProviderController().saveModifiedProvider(context, provider, provider.getEmail(),provider.getPhoneNumber());
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

    public boolean checkIfPatientAlreadyInList(Patient patient){
        ArrayList<Patient> patients = provider.getPatients();

        for (Patient p: patients) {
            if (patient.getUserID().equals(p.getUserID())){
                return false;
            }
        }

        return true;
    }
}
