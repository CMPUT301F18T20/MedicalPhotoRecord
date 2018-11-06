package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Provider extends User implements Refreshable {
    protected ArrayList<Patient> patients = new ArrayList<>();

    public Provider(String userId)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId);
    }

    public Provider(String userId, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId, email, phoneNumber);
    }

    public Patient getPatient(int PatientIndex) {
        if (PatientIndex > this.patients.size()) {
            throw new NoSuchElementException("Patient not found");
        }
        return this.patients.get(PatientIndex);
    }

    public Patient getPatient(String userID) {
        for (Patient patient : this.patients) {
            if (patient.getUserID().equals(userID)) {
                return patient;
            }
        }
        throw new NoSuchElementException("Patient not found");
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void assignPatient(Patient patient) {
        patients.add(patient);
    }

    public void assignPatient(String userID) {
        //Some sort of database lookup?
    }

    public void unAssignPatient(Patient patient) {
        this.patients.remove(patient);
        //TODO
    }

    public void unAssignPatient(String userID) {
        this.unAssignPatient(this.getPatient(userID));
    }

    public void unAssignPatient(int PatientIndex) {
        this.patients.remove(PatientIndex);
    }

    @Override
    public void refresh() {
        //TODO
    }
}
