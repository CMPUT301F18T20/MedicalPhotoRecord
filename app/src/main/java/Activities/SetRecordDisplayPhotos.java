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
import Controllers.OfflineLoadController;
import Controllers.OfflineSaveController;
import Controllers.PhotoController;
import Exceptions.TooManyPhotosForSinglePatientRecord;

/**
 * SetBodyLocationPhoto Activity
 * Used in add record
 * When patient clicked on body location photo, it will redirect to this activty
 * Show grid view body location photos, Set body location photo to the photo that patient click
 * @version 2.0
 * @see Photo
 * @see com.cmput301f18t20.medicalphotorecord.Patient
 * @see com.cmput301f18t20.medicalphotorecord.Record
 */
public class SetRecordDisplayPhotos extends AppCompatActivity {

    private GridView photoGridView;
    private TextView instructions;
    protected String recordUUID,
            mode, oppositePhotoUUID,
            oldPhotoUUID;

    protected Photo oppositePhoto;

    /**
     * Get from intent recordUUID,mod
     * Set necessary view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_record_display_photos);

        //get intent and recordUUI, mode ("front" or "back")
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        this.mode = intent.getStringExtra("MODE");

        ArrayList<Photo> tempPhotos = new PhotoController().loadTempPhotos(this);
        ArrayList<Photo> newTempPhotos = new ArrayList<>();

        // Add to new list of photo all old photo in temp photo list, except for old selected photo
        // Find old selected photo via getIsViewedBodyPhoto, Set old selected photo isViewBodyPhoto to "", then add to new list of photos
        for (Photo photo : tempPhotos) {

            if (photo.getIsViewedBodyPhoto().equals(this.mode)) {
                photo.setIsViewedBodyPhoto("");
                newTempPhotos.add(photo);
            }else{
                newTempPhotos.add(photo);
            }
        }
        new OfflineSaveController().saveTempPhotoList(newTempPhotos, this);

        this.instructions = (TextView) findViewById(R.id.set_record_photos_title);
        this.photoGridView = (GridView) findViewById(R.id.set_record_photos_gridview);
        this.photoGridView.setAdapter(new ImageAdapter(this, "", "temp"));

    }

    /**
     * Set clicked body location photo to be displayed
     */
    //TODO handle onDestroy(back button pressed)
    @Override
    protected void onResume() {
        super.onResume();


        this.photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Get photo clicked, delete it from temp photo list
                Photo photo = (Photo) photoGridView.getAdapter().getItem(position);
                ArrayList<Photo> tempPhotos = new OfflineLoadController().loadTempPhotoList(SetRecordDisplayPhotos.this);
                ArrayList<Photo> newTempPhotos = new ArrayList<>();

                // Add to new list of photo all old photo in temp photo list, except for selected photo
                // Set selected photo isViewBodyPhoto to either "front" or "back", then add to new list of photos
                for (Photo p:tempPhotos){

                    if (p.getBodyLocation().length() != 0 && p.getUUID().equals(photo.getUUID())){
                        p.setIsViewedBodyPhoto(SetRecordDisplayPhotos.this.mode);
                        newTempPhotos.add(p);
                    }else{
                        newTempPhotos.add(p);
                    }

                }

                new OfflineSaveController().saveTempPhotoList(newTempPhotos, SetRecordDisplayPhotos.this);
                SetRecordDisplayPhotos.this.finish();
            }
        });
    }
}
