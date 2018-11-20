package Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

public class AddPatientController {

    public void addPatient(Context context, String providerID ,String patientID){
        ModifyProviderController modifyProviderController = new ModifyProviderController();
        Provider provider = modifyProviderController.getProvider(context, providerID);

        ModifyUserController modifyUserController = new ModifyUserController();

        if (modifyUserController.getPatient(context, patientID) == null){
            Toast.makeText(context, "THE USER DOES NOT EXIST", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("addpatt2", modifyUserController.getPatient(context,patientID).toString());
            provider.assignPatient(modifyUserController.getPatient(context,patientID));
        }

    }
}
