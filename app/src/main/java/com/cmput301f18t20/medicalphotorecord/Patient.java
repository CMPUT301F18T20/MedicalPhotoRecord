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
 * Patient class, Patients have a list of problems they can view/modify
 * and they have providers that are assigned to themselves.
 *
 * @author mwhackma
 * @version 1.0
 * @see Problem
 * @see Provider
 * @since 1.0
 */

public class Patient extends User  {
    protected ArrayList<Problem> problems = new ArrayList<>();
    protected ArrayList<String> associatedProviderIDs = new ArrayList<>();

    public Patient(String userId) throws UserIDMustBeAtLeastEightCharactersException {
        super(userId);
    }

    public Patient(String userId, String email, String phoneNumber)
            throws UserIDMustBeAtLeastEightCharactersException {
        super(userId, email, phoneNumber);
    }

    public Problem getProblem(int problemIndex) {
        try {
            return this.problems.get(problemIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Problem not found");
        }
    }

    public void setProblem(Problem problem, int problemIndex) {
        if (problem.getCreatedByUserID().equals(this.UserID)) {
            this.problems.set(problemIndex, problem);
        } else {
            throw new IllegalArgumentException(
                    "Problem's createdByUserId does not match current patient's user id");
        }
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public void addProblem(Problem problem) {
        if (problem.getCreatedByUserID().equals(this.UserID)) {
            problems.add(problem);
        } else {
            throw new IllegalArgumentException(
                    "Problem's createdByUserId does not match current patient's user id");
        }
    }

    public void removeProblem(Problem problem) {
        problems.remove(problem);
    }

    public void removeProblem(int problemIndex) {
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
