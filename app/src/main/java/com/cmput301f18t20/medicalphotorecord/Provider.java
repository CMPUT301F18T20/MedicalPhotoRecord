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

public class Provider extends User implements Refreshable {
    protected ArrayList<Patient> patients = new ArrayList<>();

    public Provider(String userId)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId);
        this.refresh();
    }

    public Provider(String userId, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId, email, phoneNumber);
        this.refresh();
    }

    public Patient getPatient(int PatientIndex) {
        this.refresh();
        if (PatientIndex > this.patients.size()) {
            throw new NoSuchElementException("Patient not found");
        }
        return this.patients.get(PatientIndex);
    }

    public Patient getPatient(String userID) {
        this.refresh();
        for (Patient patient : this.patients) {
            if (patient.getUserID().equals(userID)) {
                return patient;
            }
        }
        throw new NoSuchElementException("Patient not found");
    }

    public ArrayList<Patient> getPatients() {
        this.refresh();
        return patients;
    }

    public void assignPatient(Patient patient) {
        //TODO should we have a check here to make sure the same patient isn't added twice?
        //FIXME should refresh be called before or after? Does it fetch from or write to the database?
        patients.add(patient);
    }

    public void assignPatient(String userID) {
        //Some sort of database lookup?
    }

    public void unAssignPatient(Patient patient) {
        patient.removeProvider(this);
        this.patients.remove(patient);
        //TODO         this.refresh() before or after??;
    }

    public void unAssignPatient(String userID) {
        this.unAssignPatient(this.getPatient(userID));
    }

    public void unAssignPatient(int PatientIndex) {
        //lock database
        //pull database changes
        this.patients.remove(PatientIndex);
        //write to database
        //TODO         this.refresh() before or after??;

    }

    @Override
    public void refresh() {
        //TODO
    }
}
