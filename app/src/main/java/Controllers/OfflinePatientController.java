package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;

import java.util.ArrayList;

public class OfflinePatientController {

    public Patient getPatient(Context context, String uuid){
        ArrayList<Patient> patients = new OfflineLoadController().loadPatientList(context);

        // Need to get uuid instead
        for (Patient user : patients) {
            if (uuid.equals(user.getUserID())) {
                return user;
            }
        }
    }
}
