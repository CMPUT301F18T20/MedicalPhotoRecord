package com.cmput301f18t20.medicalphotorecord;

import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class PatientTests {
    protected static String CorrectUserID1 = "abcdefgh";
    protected static String CorrectUserID2 = "stuvwxyz";
    protected static String CorrectUserID3 = "acebgfIII";

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetProvider() throws UserIDMustBeAtLeastEightCharactersException {
        
        // Init patients and provider
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        Provider provider = new Provider(CorrectUserID2, "provider_email@email.com", "1111111111");

        // If provider is not there, check for exception, get by user id
        try {
            Provider providerGot = patient.getProvider(CorrectUserID2);
        } catch (NoSuchElementException e) {
            assertEquals("Provider not found", e.getMessage());
        }

        // If provider is not there, check for exception, get by index
        try {
            Provider providerGot = patient.getProvider(0);
        } catch (NoSuchElementException e) {
            assertEquals("Provider not found", e.getMessage());
        }

        // If provider is there, check if they're the same providers
        provider.assignPatient(patient);
        Provider providerGot1 = patient.getProvider(CorrectUserID2);
        Provider providerGot2 = patient.getProvider(0);
        assertEquals("compare provider userId", CorrectUserID2, providerGot1.getUserID());
        assertEquals("compare provider email", "provider_email@email.com", providerGot1.getEmail());
        assertEquals("compare provider phone number", "1111111111", providerGot1.getPhoneNumber());
        assertEquals("compare the two provider got by userId and by index", providerGot1, providerGot2);
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetProviders() throws UserIDMustBeAtLeastEightCharactersException {

        // Init patients and provider
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        ArrayList<Provider> providers = new ArrayList<Provider>();
        Provider provider = new Provider(CorrectUserID2, "provider_email@email.com", "1111111111");
        Provider provider1 = new Provider(CorrectUserID3, "provider2_email@email.com", "1111111111");

        // Check for empty list of provider
        assertEquals("Provider list of size 0", providers, patient.getProviders());

        // Check for non empty list of provider (from assigning patient to provider since patient cannot add provider themselves)
        provider.assignPatient(patient);
        providers.add(provider);
        assertEquals("Provider list of size 1", providers, patient.getProviders());
        provider1.assignPatient(patient);
        providers.add(provider1);
        assertEquals("Provider list of size 2", providers, patient.getProviders());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testGetProblem()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {

        // Init patient and problem, add problem to patient's problem list
        Patient patient = new Patient(CorrectUserID1, "patient_email@email.com", "1234567890");
        Problem problem = new Problem(patient.getUserID(),"problem");

        // If problem is not there, check for exception
        try {
            Problem problemGot = patient.getProblem(0);
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
        Problem problem1 = new Problem(CorrectUserID2,"problem1");

        // Remove empty patient's list of problem (via index), exception thrown
        try{
            patient.removeProblem(0);
        }catch (NoSuchElementException e){
            assertEquals("Removing from empty patient's problem list via index", e.getMessage());
        }

        // Remove empty patient's list of problem (via problem object itself), exception thrown
        try{
            patient.removeProblem(problem);
        }catch (NoSuchElementException e){
            assertEquals("Removing from empty patient's problem list via problem object", e.getMessage());
        }

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

        // Set normal problem by index
        patient.setProblem(problem,1);
        Problem problemGot = patient.getProblem(1);
        assertEquals("compare problem userId",problem.getCreatedByUserID(), problemGot.getCreatedByUserID());
        assertEquals("compare problem title", problem.getTitle(), problemGot.getTitle());
        assertEquals("compare problem date", problem.getDate(), problemGot.getDate());

    }

}
