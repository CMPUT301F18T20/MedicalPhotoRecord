package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import Controllers.ModifyPatientRecordController;
import Controllers.PhotoController;
import Exceptions.NoSuchRecordException;

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
        Record record = null;
        try {
            record = new ModifyPatientRecordController().getPatientRecord(this,this.recordUUID);
        } catch (NoSuchRecordException e) {
            Toast.makeText(this,"Record does not exist",Toast.LENGTH_LONG).show();
        }
        this.problemUUID = record.getAssociatedProblemUUID();

        // Pass recordUUID to adapter and set grid views to all photos for that specific record
        this.photosGridView = findViewById(R.id.browse_records_body_photos_grid_id);
        this.photosGridView.setAdapter(new ImageAdapter(this, this.recordUUID, "body"));

        // Long click context menu
        registerForContextMenu(this.photosGridView);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // If a body location image is clicked
        this.photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.browse_records_body_photos_grid_id){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.browse_body_photos_menu,menu); ///
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int longClickPos = info.position;
        switch(item.getItemId()){
            case R.id.delete_body_photo:
                Toast.makeText(this,"delete body photo",Toast.LENGTH_LONG).show();
                new PhotoController().deleteBodyPhoto(this,this.recordUUID,longClickPos);

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onAddBodyPhotoClick(View v){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra("PATIENTRECORDIDEXTRA", this.recordUUID);
        intent.putExtra("BODYLOCATION", "head");
        intent.putExtra("ISADDRECORDACTIVITY","false");
        startActivity(intent);
    }
}
