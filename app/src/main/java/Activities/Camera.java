package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import Controllers.PhotoController;
import Exceptions.PhotoTooLargeException;

public class Camera extends AppCompatActivity {

    private Button cameraButton;
    private ImageView cameraImage;
    private String recordUUID;
    private String bodyLocation;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraButton = findViewById(R.id.camera_button_id);
        cameraImage = findViewById(R.id.camera_image_view_id);

        this.recordUUID = "tobedonelater";
        this.bodyLocation = "fronthead";
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Check if camera button is clicked
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Get the bitmap and shows to image view
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        this.cameraImage.setImageBitmap(bitmapCompressed);


        // Try to save to database
        try {
            this.photo = new Photo(this.recordUUID, this.bodyLocation, bitmapCompressed);
            new PhotoController().savePhoto(Camera.this, this.photo);
            Toast.makeText(Camera.this, "Your photo have been saved", Toast.LENGTH_LONG).show();
        } catch (PhotoTooLargeException e) {
            Toast.makeText(Camera.this, "Your photo size is too big >65536 bytes", Toast.LENGTH_LONG).show();
        }

    }
}
