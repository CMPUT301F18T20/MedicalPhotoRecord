package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class Provider extends User {
    protected ArrayList<Patient> Patients;

    public void assignPatient(Patient patient) {
        Patients.add(patient);
    }

    public void removePatient(Patient patient) {
        Patients.remove(patient);
    }

    //TODO: UNNEEDED
    public ProblemList getPatientProblems(Patient patient) {
        return patient.getProblemList();
    }
    //TODO: UNNEEDED
    public void getPatientInfo(Patient patient) {}

    /* fetch patients from database or local */
    public void updatePatientList() {}


}
