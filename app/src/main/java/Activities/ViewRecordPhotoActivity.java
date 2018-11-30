package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.PhotoController;

public class ViewRecordPhotoActivity extends AppCompatActivity {

    private ImageView recordPhotoImageView;
    private String recordUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record_photo);

        // Get position and record uuid
        Intent intent = getIntent();
        int position = intent.getIntExtra("pos",0);
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");

        // Set image view
        ArrayList<Photo> recordPhotos = new PhotoController().getPhotosForRecord(this, this.recordUUID);
        ArrayList<Bitmap> recordBitmaps = new PhotoController().getBitMapForPhotoList(ViewRecordPhotoActivity.this, recordPhotos);

        recordPhotoImageView = findViewById(R.id.view_record_photo_image_view_id);
        Bitmap gotBimapBigger = Bitmap.createScaledBitmap(recordBitmaps.get(position), 1000, 1000, true);
        recordPhotoImageView.setImageBitmap(gotBimapBigger);
    }
}
