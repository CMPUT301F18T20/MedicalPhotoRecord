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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientRecordController;
import Controllers.ModifyPatientRecordController;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;


public class ModifyRecordActivity extends AppCompatActivity {
    protected TextView welcome,
            date;
    protected EditText title,
            description;
    protected ImageButton body_location,
            photo;
    protected Button geolocation,
            save_button;

    private String new_title,
            new_description,userID,
            recordUUID;

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
        this.body_location = (ImageButton)findViewById(R.id.modify_record_body_location);
        this.photo =(ImageButton)findViewById(R.id.modify_record_photo);
        this.geolocation = (Button)findViewById(R.id.modify_record_geo);
        this.save_button = (Button)findViewById(R.id.modify_record_save);

        //retrieve record, UUID, userid
        Intent intent = getIntent();
        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        try {
            this.chosen_record = new ElasticsearchPatientRecordController.GetPatientRecordByPatientRecordUUIDTask().execute(this.recordUUID).get();
        } catch(InterruptedException e1){
            throw new RuntimeException(e1);
        }catch (ExecutionException e2){
            throw new RuntimeException(e2);
        }

        //set text
        this.title.setText(this.chosen_record.getTitle());
        this.date.setText(this.chosen_record.getDate().toString());
        this.description.setText(this.chosen_record.getDescription());


    }
    public void onSave(View v){
        //Get new info
        this.new_title= this.title.getText().toString();
        this.new_description = this.description.getText().toString();

        //Create new Record object with updated info
        try{
            this.new_record = ModifyPatientRecordController.createNewRecord(this.userID,this.new_title,this.chosen_record.getDate(),this.new_description);
        }catch(UserIDMustBeAtLeastEightCharactersException e1){
            Toast.makeText(this,"UserID Must Be At Least 8 Characters", Toast.LENGTH_LONG).show();
        } catch(TitleTooLongException e2){
            Toast.makeText(this,"Title is Too Long, Please Re-enter a Shorter Title"
                    ,Toast.LENGTH_LONG)
                    .show();
        }
        //Delete old record, Save new

        ModifyPatientRecordController.modifyRecord(this,this.new_record,this.chosen_record);
        Toast.makeText(this,"Record info has been saved!",Toast.LENGTH_LONG).show();

    }

}
