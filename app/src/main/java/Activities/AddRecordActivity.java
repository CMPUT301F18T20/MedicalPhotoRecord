package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;

import Controllers.AddDeleteRecordController;
import Controllers.OfflineLoadController;
import Controllers.PhotoController;
import Exceptions.TitleTooLongException;
import Exceptions.TooManyPhotosForSinglePatientRecord;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class AddRecordActivity extends AppCompatActivity {

    private EditText record_title_edit;
    private EditText record_description_edit;
    private TextView record_date_text;
    private Button save_record_button;
    private ImageButton add_image_button;
    private Button set_geolocation_button;
    private ImageButton add_front_bodylocation_button;
    private ImageButton add_back_bodylocation_button;

    private Date record_date = new Date();
    private String record_title;
    private String record_description;
    private String userId;
    private String problemUUID;
    private String bodyLocation;
    private int onCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        this.onCurrentPage = 1;
        // Get Views
        this.record_title_edit = (EditText)findViewById(R.id.add_record_title_id);
        this.record_description_edit = (EditText)findViewById(R.id.add_record_description_id);
        this.record_date_text = (TextView) findViewById(R.id.record_date_id);
        this.save_record_button = (Button)findViewById(R.id.record_add_button_id);
        this.set_geolocation_button = (Button)findViewById(R.id.set_geolocation_button_id);
        this.add_front_bodylocation_button = (ImageButton)findViewById(R.id.add_F_bodyLocation_id);
        this.add_back_bodylocation_button = (ImageButton)findViewById(R.id.add_R_bodyLocation_id);

        this.record_date_text.setText(this.record_date.toString());

        //set desired photos for display
        ArrayList<Photo> tempPhotos = new  PhotoController().loadTempPhotos(this);
        for (Photo photo: tempPhotos){
            if (photo.getIsViewedBodyPhoto().equals("")){
                continue;
            }
            else if (photo.getIsViewedBodyPhoto().equals("front")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.add_front_bodylocation_button.setImageBitmap(bitmapCompressed);
            }
            else if (photo.getIsViewedBodyPhoto().equals("back")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.add_back_bodylocation_button.setImageBitmap(bitmapCompressed);
            }
        }
        Intent intent = getIntent();
        this.userId = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra("PROBLEMIDEXTRA");
        this.bodyLocation = intent.getStringExtra("BODYLOCATION");
        init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // clear all temporary photos if they click the back button
        new PhotoController().clearTempPhotos(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if user is on current activity and goes to another activity --> onCurrentPage = 0
        //Ensures that only deletes if the user intentionally exits app and doesn't come back
        if (this.onCurrentPage == 1){
            // clear all temporary photos
            new PhotoController().clearTempPhotos(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.onCurrentPage = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCurrentPage = 1;

        ArrayList<Photo> tempPhotos = new OfflineLoadController().loadTempPhotoList(this);
        for (Photo photo: tempPhotos){

            if (photo.getIsViewedBodyPhoto().equals("")){
                continue;
            }
            else if (photo.getIsViewedBodyPhoto().equals("front")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.add_front_bodylocation_button.setImageBitmap(bitmapCompressed);
            }
            else if (photo.getIsViewedBodyPhoto().equals("back")){
                Bitmap bitmap = photo.getBitmapFromString();
                Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                this.add_back_bodylocation_button.setImageBitmap(bitmapCompressed);
            }
        }
    }

    public void setFrontPhoto(View view){
        Intent intent = new Intent(this,SetRecordDisplayPhotos.class);
        intent.putExtra("PATIENTRECORDIDEXTRA","");
        intent.putExtra("MODE","front");
        this.onCurrentPage = 0;
        startActivity(intent);

    }

    public void setBackPhoto(View view){
        Intent intent = new Intent(this,SetRecordDisplayPhotos.class);
        intent.putExtra("PATIENTRECORDIDEXTRA","");
        intent.putExtra("MODE","back");
        this.onCurrentPage = 0;
        startActivity(intent);
    }
    // Add record photo
    public void onAddPhotoClick(View v){

        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra("PATIENTRECORDIDEXTRA", "");
        intent.putExtra("BODYLOCATION", "");
        intent.putExtra("ISADDRECORDACTIVITY","true");
        this.onCurrentPage = 0;
        startActivity(intent);
    }

    //view photos added
    public void onViewPhotos(View v){
        Intent intent = new Intent(this,ViewAddedPhotoActivity.class);
        this.onCurrentPage = 0;
        startActivity(intent);
    }

    // Save all necessary info to record
    public void addRecordButton(View view){

        // get record info
        this.record_title = this.record_title_edit.getText().toString();
        this.record_description = this.record_description_edit.getText().toString();

        // Save record
        PatientRecord record = null;

        try{
            record = new AddDeleteRecordController().createRecord(this.problemUUID, this.userId, this.record_title, this.record_date, this.record_description);
            record.setBodyLocation(this.bodyLocation);
            // Save photo and body location photo
            new PhotoController().saveTempPhotosToDatabase(this, record.getUUID());

            // Save geo?

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(AddRecordActivity.this, "Your userId has to contains more than 8 characters",Toast.LENGTH_LONG).show();
        } catch (TitleTooLongException e) {
            Toast.makeText(AddRecordActivity.this, "Your title is too long",Toast.LENGTH_LONG).show();
        }

        new AddDeleteRecordController().saveRecord(this,record);
        Toast.makeText(AddRecordActivity.this, "Your new record has been added!",Toast.LENGTH_LONG).show();
    }

    //On click listener on geo button.
    private void init(){
        Button btnMap = (Button) findViewById(R.id.set_geolocation_button_id);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecordActivity.this, AddGeoActivity.class);
                startActivity(intent);
            }
        });
    }
}
