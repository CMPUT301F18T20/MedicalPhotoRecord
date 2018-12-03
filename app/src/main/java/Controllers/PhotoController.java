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

/**
 * PhotoController
 * Can get photos for a record
 * Can get photos for a problem
 * Can get body photos for a record
 * Can get bitmaps for a photos list
 * Can get labels for a photos list
 * Can add photo online and offline
 * Can delete photo online and offline
 * Can load temporary photos list
 * Can clear temporary photos list
 * Can save temporary photo list
 * @version 2.0
 * @see Photo
 * @see Record
 */
public class PhotoController {

    /**
     * Get photos list for a specific record depending on online or offline
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     * @return array list of photos
     */
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

    /**
     * Get photo list for a specific problem depending on online or offline
     * @param context: activity to be passed for offline save and load
     * @param problemUUID
     * @return array list of photos
     */
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

    /**
     * Get body location photos for a specific record
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     * @return array list of photos
     */
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

    /**
     * Get bitmaps for an input photo list
     * @param context: activity to be passed for offline save and load
     * @param photos: list of photo objects
     * @return array list of bitmaps
     */
    public ArrayList<Bitmap> getBitMapsForPhotoList(Context context, ArrayList<Photo> photos){

        ArrayList<Bitmap> bitmapsForPhotos = new ArrayList<>();

        // Get bitmap for every photo in the list
        for (Photo p:photos){
            bitmapsForPhotos.add(p.getBitmapFromString());
        }

        return bitmapsForPhotos;
    }

    /**
     * Get label list of input photo list
     * @param context: activity to be passed for offline save and load
     * @param photos
     * @return array list of label string
     */
    public ArrayList<String> getLabelsForPhotoList(Context context, ArrayList<Photo> photos){

        ArrayList<String> labelsForPhotos = new ArrayList<>();

        // Get label string for every photo in the list
        for (Photo p:photos){
            labelsForPhotos.add(p.getLabel());
        }

        return labelsForPhotos;
    }

    /**
     * Depending on the mode, save photo to database
     * + Actual save: for body location photo when browsing body location photo
     * + Temp save: for body location photo and normal photo in add record or modify record activity
     * @param context: activity to be passed for offline save and load
     * @param photo
     * @param mode: actual saving to database or just temporary save
     * @throws TooManyPhotosForSinglePatientRecord
     */
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

    /**
     * @param context: activity to be passed for offline save and load
     * @return list of temporary photos
     */
    public ArrayList<Photo> loadTempPhotos(Context context){
        ArrayList<Photo> photos = new OfflineLoadController().loadTempPhotoList(context);
        return photos;
    }

    /**
     * Clear temporary photo database
     * @param context: activity to be passed for offline save and load
     */
    public void clearTempPhotos(Context context){
        ArrayList<Photo> tempPhotos = new ArrayList<>();
        new OfflineSaveController().saveTempPhotoList(tempPhotos, context);
    }

    /**
     * Save all photos from temporary database to actual database
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     */
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

    /**
     * Delete a body location photo online and offline
     * @param context: activity to be passed for offline save and load
     * @param recordUUID
     * @param position: position to get the actual photo to be deleted from array list
     */
    public void deleteBodyPhoto(Context context, String recordUUID, int position){
        ArrayList<Photo> bodyPhotos = getBodyPhotosForRecord(context,recordUUID);
        Photo selectedBodyPhoto = bodyPhotos.get(position);
        String selectedBodyPhotoUUID = selectedBodyPhoto.getUUID();
        // Online delete
        try {
            new ElasticsearchPhotoController.DeletePhotosTask().execute(selectedBodyPhotoUUID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Offline delete
        ArrayList<Photo> offlinePhotos = new OfflineLoadController().loadPhotoList(context);
        for (Photo p:new ArrayList<>(offlinePhotos)){
            if(p.getUUID().equals(selectedBodyPhotoUUID)){
                offlinePhotos.remove(p);
            }
        }
        new OfflineSaveController().savePhotoList(offlinePhotos,context);
    }
}
