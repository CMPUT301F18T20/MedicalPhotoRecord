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
        return this.patients.get(PatientIndex);
    }

    public Patient getPatient(String userID) throws NoSuchElementException {

        for (Patient patient:patients){
            if (patient.getUserID() == userID){
                return patient;
            }
        }
        throw new NoSuchElementException();
    }

    public ArrayList<Patient> getPatients() {
        return this.patients;
    }

    public void assignPatient(Patient patient) {
        patient.addProvider(this);
        this.patients.add(patient);
    }


    public void unAssignPatient(Patient patient) {
        patient.removeProvider(this);
        this.patients.remove(patient);
        //TODO
    }

    public void unAssignPatient(String userID) {
        for (Patient patient:this.patients){
            if (patient.getUserID() == userID){
                this.patients.remove(patient);
            }
        }
    }

    public void unAssignPatient(int PatientIndex) {
        this.patients.remove(PatientIndex);
    }

    @Override
    public void refresh() {
        //TODO
    }
}
