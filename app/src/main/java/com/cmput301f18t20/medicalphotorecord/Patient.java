/*
 * Class name: Patient
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
 * Patient class, Patients have a list of Problems they can view/modify.
 *
 * @version 1.0
 * @see User
 * @see Provider
 * @see Problem
 * @see PatientRecord
 * @since 1.0
 */

public class Patient extends User  {
    protected ArrayList<Problem> problems = new ArrayList<>();
    protected ArrayList<String> associatedProviderIDs = new ArrayList<>();

    /** Constructor to build a bare minimum Patient with only UserID
     * @param userId UserID of the provider
     * @throws UserIDMustBeAtLeastEightCharactersException If UserID is not at least 8 characters, exception is thrown
     * @see User
     */
    public Patient(String userId) throws UserIDMustBeAtLeastEightCharactersException {
        super(userId);
    }
    
    /** Constructor to build a Patient with all fields filled in
     * @param userId UserID of the Patient
     * @param email Email of the Patient
     * @param phoneNumber Phone Number of the Patient
     * @throws UserIDMustBeAtLeastEightCharactersException If UserID is not at least 8 characters, exception is thrown
     * @see User
     */
    public Patient(String userId, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId, email, phoneNumber);
    }

    /** Get assigned Problem by their index in the array
     * @param problemIndex index of the Problem to return from the list of Problems
     * @return requested Problem
     * @throws NoSuchElementException If the Problem does not exist
     */
    public Problem getProblem(int problemIndex) throws NoSuchElementException {
        try {
            return this.problems.get(problemIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Problem not found");
        }
    }


    /** Set a particular index in the Problems array to a provided Problem
     * @param problem problem to set the problemIndex's value to
     * @param problemIndex index in the Problems array to be changed
     * @throws IllegalArgumentException Problem's createdByUserId does not match current patient's user id
     */
    public void setProblem(Problem problem, int problemIndex) throws IllegalArgumentException {
        if (problem.getCreatedByUserID().equals(this.UserID)) {
            this.problems.set(problemIndex, problem);
        } else {
            throw new IllegalArgumentException(
                    "Problem's createdByUserId does not match current patient's user id");
        }
    }
    /** Get all assigned Problems
     * @return all assigned Problems
     */
    public ArrayList<Problem> getProblems() {
        return problems;
    }
    
    /** Add a Problem object to this Patient
     * @param problem Problem to add to this Patient
     * @throws IllegalArgumentException if the Problem's createdByUserId does not match current Patient's user id
     */
    public void addProblem(Problem problem) throws IllegalArgumentException {
        if (problem.getCreatedByUserID().equals(this.UserID)) {
            problems.add(problem);
        } else {
            throw new IllegalArgumentException(
                    "Problem's createdByUserId does not match current patient's user id");
        }
    }
    
    /** Remove a Problem object from this Patient by Problem
     * @param problem Problem to remove from this Patient
     */
    public void removeProblem(Problem problem) {
        problems.remove(problem);
    }

    /** Remove a Problem object from this Patient by index in Problems array
     * @param problemIndex index of Problem to remove from this Patient
     * @throws NoSuchElementException Problem not fount.
     */
    public void removeProblem(int problemIndex) throws NoSuchElementException {
        try {
            problems.remove(problemIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Problem not found");
        }
    }

    //TODO testing
    public void addAssociatedProviderID(String UserID) {
        this.associatedProviderIDs.add(UserID);
    }

    public String toString(){
        return this.UserID + " " + this.email + " " + this.phoneNumber;
    }
}
