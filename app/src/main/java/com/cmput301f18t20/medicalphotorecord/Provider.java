package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class Provider extends User implements Refreshable {
    protected ArrayList<Patient> patients = new ArrayList<>();

    public Provider(String userId, String email, String phoneNumber) throws NonNumericUserIDException{
        super(userId, email, phoneNumber);
    }

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

    @Override
    public void refresh() {
        //TODO
    }
}
