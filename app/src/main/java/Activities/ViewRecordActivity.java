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
import static android.widget.Toast.LENGTH_LONG;

/**
 * ViewRecordActivity
 * Simply displays the details and information of a selected record
 * Contains title,date,description, body location photos, record photos
 * and geolocation.
 *
 * @version 1.0
 * @since   2018-12-01
 */


public class ViewRecordActivity extends AppCompatActivity {
    protected TextView title,date,description,geodisplay;
    protected ImageButton body_location,photo;
    protected Button geolocation;
    GeoLocation currentgeo;

    private PatientRecord currentRecord;
    protected String recordUUID,userID,problemUUID;

    //for checking map services
    private static final String TAG = "ViewRecordActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        //initialize text and buttons

        this.title = findViewById(R.id.view_record_title);
        this.date = findViewById(R.id.view_record_date);
        this.description = findViewById(R.id.view_record_description);
        this.view_front_body_button = (ImageButton)findViewById(R.id.view_front_body);
        this.view_back_body_button = (ImageButton)findViewById(R.id.view_back_body);
        this.geolocation = findViewById(R.id.view_record_geo);
        this.geodisplay = (TextView)findViewById(R.id.record_view_geo_id);

        //Get Record object through intent
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);

        try{
            this.currentRecord = new ElasticsearchPatientRecordController
                    .GetPatientRecordByPatientRecordUUIDTask().execute(this.recordUUID).get();
        }catch (InterruptedException e){
            Toast.makeText(this, "Interrupted while fetching current record", LENGTH_LONG).show();
            finish();
        }catch (ExecutionException e){
            Toast.makeText(this, "Execution exception while fetching current record", LENGTH_LONG).show();
            finish();
        }

        this.currentgeo = new GeoLocationController().getGeoLocation(ViewRecordActivity.this,this.recordUUID);
        Log.d(TAG, "onCreate: "+currentgeo.getLongitude()+currentgeo.getLatitude()+currentgeo.getAddress());
        Log.d(TAG, "onCreate: UUID"+recordUUID);

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

    /**
     * On click listener on geo button
     */
    private void init(){
        Button btnMap =  findViewById(R.id.view_record_geo);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRecordActivity.this, ViewGeoActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * this method checks if the google play services in android device is ok or not.
     * @return - true if service is working, otherwise false.
     */
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

    // View record's GeoLocation in map
    public void onViewGeoLocationClick(View v){

        Intent intent = new Intent(this, ViewGeoActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        Log.d(TAG, "onViewGeoLocationClick: "+this.recordUUID);
        startActivity(intent);
    }

    /**
     * This method is called when browse_record_photos_id button is clicked
     * and starts BrowseRecordPhotoActivity.
     * @param v - current view
     */
    public void onBrowseRecordPhotosClick(View v){

        Intent intent = new Intent(this, BrowseRecordPhotosActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        startActivity(intent);
    }

    /**
     * This method is called when browse_body_location_photos_id button is clicked
     * and starts BrowseBodyLocationPhotosActivity
     * @param v - current view
     */
    public void onBrowseRecordBodyPhotosClick(View v){

        Intent intent = new Intent(this, BrowseBodyLocationPhotosActivity.class);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        startActivity(intent);
    }

}
