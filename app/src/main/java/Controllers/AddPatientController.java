package Controllers;

import android.content.Context;
import android.widget.Toast;
import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddPatientController {
    Provider provider = null;
    Patient patient = null;

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
