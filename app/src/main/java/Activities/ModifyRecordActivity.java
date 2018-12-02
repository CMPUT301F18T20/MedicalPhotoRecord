/*
 * Class name: ModifyRecordActivity
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 19/11/18 1:27 PM
 *
 * Last Modified: 19/11/18 1:27 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientRecordController;

import Controllers.ModifyPatientRecordController;
import Controllers.PhotoController;
import Exceptions.NoSuchRecordException;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

/**
 * ModifyRecordActivity
 * Simply modifies a selected record
 * Title, date, description, geolocation can be modified.
 * additional photos can be added.
 *
 * @version 1.0
 * @since   2018-12-01
 */

public class ModifyRecordActivity extends AppCompatActivity {
    protected TextView welcome,
            date;
    protected EditText title,
            description;
    protected ImageButton modify_front_button, modify_back_button;
    protected Button geolocation,
            save_button;

    private String new_title,
            new_description,userID,
            recordUUID, problemUUID;

    private PatientRecord chosen_record,
            new_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_record);

        //initialize buttons and text
        this.welcome = (TextView)findViewById(R.id.modify_record_welcome);
        this.date = (TextView)findViewById(R.id.modify_record_date);
        this.title = (EditText)findViewById(R.id.modify_record_title);
        this.description = (EditText)findViewById(R.id.modify_record_description);
        this.modify_front_button = findViewById(R.id.modify_front_body);
        this.modify_back_button = findViewById(R.id.modify_back_body);
        this.geolocation = (Button)findViewById(R.id.modify_record_geo);
        this.save_button = (Button)findViewById(R.id.modify_record_save);

        //retrieve record, UUID, userid, problem uuid
        Intent intent = getIntent();
        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        try {
            this.chosen_record = new ModifyPatientRecordController().getPatientRecord(this,this.recordUUID);
        } catch (NoSuchRecordException e) {
            Toast.makeText(this,"Record does not exist",Toast.LENGTH_LONG).show();        }
        this.problemUUID = this.chosen_record.getAssociatedProblemUUID();


        //set text
        this.title.setText(this.chosen_record.getTitle());
        this.date.setText(this.chosen_record.getDate().toString());
        this.description.setText(this.chosen_record.getDescription());

        // clear all temporary photos
        new PhotoController().clearTempPhotos(this);


    }
    public void onSave(View v){
        //Get new info
        this.new_title= this.title.getText().toString();
        this.new_description = this.description.getText().toString();

        // Save photo and body location photo
        new PhotoController().saveTempPhotosToDatabase(this, this.chosen_record.getUUID());

        // Save geo?

        ModifyPatientRecordController.modifyRecord(this,this.chosen_record, this.new_title,this.new_description);
        Toast.makeText(this,"Record info has been saved!",Toast.LENGTH_LONG).show();

    }

    // Add record photo

    /**
     * This method is called when add_photo_button_modify_id is clicked
     *
     * @param v - current view
     */
    public void onAddPhotoClickModify(View v){

        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra("PATIENTRECORDIDEXTRA", "");
        intent.putExtra("BODYLOCATION", "");
        intent.putExtra("ISADDRECORDACTIVITY","true");
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();

        ArrayList<Photo> tempPhotos = new PhotoController().getBodyPhotosForRecord(ModifyRecordActivity.this, this.recordUUID);
        for (Photo photo: tempPhotos){

            if (photo.getIsViewedBodyPhoto().equals("")){
                continue;
            }
            else if (photo.getIsViewedBodyPhoto().equals("front")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.modify_front_button.setImageBitmap(bitmapCompressed);
            }
            else if (photo.getIsViewedBodyPhoto().equals("back")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.modify_back_button.setImageBitmap(bitmapCompressed);
            }
        }
    }

}
