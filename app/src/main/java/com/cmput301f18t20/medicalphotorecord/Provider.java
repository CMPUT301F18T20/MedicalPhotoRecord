package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class Provider {
    protected ArrayList<Patient> patients;

    public Patient getPatient(int PatientIndex) {
        return this.patients.get(PatientIndex);
    }

    public Patient getPatient(String userID) {
        return null; //TODO
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void assignPatient(Patient patient) {
        patients.add(patient);
    }

    public void assignPatient(String userID) {
        //TODO
    }

    public void unAssignPatient(Patient patient) {
        this.patients.remove(patient);
        //TODO
    }

    public void unAssignPatient(String userID) {
        //TODO
    }

    public void unAssignPatient(int PatientIndex) {
        this.patients.remove(PatientIndex);
    }

    public void fetchUpdatedPatientList() {
        //TODO
    }
}
