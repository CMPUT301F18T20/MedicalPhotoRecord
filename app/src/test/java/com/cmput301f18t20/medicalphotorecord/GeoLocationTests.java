/*
 * Class name: GeoLocation
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/27/18 6:21 PM
 *
 * Last Modified: 11/27/18 7:50 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */
package com.cmput301f18t20.medicalphotorecord;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeoLocationTests{

    private GeoLocation geo;

    static final double longitude = 50;
    static final double latitude = 50;
    static final String address = "University of Alberta";


    @Test
    public void GeolocationTests(){
        geo = new GeoLocation(longitude, latitude, address);

        // Tests for get methods
        assertEquals(longitude, geo.getLongitude(), 0.0);
        assertEquals(latitude, geo.getLatitude(), 0.0);
        assertEquals(address, geo.getAddress());

        // Tests for set methods
        final double newlongitude = 60;
        final double newlatitude = 60;
        final String newaddress = "Home";

        geo.setLongitude(newlongitude);
        geo.setLatitude(newlatitude);
        geo.setAddress(newaddress);

        assertEquals(newlongitude, geo.getLongitude(), 0.0);
        assertEquals(newlatitude, geo.getLatitude(), 0.0);
        assertEquals(newaddress, geo.getAddress());
    }

}