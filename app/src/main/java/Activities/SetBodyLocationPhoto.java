/*
 * Class name: SetBodyLocationPhoto
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/1/18 1:47 PM
 *
 * Last Modified: 12/1/18 1:47 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.concurrent.ExecutionException;

import Controllers.SetBodyPhotoController;

public class SetBodyLocationPhoto extends AppCompatActivity {

    private GridView photoGridView;
    String recordUUID;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_body_location_photo);

        // Get record UUID and mode
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        this.mode = intent.getStringExtra("MODE");

        this.photoGridView = (GridView) findViewById(R.id.set_body_location_gridview);
        this.photoGridView.setAdapter(new ImageAdapter(this, this.recordUUID, "body"));

    }

    @Override
    protected void onResume(){
        super.onResume();

        this.photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Set old display image to not be display
                new SetBodyPhotoController().setOldDisplayPhotoToNotBeDisplayed(SetBodyLocationPhoto.this, SetBodyLocationPhoto.this.recordUUID, SetBodyLocationPhoto.this.mode);

                // Set clicked image to be display
                Photo photo = (Photo) photoGridView.getAdapter().getItem(position);
                try {
                    new SetBodyPhotoController().setNewClickedPhotoToBeDisplayed(SetBodyLocationPhoto.this, SetBodyLocationPhoto.this.recordUUID, SetBodyLocationPhoto.this.mode, photo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SetBodyLocationPhoto.this.finish();

            }
        });
    }
}
