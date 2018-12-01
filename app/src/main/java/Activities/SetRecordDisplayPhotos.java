package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Controllers.AddDeleteRecordController;
import Controllers.PhotoController;
import Exceptions.TooManyPhotosForSinglePatientRecord;

public class SetRecordDisplayPhotos extends AppCompatActivity {

    private GridView photoGridView;
    private TextView instructions;
    private String recordUUID,
            mode,
            oldPhotoUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_record_display_photos);

        //get intent and recordUUID
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");

        //If mode is "front" then sets left photo in record, if mode is "back then sets right photo in record
        this.mode = intent.getStringExtra("MODE");

        //sets the old photo to "" so that new photo can be displayed
        this.oldPhotoUUID = intent.getStringExtra("OLDPHOTOUUID");

        ArrayList<Photo> photos = new PhotoController().loadTempPhotos(this);
        //clear the old temp list
        new PhotoController().clearTempPhotos(this);

        for(Photo photo:photos){
            //if old photo then setIsViewedBodyPhoto to "" and then save back to tempfile
            if(photo.getUUID().equals(this.oldPhotoUUID)){
                photo.setIsViewedBodyPhoto("");
                try {
                    new PhotoController().saveAddPhoto(this,photo,"tempSave");
                } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                    tooManyPhotosForSinglePatientRecord.printStackTrace();
                }
            } else{ //else save it back
                try {
                    new PhotoController().saveAddPhoto(this,photo,"tempSave");
                } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                    tooManyPhotosForSinglePatientRecord.printStackTrace();
                }
            }
        }


        this.instructions = (TextView)findViewById(R.id.set_record_photos_title);
        this.photoGridView = (GridView)findViewById(R.id.set_record_photos_gridview);


        this.photoGridView.setAdapter(new ImageAdapter(this));

    }
    //TODO handle onDestroy(back button pressed)
    @Override
    protected void onResume() {
        super.onResume();

        this.photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //if mode == front
                if(SetRecordDisplayPhotos.this.mode.equals("front")){
                    Photo photo = (Photo)photoGridView.getAdapter().getItem(position);
                    //fetch photos from tempfile
                    ArrayList<Photo> photos = new PhotoController().loadTempPhotos(SetRecordDisplayPhotos.this);
                    //clear tempfile
                    new PhotoController().clearTempPhotos(SetRecordDisplayPhotos.this);

                    for(Photo fetchedPhoto: photos){

                        //if selected photo, then setisViewedBodyPhoto to front and save back to tempfile
                        if(photo.getUUID().equals(fetchedPhoto.getUUID())){
                            photo.setIsViewedBodyPhoto("front");
                            try {
                                new PhotoController().saveAddPhoto(SetRecordDisplayPhotos.this,photo,"tempSave");
                            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                                tooManyPhotosForSinglePatientRecord.printStackTrace();
                            }
                        } else{
                            try {
                                new PhotoController().saveAddPhoto(SetRecordDisplayPhotos.this,fetchedPhoto,"tempSave");

                            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                                tooManyPhotosForSinglePatientRecord.printStackTrace();
                            }
                        }
                    }
                    SetRecordDisplayPhotos.this.finish();
                }
                //if mode == back
                else if (SetRecordDisplayPhotos.this.mode.equals("back")){
                        Photo photo = (Photo)photoGridView.getAdapter().getItem(position);
                    //fetch photos from tempfile
                    ArrayList<Photo> photos = new PhotoController().loadTempPhotos(SetRecordDisplayPhotos.this);
                    //clear tempfile
                    new PhotoController().clearTempPhotos(SetRecordDisplayPhotos.this);

                    for(Photo fetchedPhoto: photos){

                        //if selected photo, then setisViewedBodyPhoto to front and save back to tempfile
                        if(photo.getUUID().equals(fetchedPhoto.getUUID())){
                            photo.setIsViewedBodyPhoto("back");
                            try {
                                new PhotoController().saveAddPhoto(SetRecordDisplayPhotos.this,photo,"tempSave");

                            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                                tooManyPhotosForSinglePatientRecord.printStackTrace();
                            }
                        } else{
                            try {
                                new PhotoController().saveAddPhoto(SetRecordDisplayPhotos.this,fetchedPhoto,"tempSave");

                            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                                tooManyPhotosForSinglePatientRecord.printStackTrace();
                            }
                        }
                    }
                    SetRecordDisplayPhotos.this.finish();
                }
            }
        });
    }
}
