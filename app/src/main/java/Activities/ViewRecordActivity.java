package Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.GeoLocation;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.concurrent.ExecutionException;

import Controllers.ElasticsearchPatientRecordController;
import Controllers.GeoLocationController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

public class ViewRecordActivity extends AppCompatActivity {
    protected TextView title,date,description,geodisplay;
    protected ImageButton body_location,photo;
    protected Button geolocation;
    GeoLocation currentgeo;

    private PatientRecord currentRecord;
    private String recordUUID,userID,problemUUID;

    //for checking map services
    private static final String TAG = "ViewRecordActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        //initialize text and buttons
        this.title = (TextView)findViewById(R.id.view_record_title);
        this.date = (TextView)findViewById(R.id.view_record_date);
        this.description = (TextView)findViewById(R.id.view_record_description);
        this.body_location = (ImageButton)findViewById(R.id.view_record_body_location);
        this.photo = (ImageButton)findViewById(R.id.view_record_photo);
        this.geolocation = (Button)findViewById(R.id.view_record_geo);
        //this.geodisplay = (TextView)findViewById(R.id.record_view_geo_id);

        //Get Record object through intent
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);

        try{
            this.currentRecord = new ElasticsearchPatientRecordController
                    .GetPatientRecordByPatientRecordUUIDTask().execute(this.recordUUID).get();
        }catch (InterruptedException e1){
            e1.printStackTrace();
        }catch (ExecutionException e2){
            e2.printStackTrace();
        }

        /*this.currentgeo = new GeoLocationController().getGeoLocation(ViewRecordActivity.this,this.recordUUID);
        Log.d(TAG, "onCreate: "+currentgeo.getLongitude()+currentgeo.getLatitude()+currentgeo.getAddress());
        Log.d(TAG, "onCreate: UUID"+recordUUID);*/

        //Set text
        String tempString = "Record: "+ this.currentRecord.getTitle();
        this.title.setText(tempString);

        tempString = "Date: " + this.currentRecord.getDate();
        this.date.setText(tempString);

        tempString = "Description: "+ this.currentRecord.getDescription();
        this.description.setText(tempString);

         /*if (currentgeo.getAddress() == null) {
            String geodisplay = "The GeoLocation is not set";
            this.geodisplay.setText(geodisplay);
        } else {
            String geodisplay = "The GeoLocation is currently set at:  "+currentgeo.getAddress();
            this.geodisplay.setText(geodisplay);
        }*/


        //TODO Add setting of body_location photo + photo + geolocation
    }

    // View record's GeoLocation in map
    public void onViewGeoLocationClick(View v){

        Intent intent = new Intent(this, ViewGeoActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        Log.d(TAG, "onViewGeoLocationClick: "+this.recordUUID);
        startActivity(intent);
    }

    // Browse record's photos
    public void onBrowseRecordPhotosClick(View v){

        Intent intent = new Intent(this, BrowseRecordPhotosActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        startActivity(intent);
    }

    // Browse record's photos
    public void onBrowseRecordBodyPhotosClick(View v){

        Intent intent = new Intent(this, BrowseBodyLocationPhotosActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        startActivity(intent);
    }

}
