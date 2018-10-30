package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PatientRecordUnitTests {

    /* Tests that we can set and get the Location value of a patient record */
    @Test
    public void CanGetAndSetLocation() {
        /* limit for longitude is +- 180, latitude is +-90. TODO: setter should throw error on violating those */
        int offset = 15;
        PatientRecord record = new PatientRecord();
        Location newLocation = new Location(LocationManager.GPS_PROVIDER);
        newLocation.setLatitude(0);
        newLocation.setLongitude(0);

        /* todo: expect to catch error when coordinates are out of range */
        for (int i = -195; i < 195; i+=5) {
            newLocation.setLatitude(i);
            newLocation.setLongitude(i + offset);
            record.setGeolocation(newLocation);

            assertEquals("geolocation was not set properly.",
                    newLocation, record.geolocation);
            assertEquals("geolocation was not fetched properly.",
                    newLocation, record.getGeolocation());
        }
    }

    /* Sanity test for constructor with string input */
    @Test
    public void CanSetCreatedUserIDFromConstructor() {
        try {
            /* list of UserIDs to test against */
            for (String TestUserID : Arrays.asList("18004192", "29811001", "99999999999999")) {

                PatientRecord patientRecord = new PatientRecord(TestUserID);
            }

        } catch (NonNumericUserIDException e) {
            fail("NonNumericUserIDException should not have been generated");
        }
    }

    /* generates NonNumericUserIDException on non numeric input for UserID */
    @Test
    public void NonNumericUserIDExceptionGeneration () {
        for (String TestUserID : Arrays.asList("word not number", "wordAndNumber22 34", "")) {
            try {
                PatientRecord patientRecord = new PatientRecord(TestUserID);
                fail("NonNumericUserIDException should have been generated for input " + TestUserID);

            } catch (NonNumericUserIDException e) {
                //Do nothing as correct functionality generates this exception
            }
        }
    }

    /* Creates 4 photos and adds them to two patient records in a specific order.  Adds those
     * patient records to a problem so we can verify the getAllPhotos() method from problem
     * as well.  Removes a photo by object and by index and tests both times that the
     * problem sees the changes with getAllPhotos().  Verifies that the record set the changes
     * correctly by verifying final state of both objects to have the photo array we expected
     */
    @Test
    public void removePhotos() {
        Problem problem = new Problem();
        ArrayList<Photo> testPhotos = new ArrayList<>();

        // create new photos
        Photo Photo1 = new Photo();
        Photo Photo2 = new Photo();
        Photo Photo3 = new Photo();
        Photo Photo4 = new Photo();

        //add the records to a problem. Going to also be testing
        //the problem's getAllPhotos() method here because this is a
        //common use case
        problem.addRecord(new PatientRecord());
        problem.addRecord(new PatientRecord());

        // create new patient records to add the photos to
        PatientRecord patientRecord1 = (PatientRecord) problem.getRecord(0);
        PatientRecord patientRecord2 = (PatientRecord) problem.getRecord(1);

        //photo3 should appear first in results as patient record 1 is the
        //first record chronologically
        patientRecord1.addPhoto(Photo3);

        //photo 1 should be next followed by photo2 and photo3 as they were
        // added in that order to the second record chronologically
        patientRecord2.addPhoto(Photo1);
        patientRecord2.addPhoto(Photo2);
        patientRecord2.addPhoto(Photo4);

        // this is the order they should come back in based on above description
        testPhotos.addAll(Arrays.asList(Photo3, Photo1, Photo2, Photo4));

        //test that all the add calls worked and the photos were added in correct order
        assertEquals("Photos did not come back in correct order",
                testPhotos, problem.getAllPhotosFromRecordsInOrder());

        //remove photo3 from patientRecord1, which removes Photo3 from the results.
        patientRecord1.removePhoto(Photo3);

        //set up testPhotos to match changes
        testPhotos.remove(Photo3);

        //Results should be Photo1, 2, 4
        assertEquals("Expected not to see Photo3 in results as Photo3 was removed",
                testPhotos, problem.getAllPhotosFromRecordsInOrder());

        //this should remove Photo1 as it was the first added
        patientRecord2.removePhoto(0);

        //set up testPhotos to match changes
        testPhotos.remove(Photo1);

        //Results should be Photo2,4
        assertEquals("Expected to only see photo2 and photo4 as both photo1 and 3 are gone",
                testPhotos, problem.getAllPhotosFromRecordsInOrder());

        //Results should be Photo2,4
        assertEquals("Results should be Photo2, and Photo4 as Photo1 was removed",
                testPhotos, patientRecord2.getPhotos());

        //Results should be blank photo array
        assertEquals("Results should be an empty array as photo3 was removed",
                new ArrayList<Photo>(), patientRecord1.getPhotos());
    }

    /* tests: addPhoto, getPhotos, setPhoto, getPhoto */
    @Test
    public void AddGetSetandGetAllPhoto() {
        PatientRecord patientRecord = new PatientRecord();
        ArrayList<Photo> testPhotos = new ArrayList<>();
        Photo testPhoto = new Photo();

        /* add 20 new photos in, adding the new photo to
        both the test array and the patient record */
        for (int i = 0; i < 20; i++) {
            Photo newPhoto = new Photo();
            testPhotos.add(newPhoto);
            patientRecord.addPhoto(newPhoto);
        }

        /* make sure they're equal */
        assertEquals("test photos array did not match the patient record array of photos, add failed",
                testPhotos, patientRecord.getPhotos());

        /* reset test photo */
        testPhotos.clear();

        /* set all 20 pictures in patient record to the same picture */
        for (int i = 0; i < 20; i++) {
            testPhotos.add(testPhoto);
            patientRecord.setPhoto(testPhoto, i);
        }

        /* make sure they're equal */
        assertEquals("patient record did not use set correctly",
                testPhotos, patientRecord.getPhotos());


        /* set all 20 pictures in patient record to the same picture */
        for (int i = 0; i < 20; i++) {
            testPhotos.add(testPhoto);
            patientRecord.setPhoto(testPhoto, i);
        }

        assertEquals("patient record did not store the correct photo",
                patientRecord.getPhoto(0), testPhoto);
    }

//getBodyLocation(int) setBodyLocation(BodyLocation, int), getBodyLocations() addBodyLocation(BodyLocation)
// removeBodyLocation(BodyLocation) removeBodyLocation(int bodyLocationIndex)


    //TODO network and local storage tests

}
