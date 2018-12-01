/*
 * Class name: GeoLocationController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/30/18 7:21 PM
 *
 * Last Modified: 11/30/18 7:21 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.GeoLocation;

import java.util.ArrayList;

public class GeoLocationController {

    private static final String TAG = "GeoLocationController";

    public GeoLocation getGeoLocation(Context context, String uuid) {

        // Compare uuid to every problem's uuid to get problem
        ArrayList<GeoLocation> geoLocations = new OfflineLoadController().loadGeoLocationList(context);
        for (GeoLocation g : geoLocations) {
            if (uuid.equals(g.getRecordUUID())) {
                return g;
            }
        }

        Log.d(TAG, "getGeoLocation: ");
        // If not found
        return null;
    }

    public void addGeoLocation(Context context, GeoLocation geoLocation){

        // Get list of Geolocations, add, save list of Geolocations
        ArrayList<GeoLocation> geoLocations = new OfflineLoadController().loadGeoLocationList(context);
        geoLocations.add(geoLocation);
        new OfflineSaveController().saveGeoLocationList(geoLocations,context);
        Log.d(TAG, "addGeoLocation: "+geoLocation.getLatitude());

    }
}
