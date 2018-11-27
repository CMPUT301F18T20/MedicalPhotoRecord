package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.OfflineLoadController;

public class BrowseRecordPhotosActivity extends AppCompatActivity {

    private GridView photosGridView;
    private String recordUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_record_photos);

        // Get record uuid
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");

        // Pass recordUUID to adapter and set grid views to all photos for that specific record
        this.photosGridView = findViewById(R.id.browse_records_photos_grid_id);
        this.photosGridView.setAdapter(new ImageAdapter(BrowseRecordPhotosActivity.this, this.recordUUID));
    }

    @Override
    protected void onResume(){
        super.onResume();

        // If an imaged is clicked
        this.photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BrowseRecordPhotosActivity.this, "You selected an image", Toast.LENGTH_LONG).show();
            }
        });

    }
}
