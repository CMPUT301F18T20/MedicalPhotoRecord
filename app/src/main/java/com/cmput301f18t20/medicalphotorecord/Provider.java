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
/**
 * User class that represents the Provider.
 *
 * @author cmput301T20
 * @version 1.0
 * @see User
 * @see Patient
 * @see Record
 * @since 1.0
 */
public class Provider extends User {
    protected ArrayList<Patient> patients = new ArrayList<>();

    /** Constructor to build a bare minimum provider with only UserID
     * @param userId UserID of the provider
     * @throws UserIDMustBeAtLeastEightCharactersException If UserID is not at least 8 characters, exception is thrown
     * @see User
     */
    public Provider(String userId)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId);
    }

    /** Constructor to build a provider with all fields filled in
     * @param userId UserID of the provider
     * @param email Email of the provider
     * @param phoneNumber Phone Number of the provider
     * @throws UserIDMustBeAtLeastEightCharactersException If UserID is not at least 8 characters, exception is thrown
     * @see User
     */
    public Provider(String userId, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId, email, phoneNumber);
    }

    /** Get assigned Patient by their index in the array
     * @param PatientIndex index of the patient to return from the list of patients
     * @return requested patient
     * @throws NoSuchElementException If the patient does not exist
     */
    public Patient getPatient(int PatientIndex) {
        if (PatientIndex > this.patients.size()) {
            throw new NoSuchElementException("Patient not found");
        }
        return this.patients.get(PatientIndex);
    }

    /** Get assigned Patient by their userID
     * @param userID index of the patient to return from the list of patients
     * @return requested patient
     * @throws NoSuchElementException If the patient does not exist
     */
    public Patient getPatient(String userID) {
        for (Patient patient : this.patients) {
            if (patient.getUserID().equals(userID)) {
                return patient;
            }
        }
        throw new NoSuchElementException("Patient not found");
    }

    /** Get all assigned Patients
     * @return all assigned Patients
     */
    public ArrayList<Patient> getPatients() {
        return patients;
    }


    /** Assign a patient object to this Provider
     * @param patient Patient to assign to this Provider
     */
    public void assignPatient(Patient patient) {
        //TODO should we have a check here to make sure the same patient isn't added twice?
        patients.add(patient);
    }

    /** Unassign a patient object from this Provider by Provider
     * @param patient Patient to unassign from this Provider
     */
    public void unAssignPatient(Patient patient) {
        this.patients.remove(patient);
    }

    /** Unassign a patient object from this Provider by userID
     * @param userID Patient userID to unassign from this Provider
     */
    public void unAssignPatient(String userID) {
        this.unAssignPatient(this.getPatient(userID));
    }

    /** Unassign a patient object from this Provider by index
     * @param PatientIndex index of Patient to unassign from this Provider in the patients array
     */
    public void unAssignPatient(int PatientIndex) {
        this.patients.remove(PatientIndex);
    }
}
