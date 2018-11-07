package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class Patient extends User implements Refreshable {
    protected ArrayList<Problem> problems = new ArrayList<>();
    protected ArrayList<Provider> providers = new ArrayList<>();

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

        //TODO: commit changes to disk/network
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

        //TODO: commit changes to disk/network
    }

    public void removeProblem(Problem problem) {
        problems.remove(problem);
        //TODO: commit changes to disk/network
    }

    public void removeProblem(int problemIndex) {
        try {
            problems.remove(problemIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Problem not found");
        }
        //TODO: commit changes to disk/network
    }

    @Override
    public void refresh() {
        //TODO, will refresh list of problems and providers? separate functions for each?
    }

    public Provider getProvider(int ProviderIndex) {
        try {
            return this.providers.get(ProviderIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Provider not found");
        }
    }

    public Provider getProvider(String userID) {
        for (Provider provider : this.providers) {
            if (provider.getUserID().equals(userID)) {
                return provider;
            }
        }
        throw new NoSuchElementException("Provider not found");
    }

    public ArrayList<Provider> getProviders() {
        return providers;
    }
}
