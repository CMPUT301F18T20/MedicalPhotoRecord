package Controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.TooManyPhotosForSinglePatientRecord;

public class PhotoController {

    // GET
    public ArrayList<Photo> getPhotosForRecord(Context context, String recordUUID){

        // Online
        ArrayList<Photo> onlineRecordPhotos = new ArrayList<>();
        try {
            onlineRecordPhotos = new ElasticsearchPhotoController.GetPhotosByRecordUUIDTask().execute(recordUUID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        ArrayList<Photo> offlinePhotos = new OfflineLoadController().loadPhotoList(context);
        ArrayList<Photo> offlineRecordPhotos = new ArrayList<>();

        // Loop through all photos and get photo with same recordUUID
        for (Photo p:offlinePhotos){
            if (recordUUID.equals(p.getRecordUUID())){
                offlineRecordPhotos.add(p);
            }
        }

        // Syncing
        ArrayList<Photo> actualRecordPhotos = onlineRecordPhotos;
        return actualRecordPhotos;
    }

    public ArrayList<Photo> getPhotosForProblem(Context context, String problemUUID){

        // Online
        ArrayList<Photo> onlineProblemPhotos = new ArrayList<>();
        try {
            onlineProblemPhotos = new ElasticsearchPhotoController.GetPhotosByProblemUUIDTask().execute(problemUUID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        ArrayList<Photo> offlinePhotos = new OfflineLoadController().loadPhotoList(context);
        ArrayList<Photo> offlineProblemPhotos = new ArrayList<>();

        // Loop through all photos and get photo with same problemUUID
        for (Photo p:offlinePhotos){
            if (problemUUID.equals(p.getProblemUUID())){
                offlineProblemPhotos.add(p);
            }
        }

        // Syncing
        ArrayList<Photo> actualProblemPhotos = onlineProblemPhotos;
        return actualProblemPhotos;
    }

    public ArrayList<Photo> getBodyPhotosForRecord(Context context, String recordUUID){

        ArrayList<Photo> recordPhotos = getPhotosForRecord(context, recordUUID);
        ArrayList<Photo> recordBodyPhotos = new ArrayList<>();

        // Check if body location is not empty string (not normal photo)
        for (Photo p:recordPhotos){
            if (p.getBodyLocation().length() > 0){
                recordBodyPhotos.add(p);
            }
        }

        return recordBodyPhotos;
    }

    public ArrayList<Bitmap> getBitMapsForPhotoList(Context context, ArrayList<Photo> photos){

        ArrayList<Bitmap> bitmapsForPhotos = new ArrayList<>();

        // Get bitmap for every photo in the list
        for (Photo p:photos){
            bitmapsForPhotos.add(p.getBitmapFromString());
        }

        return bitmapsForPhotos;
    }

    public ArrayList<String> getLabelsForPhotoList(Context context, ArrayList<Photo> photos){

        ArrayList<String> labelsForPhotos = new ArrayList<>();

        // Get label string for every photo in the list
        for (Photo p:photos){
            labelsForPhotos.add(p.getLabel());
        }

        return labelsForPhotos;
    }

    // SAVE
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
            try {
                new ElasticsearchPhotoController.AddPhotoTask().execute(photo).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

    public ArrayList<Photo> deletePhotoFromPhotoList(Context context, String photoUUID, ArrayList<Photo> photos){

        for (Photo p:new ArrayList<>(photos)){
            if(p.getUUID().equals(photoUUID)){
                photos.remove(p);
            }
        }
        return photos;
    }

}
