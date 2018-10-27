package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class Patient extends User {
    protected ArrayList<Problem> problems = new ArrayList<>();
    protected ArrayList<Provider> providers = new ArrayList<>();

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

    public void fetchUpdatedProblemList() {
        //TODO
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