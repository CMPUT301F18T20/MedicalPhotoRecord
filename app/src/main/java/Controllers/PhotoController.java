package Controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.lang.reflect.Array;
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

    public ArrayList<Bitmap> getBitmapsForProblem(Context context, String problemUUID){

        ArrayList<Photo> photos = getPhotos(context);
        ArrayList<Bitmap> bitmapsOfProblem = new ArrayList<>();

        // Loop through all photos and get bitmap for ones with same problemUUID
        for (Photo p:photos){
            if (problemUUID.equals(p.getProblemUUID())){
                bitmapsOfProblem.add(p.getBitmapFromString());
            }
        }

        return bitmapsOfProblem;
    }

    public ArrayList<Bitmap> getBodyBitmapsForRecord(Context context, String recordUUID){

        ArrayList<Photo> photos = getPhotos(context);
        ArrayList<Bitmap> bodyBitmapsOfRecord = new ArrayList<>();

        // Loop through all photos and get bitmap for ones with same problemUUID
        for (Photo p:photos){
            if (p.getBodyLocation().length() != 0){
                bodyBitmapsOfRecord.add(p.getBitmapFromString());
            }
        }

        return bodyBitmapsOfRecord;
    }

    public void saveAddPhoto(Context context, Photo photo, String mode) throws TooManyPhotosForSinglePatientRecord {

        // Check if there are more than 10 photos for a record
        ArrayList<Photo> checkPhotos = getPhotosForRecord(context, photo.getRecordUUID());
        ArrayList<Photo> tempPhotos = new OfflineLoadController().loadTempPhotoList(context);
        if (checkPhotos.size() + tempPhotos.size() >= 10) {
            throw new TooManyPhotosForSinglePatientRecord();
        }

        // Actually saving the photo to database
        if (mode == "actualSave") {

            // Online


            // Offline
            ArrayList<Photo> photos = new OfflineLoadController().loadPhotoList(context);
            photos.add(photo);
            new OfflineSaveController().savePhotoList(photos, context);
        }

        // Temporary storage for later saving, after the record is fully created
        if (mode == "tempSave") {
            tempPhotos.add(photo);
            new OfflineSaveController().saveTempPhotoList(tempPhotos, context);
        }

    }
    public ArrayList<Photo> loadTempPhotos(Context context){
        ArrayList<Photo> photos = new OfflineLoadController().loadTempPhotoList(context);
        return photos;
    }

    public ArrayList<Bitmap> getBitmapsFromTemp(Context context){
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        ArrayList<Photo> photos = loadTempPhotos(context);
        for(Photo photo:photos){
            bitmapList.add(photo.getBitmapFromString());
        }
        return bitmapList;
    }

    public void clearTempPhotos(Context context){
        ArrayList<Photo> tempPhotos = new ArrayList<>();
        new OfflineSaveController().saveTempPhotoList(tempPhotos, context);
    }

    public void saveTempPhotosToDatabase(Context context, String recordUUID){

        // Add all temporary photos to actual photo database
        ArrayList<Photo> tempPhotos = new OfflineLoadController().loadTempPhotoList(context);
        for (Photo p:tempPhotos){
            try {
                p.setRecordUUID(recordUUID);
                saveAddPhoto(context, p, "actualSave");
            } catch (TooManyPhotosForSinglePatientRecord tooManyPhotosForSinglePatientRecord) {
                tooManyPhotosForSinglePatientRecord.printStackTrace();
            }
        }

        // Clear temp photo file
        clearTempPhotos(context);
    }
}
