package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    //TODO network and local storage tests

}
