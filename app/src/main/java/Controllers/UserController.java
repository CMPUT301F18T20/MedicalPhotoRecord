/*
 * Class name: UserController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 7:31 PM
 *
 * Last Modified: 11/15/18 7:31 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;
import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;

public class UserController {

    public void addPatient(Context context, Patient patient){

        // Add patient to database offline
        ArrayList<Patient> patients = new ArrayList<>();
        patients = new OfflineLoadController().loadPatientList(context);
        patients.add(patient);
        new OfflineSaveController().savePatientList(patients,context);

        // Add patient to database online
        new ElasticsearchPatientController.AddPatientTask().execute(patient);

        // Handle syncing problem later
    }

    public void addProvider(Context context, Provider provider){

        // Add provider to database offline
        ArrayList<Provider> providers = new ArrayList<>();
        providers = new OfflineLoadController().loadProviderList(context);
        providers.add(provider);
        new OfflineSaveController().saveProviderList(providers,context);

        // Add provider to database online
        new ElasticsearchProviderController.AddProviderTask().execute(provider);

        // Handle syncing problem later
    }
}
