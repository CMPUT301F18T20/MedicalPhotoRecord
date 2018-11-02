package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PatientRecordUnitTests {
    static final String Correct_User_ID = "abcdefgh";
    static final String Correct_Title = "abcdefgh";

    /* Tests that we can set and get the Location value of a patient record */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetAndSetLocation()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException  {
        /* limit for longitude is +- 180, latitude is +-90. TODO: setter should throw error on violating those */
        int offset = 15;
        PatientRecord record = new PatientRecord(Correct_User_ID, Correct_Title);
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

    /* does not generate UserIDMustBeAtLeastEightCharactersException on valid input
     */
    @Test(expected = Test.None.class /* no exception expected */)
    public void CanGetCreatedUserIDAndConstructorSanity()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        /* list of UserIDs to test against */
        for (String TestUserID : Arrays.asList("18004192", "UserName", "'$%%**?+++")) {

            PatientRecord patientRecord = new PatientRecord(TestUserID, Correct_Title);

            assertEquals("UserIDs did not match.",
                    TestUserID, patientRecord.getCreatedByUserID());
            assertEquals("Titles did not match.",
                    Correct_Title, patientRecord.getTitle());
        }
    }

    /* generates UserIDMustBeAtLeastEightCharactersException on invalid input
     */
    @Test
    public void UserIDMustBeAtLeastEightCharactersExceptionGeneration ()
            throws TitleTooLongException {
        for (String TestUserID : Arrays.asList("Small", "Limits7", "")) {

            try {
                PatientRecord patientRecord = new PatientRecord(TestUserID, Correct_Title);
                fail("UserIDMustBeAtLeastEightCharactersException should have " +
                        "been generated for input " + TestUserID);

            } catch (UserIDMustBeAtLeastEightCharactersException e) {
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
    @Test(expected = Test.None.class /* no exception expected */)
    public void removePhotos()
            throws UserIDMustBeAtLeastEightCharactersException,
            TitleTooLongException, TooManyPhotosForSinglePatientRecord {
        Problem problem = new Problem(Correct_User_ID, Correct_Title);
        ArrayList<Photo> testPhotos = new ArrayList<>();

        // create new photos
        Photo Photo1 = new Photo();
        Photo Photo2 = new Photo();
        Photo Photo3 = new Photo();
        Photo Photo4 = new Photo();

        //add the records to a problem. Going to also be testing
        //the problem's getAllPhotos() method here because this is a
        //common use case
        problem.addRecord(new PatientRecord(Correct_User_ID, Correct_Title));
        problem.addRecord(new PatientRecord(Correct_User_ID, Correct_Title));

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
    @Test(expected = Test.None.class /* no exception expected */)
    public void AddGetSetandGetAllPhoto()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        try {
            PatientRecord patientRecord = new PatientRecord(Correct_User_ID, Correct_Title);
            ArrayList<Photo> testPhotos = new ArrayList<>();
            Photo testPhoto = new Photo();

            /* add 10 new photos in, adding the new photo to
            both the test array and the patient record */
            for (int i = 0; i < 10; i++) {
                Photo newPhoto = new Photo();
                testPhotos.add(newPhoto);
                patientRecord.addPhoto(newPhoto);
            }

            /* make sure they're equal */
            assertEquals("test photos array did not match the patient record array of photos, add failed",
                    testPhotos, patientRecord.getPhotos());

            /* reset test photo */
            testPhotos.clear();

            /* set all 10 pictures in patient record to the same picture */
            for (int i = 0; i < 10; i++) {
                testPhotos.add(testPhoto);
                patientRecord.setPhoto(testPhoto, i);
            }

            /* make sure the arrays are equal */
            assertEquals("patient record did not use set correctly",
                    testPhotos, patientRecord.getPhotos());

            assertEquals("patient record did not store the correct photo",
                    patientRecord.getPhoto(0), testPhoto);
        } catch (TooManyPhotosForSinglePatientRecord e) {
            fail("Unexpected TooManyPhotos exception");
        }
    }

    /* tests: addBodyLocation, getBodyLocations, setBodyLocation, getBodyLocation */
    @Test(expected = Test.None.class /* no exception expected */)
    public void AddGetSetandGetAllBodyLocation()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        PatientRecord patientRecord = new PatientRecord(Correct_User_ID, Correct_Title);
        ArrayList<BodyLocation> testBodyLocations = new ArrayList<>();
        BodyLocation testBodyLocation = new BodyLocation();

        /* add 20 new bodyLocations in, adding the new bodyLocation to
        both the test array and the patient record */
        for (int i = 0; i < 20; i++) {
            BodyLocation newBodyLocation = new BodyLocation();
            testBodyLocations.add(newBodyLocation);
            patientRecord.addBodyLocation(newBodyLocation);
        }

        /* make sure they're equal */
        assertEquals("test bodyLocations array did not match the patient record array of bodyLocations, add failed",
                testBodyLocations, patientRecord.getBodyLocations());

        /* reset test bodyLocation */
        testBodyLocations.clear();

        /* set all 20 bodyLocations in patient record to the same bodyLocation */
        for (int i = 0; i < 20; i++) {
            testBodyLocations.add(testBodyLocation);
            patientRecord.setBodyLocation(testBodyLocation, i);
        }

        /* make sure the arrays are equal */
        assertEquals("patient record did not use set correctly",
                testBodyLocations, patientRecord.getBodyLocations());

        assertEquals("patient record did not store the correct bodyLocation",
                patientRecord.getBodyLocation(0), testBodyLocation);
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void removeBodyLocations()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        ArrayList<BodyLocation> testBodyLocations1 = new ArrayList<>();
        ArrayList<BodyLocation> testBodyLocations2 = new ArrayList<>();

        // create new bodyLocations
        BodyLocation BodyLocation1 = new BodyLocation();
        BodyLocation BodyLocation2 = new BodyLocation();
        BodyLocation BodyLocation3 = new BodyLocation();

        // create new patient records to add the bodyLocations to
        PatientRecord patientRecord1 = new PatientRecord(Correct_User_ID, Correct_Title);
        PatientRecord patientRecord2 = new PatientRecord(Correct_User_ID, Correct_Title);

        // initially array is blank
        assertEquals("internal body location array of patient record was not initialized",
                patientRecord1.getBodyLocations(), testBodyLocations1);

        //Add body location 1 to record 1
        patientRecord1.addBodyLocation(BodyLocation1);
        testBodyLocations1.add(BodyLocation1);

        //test that the add call worked
        assertEquals("BodyLocations did not come back in correct order record 1",
                testBodyLocations1, patientRecord1.getBodyLocations());

        //add body locations 2 and 3 to record 2
        patientRecord2.addBodyLocation(BodyLocation2);
        patientRecord2.addBodyLocation(BodyLocation3);
        testBodyLocations2.addAll(Arrays.asList(BodyLocation2, BodyLocation3));

        //test that all the add calls worked and the bodyLocations were added in correct order
        assertEquals("BodyLocations did not come back in correct order record 2",
                testBodyLocations2, patientRecord2.getBodyLocations());

        //remove bodyLocation1 from patientRecord1
        patientRecord1.removeBodyLocation(BodyLocation1);
        testBodyLocations1.remove(BodyLocation1);

        //array should now be blank
        assertEquals("internal body location array of patient record was not blank",
                testBodyLocations1, patientRecord1.getBodyLocations());

        //this should remove BodyLocation2 as it was the first added
        patientRecord2.removeBodyLocation(0);
        testBodyLocations2.remove(BodyLocation2);

        //Result should be BodyLocation3
        assertEquals("Expected to only see body location 3",
                testBodyLocations2, patientRecord2.getBodyLocations());
    }

    /* tests for existence of this exception */
    @Test
    public void AddTooManyPhotosCausesException()
            throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException {
        PatientRecord patientRecord = new PatientRecord(Correct_User_ID, Correct_Title);

        /* add one less than maximum of new photos in the patient record. Should throw exception */
        for (int i = 0; i < PatientRecord.MAX_PHOTOS -1; i++) {
            try {
                patientRecord.addPhoto(new Photo());
            } catch (TooManyPhotosForSinglePatientRecord e) {
                fail("Unexpected TooManyPhotos exception, only added MAX_PHOTOS - 1 ");
            }
        }

        // add to maximum, should not cause exception
        try {
            patientRecord.addPhoto(new Photo());
        } catch (TooManyPhotosForSinglePatientRecord e) {
            fail("Unexpected TooManyPhotos exception, only added MAX_PHOTOS");
        }

        /* add one more than maximum. Should throw exception */
            try {
                patientRecord.addPhoto(new Photo());
                fail("Expected to get TooManyPhotos exception, added MAX_PHOTOS + 1 ");
            } catch (TooManyPhotosForSinglePatientRecord e) {
                //do nothing as this is correct functionality
            }
    }

    //TODO network and local storage tests

}
