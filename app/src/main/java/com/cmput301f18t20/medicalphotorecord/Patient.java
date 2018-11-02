package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class Patient extends User implements Refreshable {
    protected ArrayList<Problem> problems = new ArrayList<>();
    protected ArrayList<Provider> providers = new ArrayList<>();


    public Patient(String userId, String email, String phoneNumber) {
        super(userId, email, phoneNumber);
    }

    public Problem getProblem(int problemIndex) {
        return this.problems.get(problemIndex);
    }

    public void setProblem(Problem problem, int problemIndex) {
        this.problems.set(problemIndex, problem);
        //TODO: commit changes to disk/network
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public void addProblem(Problem problem) {
        problems.add(problem);
        //TODO: commit changes to disk/network
    }

    public void removeProblem(Problem problem) {
        problems.remove(problem);
        //TODO: commit changes to disk/network
    }

    public void removeProblem(int problemIndex) {
        problems.remove(problemIndex);
        //TODO: commit changes to disk/network
    }

    @Override
    public void refresh() {
        //TODO, will refresh list of problems and providers? separate functions for each?
    }

    public Provider getProvider(int ProviderIndex) {
        return this.providers.get(ProviderIndex);
    }

    public Provider getProvider(String userID) {
        return null; //TODO
    }

    public ArrayList<Provider> getProviders() {
        return providers;
    }

    public void fetchUpdatedProviderList() {
        //TODO
    }
}
