/*
 * Class name: SetBodyPhotoController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/1/18 1:55 PM
 *
 * Last Modified: 12/1/18 1:55 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Photo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SetBodyPhotoController {

    public void setOldDisplayPhotoToNotBeDisplayed(Context context, String recordUUID, String mode){

        // Online
        try {
            ArrayList<Photo> onlinePhotos = new ElasticsearchPhotoController.GetPhotosByRecordUUIDTask().execute(recordUUID).get();
            if (onlinePhotos.size() != 0){
                for (Photo p:onlinePhotos){
                    if (p.getIsViewedBodyPhoto().equals(mode)){

                        // If found old displayed photo, delete it from online database, set "", re add it to online database
                        String oldPhotoUUID = p.getUUID();
                        new ElasticsearchPhotoController.DeletePhotosTask().execute(oldPhotoUUID).get();
                        p.setIsViewedBodyPhoto("");
                        new ElasticsearchPhotoController.AddPhotoTask().execute(p).get();
                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        // Re add all old photos into new list except for old displayed photo, need to setIsViewedBodyPhoto to "" before re adding it
        ArrayList<Photo> oldOfflinePhotos = new OfflineLoadController().loadPhotoList(context);
        ArrayList<Photo> newOfflinePhotos = new ArrayList<>();

        if (oldOfflinePhotos.size() != 0){
            for (Photo p:oldOfflinePhotos){
                if (p.getRecordUUID().equals(recordUUID) && p.getIsViewedBodyPhoto().equals(mode)){
                    p.setIsViewedBodyPhoto("");
                    newOfflinePhotos.add(p);
                }
                newOfflinePhotos.add(p);
            }
            new OfflineSaveController().savePhotoList(newOfflinePhotos, context);
        }

    }

    public void setNewClickedPhotoToBeDisplayed(Context context, String recordUUID, String mode, Photo photo) throws ExecutionException, InterruptedException {

        // Online
        // Delete old photo object, re add modified photo object with isViewedBodyPhoto to either "front" or "back"
        String oldPhotoUUID = photo.getUUID();
        new ElasticsearchPhotoController.DeletePhotosTask().execute(oldPhotoUUID).get();
        photo.setIsViewedBodyPhoto(mode);
        new ElasticsearchPhotoController.AddPhotoTask().execute(photo).get();

        // Offline
        // Re add all old photos into new list except for new displayed photo, need to setIsViewedBodyPhoto to "front" or "back" before re adding it
        ArrayList<Photo> oldOfflinePhotos = new OfflineLoadController().loadPhotoList(context);
        ArrayList<Photo> newOfflinePhotos = new ArrayList<>();

        if (oldOfflinePhotos.size() != 0){
            for (Photo p:oldOfflinePhotos){
                if (p.getRecordUUID().equals(recordUUID) && p.getUUID().equals(oldPhotoUUID)){
                    p.setIsViewedBodyPhoto(mode);
                    newOfflinePhotos.add(p);
                }
                newOfflinePhotos.add(p);
            }
            new OfflineSaveController().savePhotoList(newOfflinePhotos, context);
        }
    }
}
