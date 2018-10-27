package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class Provider {
    protected ArrayList<Patient> Patients;

    public void assignPatient(Patient patient) {
        Patients.add(patient);
    }

    public void removePatient(Patient patient) {
        Patients.remove(patient);
    }

}
