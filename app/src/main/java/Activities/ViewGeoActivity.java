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

import Controllers.GeoLocationController;

public class ViewGeoActivity extends AppCompatActivity {

    private String recordUUID;
    private GeoLocation geoLocation;
    private GoogleMap mMap;
    private LatLng latLng;

    private static final String TAG = "ViewGeoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geo);

        // Get record uuid
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        Log.d(TAG, "onCreateUUID: " + this.recordUUID);

        try{
            this.geoLocation = new GeoLocationController().getGeoLocation(ViewGeoActivity.this, this.recordUUID);
            Log.d(TAG, "onCreate: " + geoLocation.getLatitude());

            // Set the loaded geolocation to latlng object.
            this.latLng = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());

            //call draw marker
            init();
        }catch (Exception e){
            Toast.makeText(this,"No geolocation set", Toast.LENGTH_LONG).show();
        }


    }

    //The init method draw marker on map with the latlng object.
    private void init(){

        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));  //move camera to location
                mMap.clear();
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(geoLocation.getAddress());
                mMap.addMarker(options);
            }
        });
    }

}
