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
package Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.GeoLocation;
import com.cmput301f18t20.medicalphotorecord.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import Controllers.GeoLocationController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

public class ViewMapActivity extends AppCompatActivity {

    private String problemUUID;
    private ArrayList<GeoLocation> problemgeos;
    private GoogleMap mMap;


    private static final String TAG = "ViewMapActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        // Get problem uuid
        Intent intent = getIntent();
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        Log.d(TAG, "onCreateUUID: " + this.problemUUID);

        this.problemgeos = new GeoLocationController().getProblemGeos(ViewMapActivity.this, this.problemUUID);
        Log.d(TAG, "onCreate: " + problemgeos);

        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                LatLng latLng;
                ArrayList<LatLng> latLngArrayList = null;
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));  //move camera to location
                for (GeoLocation g : problemgeos) {

                    // Set the loaded geolocation to latlng object.
                    latLng = new LatLng(g.getLatitude(), g.getLongitude());

                    latLngArrayList.add(latLng);
                }
                Log.d(TAG, "onMapReady: "+latLngArrayList);

            }
        });
    }
}
