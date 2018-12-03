/*
 * Class name: ProviderViewPatientRecordActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/2/18 9:21 AM
 *
 * Last Modified: 12/2/18 9:14 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.ModifyPatientRecordController;
import Controllers.PhotoController;
import Exceptions.NoSuchRecordException;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderViewPatientRecordActivity extends AppCompatActivity {

    protected TextView title,date,description;
    protected ImageButton view_front_body_button, view_back_body_button;
    protected Button geolocation;

    private PatientRecord currentRecord;
    private String recordUUID,userID,problemUUID;
    private String TAG = "ProviderViewPatientRecordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_view_patient_record);

        // Set all views
        this.title = (TextView)findViewById(R.id.view_record_title);
        this.date = (TextView)findViewById(R.id.view_record_date);
        this.description = (TextView)findViewById(R.id.view_record_description);
        this.view_front_body_button = (ImageButton)findViewById(R.id.view_front_body);
        this.view_back_body_button = (ImageButton)findViewById(R.id.view_back_body);
        this.geolocation = findViewById(R.id.button2);

        // Get intent
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        this.userID = intent.getStringExtra("USERIDEXTRA");

        // Get record object
        try {
            this.currentRecord = new ModifyPatientRecordController().getPatientRecord(this,this.recordUUID);
        } catch (NoSuchRecordException e) {
            Toast.makeText(this, "Record does not exist", Toast.LENGTH_LONG).show();
        }

        //Set text
        String tempString = "Record: "+ this.currentRecord.getTitle();
        this.title.setText(tempString);

        tempString = "Date: " + this.currentRecord.getDate();
        this.date.setText(tempString);

        tempString = "Description: "+ this.currentRecord.getDescription();
        this.description.setText(tempString);

        // Set body photos
        ArrayList<Photo> tempPhotos = new PhotoController().getBodyPhotosForRecord(this, this.recordUUID);
        for (Photo photo: tempPhotos){

            if (photo.getIsViewedBodyPhoto().equals("")){
                continue;
            }
            else if (photo.getIsViewedBodyPhoto().equals("front")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.view_front_body_button.setImageBitmap(bitmapCompressed);
            }
            else if (photo.getIsViewedBodyPhoto().equals("back")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.view_back_body_button.setImageBitmap(bitmapCompressed);
            }
        }
    }

    public void onViewGeoLocationClick(View v){

        Intent intent = new Intent(this, ViewGeoActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        Log.d(TAG, "onViewGeoLocationClick: "+this.recordUUID);
        //startActivity(intent);
    }
}
