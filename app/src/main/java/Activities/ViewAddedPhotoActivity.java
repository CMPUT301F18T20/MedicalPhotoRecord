package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

/**
 * ViewAddedPhotoActivity
 * Used in add record
 * Show in a grid view all temporary saved photos
 * @version 2.0
 * @see Photo
 * @see com.cmput301f18t20.medicalphotorecord.Record
 */
public class ViewAddedPhotoActivity extends AppCompatActivity {

    private GridView photosGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_added_photo);

        // Grid view images of temp photos
        this.photosGridView = findViewById(R.id.view_added_temp_photos_grid_id);
        this.photosGridView.setAdapter(new ImageAdapter(this, "", "temp"));

    }
}
