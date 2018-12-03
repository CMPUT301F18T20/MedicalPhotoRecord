package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import Controllers.DrawBodyLocationController;
import Controllers.PhotoController;
import Exceptions.PhotoTooLargeException;
import Exceptions.TooManyPhotosForSinglePatientRecord;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

/**
 * DrawBodyLocationActivity
 * Used in add record
 * Allow patient to save body location photo that they draw on
 * @version 2.0
 * @see com.cmput301f18t20.medicalphotorecord.Record
 */
public class DrawBodyLocationActivity extends AppCompatActivity {
    protected ImageView imgDraw;
    protected Button saveButton;
    private int chosen_area;
    private int mode;

    private String userID
            ,problemUUID
            ,bodylocation;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_body_location);
        imgDraw = (ImageView)findViewById(R.id.draw_layer);
        saveButton = (Button)findViewById(R.id.draw_save);

        Intent intent = getIntent();
        this.chosen_area = intent.getIntExtra("CHOSENBODYPART",0);
        this.mode = intent.getIntExtra("MODE",0);
        this.bodylocation = intent.getStringExtra("BODYLOCATION");

        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra("PROBLEMIDEXTRA");

        imgDraw.setImageResource(this.chosen_area);

    }

    /**
     * Temporary save stock body location photo that user chose
     * @param view
     */
    public void onDoneClick(View view) {
        //TODO retrieve contents of imgDraw after user has drawn an X and pass it to AddRecordActivity to set
        if (this.mode == 1){
            Intent intent = new Intent(this,BackBodyLocationActivity.class);

            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);

            Bitmap bitmap = new DrawBodyLocationController().createBitmapFromImage(imgDraw);
            Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
            Photo photo = null;
            try {
                //TODO recordUUIDs!!!!
                photo = new Photo("",this.problemUUID,this.bodylocation,bitmapCompressed,"");
            } catch (PhotoTooLargeException e) {
                Log.d("DrawBodyLocation","Photo is too large.");
                e.printStackTrace();
            }
            photo.setIsViewedBodyPhoto("front");
            try {
                new PhotoController().saveAddPhoto(this,photo,"tempSave");
            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                Log.d("DrawBodyLocation","Too many photos for record");
                tooManyPhotosForSinglePatientRecord.printStackTrace();
            }
            startActivity(intent);
        }
        else if (this.mode == 2){
            Intent intent = new Intent(this,AddRecordActivity.class);

            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            Bitmap bitmap = new DrawBodyLocationController().createBitmapFromImage(imgDraw);
            Bitmap bitmapCompressed = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
            Photo photo = null;
            try {
                //TODO recordsUUIDs
                photo = new Photo("",this.problemUUID,this.bodylocation,bitmapCompressed,"");
            } catch (PhotoTooLargeException e) {
                Log.d("DrawBodyLocation","Photo is too large.");
                e.printStackTrace();
            }
            photo.setIsViewedBodyPhoto("back");
            try {
                new PhotoController().saveAddPhoto(this,photo,"tempSave");
            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                Log.d("DrawBodyLocation","Too many photos for record");
                tooManyPhotosForSinglePatientRecord.printStackTrace();
            }
            startActivity(intent);
        }
    }

    /**
     * If user wants to add optional body location photo (go to camera activity)
     * @param views
     */
    public void onAddBodyPhotos(View views){

        Intent intent = new Intent(this,CameraActivity.class);
        intent.putExtra(PROBLEMIDEXTRA, this.problemUUID);
        intent.putExtra("PATIENTRECORDIDEXTRA","");
        intent.putExtra("BODYLOCATION",this.bodylocation);
        intent.putExtra("ISADDRECORDACTIVITY","true");
        startActivity(intent);

    }
}
