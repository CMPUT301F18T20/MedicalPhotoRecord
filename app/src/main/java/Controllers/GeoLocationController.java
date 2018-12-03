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
import android.location.Location;
import android.util.Log;
import com.cmput301f18t20.medicalphotorecord.GeoLocation;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GeoLocationController {

    //For Debug
    private static final String TAG = "GeoLocationController";

    public GeoLocation getGeoLocation(Context context, String recordUUID) {

        //Online
        GeoLocation onlineGeoLocation = null;
        try {
            onlineGeoLocation = new ElasticsearchGeoLocationController.GetGeoByGeoRecordUUIDTask().execute(recordUUID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        ArrayList<GeoLocation> geoLocations = new OfflineLoadController().loadGeoLocationList(context);
        GeoLocation offlineGeoLocation = new GeoLocation();
        for (GeoLocation g : geoLocations) {
            if (recordUUID.equals(g.getRecordUUID())) {
                offlineGeoLocation = g;
            }
        }

        // Syncing
        Log.d(TAG, "getGeoLocation: "+onlineGeoLocation.getLatitude()+offlineGeoLocation.getLatitude());
        GeoLocation actual = onlineGeoLocation;
        return actual;
    }

    public ArrayList<GeoLocation> getProblemGeos(Context context, String ProblemUUID) {

        // Online
        ArrayList<GeoLocation> onlineGeos = new ArrayList<>();
        try {
            onlineGeos = new ElasticsearchGeoLocationController.GetGeosByProblemUUIDTask().execute(ProblemUUID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Offline
        // Compare uuid to every problem's uuid to get geos for Problem
        ArrayList<GeoLocation> geoLocations = new OfflineLoadController().loadGeoLocationList(context);
        ArrayList<GeoLocation> problemGeos = new ArrayList<>();

        //Loop through all geos to find matched problem geos in to a list
        for (GeoLocation g : geoLocations) {
            if (ProblemUUID.equals(g.getProblemUUID())) {
                problemGeos.add(g);
            }
        }

        // Syncing
        ArrayList<GeoLocation> actual = onlineGeos;
        return actual;
    }

    public void addGeoLocation(Context context, GeoLocation geoLocation, String mode) {

        // Get list of Geolocations, add, save list of Geolocations
        ArrayList<GeoLocation> tempgeoLocations = new OfflineLoadController().loadTempGeoLocationList(context);

        // Actually saving the geo to database

        //Online

        //Offline
        if (mode == "actualSave") {

            // Online
            try {
                new ElasticsearchGeoLocationController.AddGeoTask().execute(geoLocation).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Offline
            ArrayList<GeoLocation> geoLocations = new OfflineLoadController().loadGeoLocationList(context);
            geoLocations.add(geoLocation);
            new OfflineSaveController().saveGeoLocationList(geoLocations, context);
        }

        // Temporary storage for later saving, after click save of record
        if (mode == "tempSave"){
            tempgeoLocations.add(geoLocation);
            new OfflineSaveController().saveTempGeoLocationList(tempgeoLocations, context);
        }
    }

    public void clearTempGeoLocations (Context context){

        ArrayList<GeoLocation> tempgeoLocations = new ArrayList<>();
        new OfflineSaveController().saveTempGeoLocationList(tempgeoLocations, context);
    }

    public void saveTempGeosToDatabase(Context context, String recordUUID){

        // Add all temporary photos to actual photo database
        ArrayList<GeoLocation> tempgeoLocations = new OfflineLoadController().loadTempGeoLocationList(context);
        for (GeoLocation g:tempgeoLocations){
            g.setRecordUUID(recordUUID);
            addGeoLocation(context, g, "actualSave");
        }

        // Clear temp Geo file
        clearTempGeoLocations(context);
    }
}