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
import Controllers.OfflineSaveController;
import Controllers.PhotoController;
import Exceptions.TooManyPhotosForSinglePatientRecord;

public class SetRecordDisplayPhotos extends AppCompatActivity {

    private GridView photoGridView;
    private TextView instructions;
    protected String recordUUID,
            mode, oppositePhotoUUID,
            oldPhotoUUID;

    protected Photo oppositePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_record_display_photos);

        //get intent and recordUUID
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");

        //If mode is "front" then sets left photo in record, if mode is "back then sets right photo in record
        this.mode = intent.getStringExtra("MODE");
        this.oppositePhotoUUID = intent.getStringExtra("OPPOSITEPHOTO");
        this.oldPhotoUUID = intent.getStringExtra("OLDPHOTOUUID");

        // Delete old photo from temp file
        ArrayList<Photo> newTempPhotos = new PhotoController().loadTempPhotos(this);
        //ArrayList<Photo> newTempPhotos = new PhotoController().deletePhotoFromPhotoList(this, this.oldPhotoUUID, oldTempPhotos);

        // Set old front or back photo isViewedBodyPhoto to "" so that it can't be shown
        for (Photo photo : newTempPhotos) {

            if (photo.getUUID().equals(this.oppositePhotoUUID)) {
                oppositePhoto = photo;
                continue;
            }

            // If old photo then setIsViewedBodyPhoto to "" and then add back to photo list
            if (photo.getUUID().equals(this.oldPhotoUUID)) {
                photo.setIsViewedBodyPhoto("");
                newTempPhotos.add(photo);
            }
        }

        // Save to temp file
        new OfflineSaveController().saveTempPhotoList(newTempPhotos, this);

        this.instructions = (TextView) findViewById(R.id.set_record_photos_title);
        this.photoGridView = (GridView) findViewById(R.id.set_record_photos_gridview);
        this.photoGridView.setAdapter(new ImageAdapter(this, "", "temp"));

    }

    //TODO handle onDestroy(back button pressed)
    @Override
    protected void onResume() {
        super.onResume();


        this.photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Get photo clicked, delete it from temp photo list, add opposite photo from before to list
                Photo photo = (Photo) photoGridView.getAdapter().getItem(position);
                ArrayList<Photo> newTempPhotos = new PhotoController().loadTempPhotos(SetRecordDisplayPhotos.this);
                //ArrayList<Photo> newTempPhotos = new PhotoController().deletePhotoFromPhotoList(SetRecordDisplayPhotos.this, photo.getUUID(), oldTempPhotos);
                newTempPhotos.add(oppositePhoto);

                //if mode == front
                if (SetRecordDisplayPhotos.this.mode.equals("front")) {

                    // If selected photo, then setisViewedBodyPhoto to front and save back to photo list
                    photo.setIsViewedBodyPhoto("front");
                    newTempPhotos.add(photo);
                    new OfflineSaveController().saveTempPhotoList(newTempPhotos, SetRecordDisplayPhotos.this);
                    SetRecordDisplayPhotos.this.finish();
                }

                //if mode == back
                else if (SetRecordDisplayPhotos.this.mode.equals("back")) {

                    // If selected photo, then setisViewedBodyPhoto to back and save back to photo list
                    photo.setIsViewedBodyPhoto("back");
                    newTempPhotos.add(photo);
                    new OfflineSaveController().saveTempPhotoList(newTempPhotos, SetRecordDisplayPhotos.this);
                    SetRecordDisplayPhotos.this.finish();
                }
            }
        });
    }
}
