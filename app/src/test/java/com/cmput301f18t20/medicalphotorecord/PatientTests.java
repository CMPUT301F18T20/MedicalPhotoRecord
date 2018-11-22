package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PatientTests {
    protected static String CorrectUserID1 = "abcdefgh";
    protected static String CorrectUserID2 = "stuvwxyz";

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetProblem()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Init patient and problem, add problem to patient's problem list
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        Problem problem = new Problem(patient.getUserID(),"problem");

        // If problem is not there, check for exception
        try {
            Problem problemGot = patient.getProblem(0);
            fail("Exception not produced");
        } catch (NoSuchElementException e) {
            assertEquals("Problem not found", e.getMessage());
        }

        // If problem is there, check and compare problem and problemGot
        patient.addProblem(problem);
        Problem problemGot1 = patient.getProblem(0);
        assertEquals("compare problem userId",problem.getCreatedByUserID(), problemGot1.getCreatedByUserID());
        assertEquals("compare problem title", problem.getTitle(), problemGot1.getTitle());
        assertEquals("compare problem date", problem.getDate(), problemGot1.getDate());

    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetProblems()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Init patient, problems, problem, problem1
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        ArrayList<Problem> problems = new ArrayList<Problem>();
        Problem problem = new Problem(patient.getUserID(),"problem");
        Problem problem1 = new Problem(patient.getUserID(),"problem1");

        // Check and add to list
        assertEquals("Patient's problem list of size 0",problems, patient.getProblems());

        patient.addProblem(problem);
        problems.add(problem);
        assertEquals("Patient's problem list of size 1", problems, patient.getProblems());
        patient.addProblem(problem1);
        problems.add(problem1);
        assertEquals("Patient's problem list of size 2", problems, patient.getProblems());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testAddProblem()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Init patient, problem
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        Problem problem = new Problem(patient.getUserID(),"problem");
        Problem problem1 = new Problem(CorrectUserID2,"problem1");

        // Add problem with same userId than patient's user id
        patient.addProblem(problem);
        Problem problemGot = patient.getProblem(0);
        assertEquals("compare problem userId",problem.getCreatedByUserID(), problemGot.getCreatedByUserID());
        assertEquals("compare problem title", problem.getTitle(), problemGot.getTitle());
        assertEquals("compare problem date", problem.getDate(), problemGot.getDate());

        // Add problem with different userId than patient's user id, exception thrown
        try{
            patient.addProblem(problem1);
            fail("Exception not produced");
        }catch (IllegalArgumentException e){
            assertEquals("Problem's createdByUserId does not match current patient's user id", e.getMessage());
        }

    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testRemoveProblem()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Init patient, problem
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        ArrayList<Problem> problems = new ArrayList<Problem>();
        Problem problem = new Problem(patient.getUserID(),"problem");
        Problem problem1 = new Problem(patient.getUserID(),"problem1");

        // Remove empty patient's list of problem (via index), exception thrown
        try{
            patient.removeProblem(0);
            fail("Exception not produced");
        }catch (NoSuchElementException e){
            assertEquals("Problem not found", e.getMessage());
        }

        // Remove empty patient's list of problem (via problem object itself), no exception thrown
        patient.removeProblem(problem);

        // Remove from non empty patient's list of problem (via problem object and via index)
        patient.addProblem(problem);
        patient.addProblem(problem1);
        problems.add(problem1);

        patient.removeProblem(0);
        assertEquals("Removing from patient's list via index", problems, patient.getProblems());

        problems.remove(problem1);
        patient.removeProblem(problem1);
        assertEquals("Removing from patient's list via problem object", problems, patient.getProblems());

    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testSetProblem()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Init patient, problem
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        Problem problem = new Problem(patient.getUserID(),"problem");
        Problem problem2 = new Problem(patient.getUserID(),"problem");
        Problem badProblem2 = new Problem(patient.getUserID() + "diff","problem");

        //add and verify
        patient.addProblem(problem);
        Problem problemGot = patient.getProblem(0);
        assertEquals("problems are not equal after add", problem, problemGot);

        // Set index 0 to problem 2 by index
        patient.setProblem(problem2,0);
        problemGot = patient.getProblem(0);
        assertEquals("problems are not equal after set", problem2, problemGot);

        //can't set a problem with wrong user id
        try {
            patient.setProblem(badProblem2, 0);
            fail("Exception not produced");
        }catch (IllegalArgumentException e){
            assertEquals("Problem's createdByUserId does not match current patient's user id", e.getMessage());
        }
    }
}
