package com.cmput301f18t20.medicalphotorecord;

public class Patient extends User {
    protected ProblemList problemList = new ProblemList();

    public ProblemList getProblemList() {
        return problemList;
    }
}
