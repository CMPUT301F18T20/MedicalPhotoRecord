package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.DrawBodyLocationController;
import Controllers.OfflineBodyLocationController;
import Exceptions.PhotoTooLargeException;


public class DrawBodyLocationActivity extends AppCompatActivity {
    protected ImageView imgDraw;
    protected Button saveButton;
    private int chosen_area;
    private int mode;

    private String userID, problemUUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_body_location);
        imgDraw = (ImageView)findViewById(R.id.draw_layer);
        saveButton = (Button)findViewById(R.id.draw_save);

        Intent intent = getIntent();
        this.chosen_area = intent.getIntExtra("CHOSENBODYPART",0);
        this.mode = intent.getIntExtra("MODE",0);

        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra("PROBLEMIDEXTRA");

        imgDraw.setImageResource(this.chosen_area);

    }

    public void onDoneClick(View view){
        //TODO retrieve contents of imgDraw after user has drawn an X and pass it to AddRecordActivity to set
        if (this.mode == 1){
            Intent intent = new Intent(this,test.class);

            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            Bitmap imgBitmap = new DrawBodyLocationController().getBitmapFromImageView(imgDraw);
            //Log.d("wow","front");
            Photo frontBodyPhoto= new DrawBodyLocationController().createPhotoInstance(imgBitmap,"front");
            ArrayList<Photo> photos = new ArrayList<>();
            photos.add(frontBodyPhoto);
            new OfflineBodyLocationController().saveBodyPhoto(this,photos);
            startActivity(intent);

            //new OfflineBodyLocationController().saveBodyPhoto(this,frontBodyPhoto);

            startActivity(intent);
        }
        else if (this.mode == 2){
            Intent intent = new Intent(this,AddRecordActivity.class);

            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            Bitmap imgBitmap = new DrawBodyLocationController().getBitmapFromImageView(imgDraw);
            Log.d("wow","back");
            Photo backBodyPhoto = new DrawBodyLocationController().createPhotoInstance(imgBitmap,"back");

           // new OfflineBodyLocationController().saveBodyPhoto(this,backBodyPhoto);

            startActivity(intent);
        }

    }
}
