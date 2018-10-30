package com.cmput301f18t20.medicalphotorecord;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class PatientTests {

    public void testGetProvider() {
        // Init patients and provider
        Patient patient = new Patient("0000", "patient_email@email.com", "1234567890");
        Provider provider = new Provider("1000", "provider_email@email.com", "1111111111");

        // If provider is not there, check for exception
        try {
            Provider providerGot = patient.getProvider("1000");
        } catch (NoSuchElementException e) {
            assertEquals("Provider not found", e.getMessage());
        }

        // If provider is there, check if they're the same providers
        provider.assignPatient(patient);
        Provider providerGot1 = patient.getProvider("1000");
        assertEquals("compare provider userId", "1000", providerGot1.getUserID());
        assertEquals("compare provider email", "provider_email@email.com", providerGot1.getEmail());
        assertEquals("compare provider phone number", "1111111111", providerGot1.getPhoneNumber());
    }

    public void testGetProviders() {

        // Init patients and provider
        Patient patient = new Patient("0000", "patient_email@email.com", "1234567890");
        ArrayList<Provider> providers = new ArrayList<Provider>();
        Provider provider = new Provider("1000", "provider_email@email.com", "1111111111");
        Provider provider1 = new Provider("2000", "provider2_email@email.com", "1111111111");

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

    public void testGetProblem(){

    }

}
