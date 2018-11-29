package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import Controllers.ModifyPatientRecordController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

public class BrowseBodyLocationPhotosActivity extends AppCompatActivity {

    private GridView photosGridView;
    private String recordUUID;
    private String problemUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_body_location_photos);

        // Get record uuid, problem uuid
        Intent intent = getIntent();
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        Record record =  new ModifyPatientRecordController().getPatientRecord(this,this.recordUUID);
        this.problemUUID = record.getAssociatedProblemUUID();

        // Pass recordUUID to adapter and set grid views to all photos for that specific record
        this.photosGridView = findViewById(R.id.browse_records_body_photos_grid_id);
        this.photosGridView.setAdapter(new ImageAdapter(this, this.recordUUID, "body"));
    }

    @Override
    protected void onResume(){
        super.onResume();

        // If a body location imaged is clicked
        this.photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void onAddBodyPhotoClick(View v){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        intent.putExtra("ISBODYLOCATION", "true");
        startActivity(intent);
    }
}
