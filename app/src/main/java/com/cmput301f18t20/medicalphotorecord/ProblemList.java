package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;

public class ProblemList {
    protected ArrayList<Problem> Problems = new ArrayList<>();

    public void setProblems(ArrayList<Problem> problems) {
        Problems = problems;
        //TODO: commit changes to disk/network
    }

    public ArrayList<Problem> getProblems() {
        return Problems;
    }

    public void addProblem(Problem problem) {
        Problems.add(problem);
        //TODO: commit changes to disk/network

    }

    public void deleteProblem(Problem problem) {
        // TODO: this will need to be a lot smarter, likely have to do with tasks like
        // TODO: "delete all problems with a certain string in their title"
        Problems.remove(problem);
        //TODO: commit changes to disk/network
    }

    public void clearProblems() {
        Problems.clear();
        //TODO: commit changes to disk/network
    }
}
