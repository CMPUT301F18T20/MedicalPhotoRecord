package Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.OfflineLoadController;

public class BrowseRecordPhotosActivity extends AppCompatActivity {

    private ImageView photoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_record_photos);

        this.photoImageView = findViewById(R.id.photo_image_view_id);


    }

    @Override
    protected void onResume(){
        super.onResume();

        ArrayList<Photo> photos = new OfflineLoadController().loadPhotoList(BrowseRecordPhotosActivity.this);
        Log.d("photos size", String.valueOf(photos.size()));
        Photo photo = photos.get(7);
        Bitmap bitmapN = photo.getBitmapFromString();
        Log.d("photos bitmap records", String.valueOf(photo.getBitmapFromString()));
        this.photoImageView.setImageBitmap(bitmapN);

    }
}
