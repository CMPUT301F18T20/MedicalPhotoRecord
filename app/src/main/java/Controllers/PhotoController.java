package Controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.cmput301f18t20.medicalphotorecord.Photo;

import java.util.ArrayList;

import Exceptions.TooManyPhotosForSinglePatientRecord;

public class PhotoController {

    public ArrayList<Photo> getPhotos(Context context){

        // Online


        // Offline
        ArrayList<Photo> offlinePhotos = new OfflineLoadController().loadPhotoList(context);

        // syncing
        ArrayList<Photo> actualPhotos = offlinePhotos;
        return actualPhotos;
    }

    public ArrayList<Photo> getPhotosForRecord(Context context, String recordUUID){

        ArrayList<Photo> photos = getPhotos(context);
        ArrayList<Photo> photosOfRecord = new ArrayList<>();

        // Loop through all photos and get photo with same recordUUID
        for (Photo p:photos){
            if (recordUUID.equals(p.getRecordUUID())){
                photosOfRecord.add(p);
            }
        }
        return photosOfRecord;
    }

    public ArrayList<Bitmap> getBitMapsForRecord(Context context, String recordUUID){

        ArrayList<Photo> photos = getPhotos(context);
        ArrayList<Bitmap> bitmapsOfRecord = new ArrayList<>();

        // Loop through all photos and get bitmap for ones with same recordUUID
        for (Photo p:photos){
            if (recordUUID.equals(p.getRecordUUID())){
                bitmapsOfRecord.add(p.getBitmapFromString());
            }
        }

        return bitmapsOfRecord;
    }

    public void saveAddPhoto(Context context, Photo photo) throws TooManyPhotosForSinglePatientRecord {

        // Check if there are more than 10 photos for a record
        ArrayList<Photo> checkPhotos = getPhotosForRecord(context, photo.getRecordUUID());
        if (checkPhotos.size() >= 10){
            throw new TooManyPhotosForSinglePatientRecord();
        }

        // Online


        // Offline
        ArrayList<Photo> photos = new OfflineLoadController().loadPhotoList(context);
        photos.add(photo);
        new OfflineSaveController().savePhotoList(photos, context);
    }
}
