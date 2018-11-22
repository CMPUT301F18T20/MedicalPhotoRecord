package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;

public class BrowseProviderPatientsController {

    public ArrayList<Patient> getPatientList(Context context, String userID){

        // Get provider
        Provider provider = new ModifyProviderController().getProvider(context, userID);
        // Get providers's patient list
        return provider.getPatients();
    }
}
