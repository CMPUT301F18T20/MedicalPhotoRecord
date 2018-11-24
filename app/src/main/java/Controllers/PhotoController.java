package Controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.cmput301f18t20.medicalphotorecord.Photo;

import java.util.ArrayList;

public class PhotoController {

    public void savePhoto(Context context, Photo photo){

        // Online

        // Offline
        ArrayList<Photo> photos = new OfflineLoadController().loadPhotoList(context);
        photos.add(photo);
        new OfflineSaveController().savePhotoList(photos, context);
    }
}
