package Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.OfflineBodyLocationController;

public class test extends AppCompatActivity {

    ImageView img;
    Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        img = (ImageView)findViewById(R.id.image);
        ArrayList<Photo> photos = new OfflineBodyLocationController().loadBodyPhotos(this);
        Photo photo = photos.get(0);
        Bitmap bitmap = photo.getBitmapFromString();
        img.setImageBitmap(bitmap);

    }
}
