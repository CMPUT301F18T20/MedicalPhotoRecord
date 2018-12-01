package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.OfflineSaveController;
import Controllers.PhotoController;
import Exceptions.PhotoTooLargeException;
import Exceptions.TooManyPhotosForSinglePatientRecord;
import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;
import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

public class CameraActivity extends AppCompatActivity {
    private Button cameraButton;
    private ImageView cameraImage;
    private EditText labelEditView;
    private String recordUUID;
    private String problemUUID;
    private String bodyLocation;
    private String label;
    private Photo photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraButton = findViewById(R.id.camera_button_id);
        cameraImage = findViewById(R.id.camera_image_view_id);
        labelEditView = findViewById(R.id.label_edit_id);

        Intent intent = getIntent();
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        this.recordUUID = intent.getStringExtra("PATIENTRECORDIDEXTRA");
        this.bodyLocation = intent.getStringExtra("BODYLOCATION");  //normal photo "" vs bodylocation photo "..."
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Check if camera button is clicked
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label = labelEditView.getText().toString();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // If camara button is clicked
        // Get the bitmap, compressed it and shows to image view
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        this.cameraImage.setImageBitmap(bitmapCompressed);

        // Try to save to database
        try {

            // Check if it's a body location photo
            if (this.bodyLocation.length() != 0){
                this.photo = new Photo(this.recordUUID, this.problemUUID, this.bodyLocation, bitmapCompressed, label);
            }else{
                this.photo = new Photo(this.recordUUID, this.problemUUID, this.bodyLocation, bitmapCompressed, "");
            }

            // Save temporary to database
            new PhotoController().saveAddPhoto(CameraActivity.this, this.photo, "tempSave");
            Toast.makeText(CameraActivity.this, "Your photo has been saved temporary. If you don't save the record, this photo will not be saved." + this.photo.getLabel(), Toast.LENGTH_LONG).show();

        } catch (PhotoTooLargeException e) {
            Toast.makeText(CameraActivity.this, "Your photo size is too big >65536 bytes", Toast.LENGTH_LONG).show();
        } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
            Toast.makeText(CameraActivity.this, "You have more than 10 photos for this record", Toast.LENGTH_LONG).show();
        }

        finish();
    }
}