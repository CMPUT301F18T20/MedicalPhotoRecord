/*
 * Class name: Provider
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 08/11/18 8:03 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class Provider extends User {
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
        //TODO should we have a check here to make sure the same patient isn't added twice?
        patients.add(patient);
    }

    public void unAssignPatient(Patient patient) {
        this.patients.remove(patient);
    }

    public void unAssignPatient(String userID) {
        this.unAssignPatient(this.getPatient(userID));
    }

    public void unAssignPatient(int PatientIndex) {
        this.patients.remove(PatientIndex);
    }
}
