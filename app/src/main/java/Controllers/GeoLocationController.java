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

import com.cmput301f18t20.medicalphotorecord.GeoLocation;

import java.util.ArrayList;

import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class GeoLocationController {

    public void saveGeoLocation(Context context, GeoLocation geoLocation) {

        //Online

        //Offline
        ArrayList<GeoLocation> geoLocations=  new OfflineLoadController().loadGeoLocationLIst(context);
        geoLocations.add(geoLocation);
        new OfflineSaveController().saveGeoLocation(geoLocations,context);
    }

}
