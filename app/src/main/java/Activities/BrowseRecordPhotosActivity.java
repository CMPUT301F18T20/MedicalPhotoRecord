package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cmput301f18t20.medicalphotorecord.R;

/**
 * BrowseRecordPhotosActivity
 * This activity is started when browse_record_photos_id button is clicked
 * in the ViewRecordActivity.
 * Displays a collection of photos saved in the chosen record.
 *
 * @version 1.0
 * @since   2018-12-01
 */

public class BrowseRecordPhotosActivity extends AppCompatActivity {

    private GridView photosGridView;
    private String recordUUID;

    /**
     * Populates the adapter with the saved images in the patient record and displays it
     * @param savedInstanceState -
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_record_photos);

        // Get record uuid
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");

        // Pass recordUUID to adapter and set grid views to all photos for that specific record
        this.photosGridView = findViewById(R.id.browse_records_photos_grid_id);
        this.photosGridView.setAdapter(new ImageAdapter(BrowseRecordPhotosActivity.this, this.recordUUID, "normal"));
    }

    /**
     * Click Listener for images
     */
    @Override
    protected void onResume(){
        super.onResume();

        // If an imaged is clicked
        this.photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // View a photo
                Intent intent = new Intent(BrowseRecordPhotosActivity.this, ViewRecordPhotoActivity.class);
                intent.putExtra("pos",position);
                intent.putExtra("PATIENTRECORDIDEXTRA", recordUUID);
                startActivity(intent);
            }
        });

    }
}
