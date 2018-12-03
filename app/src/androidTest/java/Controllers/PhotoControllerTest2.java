/*
 * Class name: PhotoControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/2/18 8:55 PM
 *
 * Last Modified: 12/2/18 8:55 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddProblemActivity;
import Enums.INDEX_TYPE;
import Exceptions.PhotoTooLargeException;
import Exceptions.TooManyPhotosForSinglePatientRecord;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * PhotoControllerTest
 * Testing for method (add, delete photo in actual database, temp functionality) in PhotoControllerTest
 * @version 1.0
 * @see PhotoController
 */
public class PhotoControllerTest2 {

    /**
     * Clear out online photo database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchPhotoController.DeletePhotosTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    /**
     * Clear out offline photo database
     */
    @After
    @Before
    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Photo> emptyPhotos = new ArrayList<>();
        new OfflineSaveController().savePhotoList(emptyPhotos,context);
    }

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);


    /**
     * Insert photos for later testing
     * @return array list of photo
     * @throws PhotoTooLargeException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ArrayList<Photo> insertPhotoIntoDatabase() throws PhotoTooLargeException, ExecutionException, InterruptedException {
        Context context = AddProblemActivity.getActivity().getBaseContext();


        // Get bitmap
        InputStream is = context.getResources().openRawResource(R.drawable.testphoto);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);

        // Create photos
        Photo recordPhoto1 = new Photo("recordID1", "problemID", "", bitmap, "");
        Photo recordPhoto2 = new Photo("recordID1", "problemID", "", bitmap, "");
        Photo problemPhoto1 = new Photo("recordID", "problemID1", "", bitmap, "");
        Photo problemPhoto2 = new Photo("recordID", "problemID1", "", bitmap, "");
        Photo bodyPhoto1 = new Photo("recordID", "problemID", "bodyLocation", bitmap, "label");
        Photo bodyPhoto2 = new Photo("recordID", "problemID", "bodyLocation", bitmap, "label");

        // Insert photos online
        new ElasticsearchPhotoController.AddPhotoTask().execute(recordPhoto1).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPhotoController.AddPhotoTask().execute(recordPhoto2).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPhotoController.AddPhotoTask().execute(problemPhoto1).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPhotoController.AddPhotoTask().execute(problemPhoto2).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPhotoController.AddPhotoTask().execute(bodyPhoto1).get();
        Thread.sleep(ControllerTestTimeout);
        new ElasticsearchPhotoController.AddPhotoTask().execute(bodyPhoto2).get();
        Thread.sleep(ControllerTestTimeout);

        // Insert photos offline
        ArrayList<Photo> offlinePhotos = new ArrayList<>();
        offlinePhotos.add(recordPhoto1);
        offlinePhotos.add(recordPhoto2);
        offlinePhotos.add(problemPhoto1);
        offlinePhotos.add(problemPhoto2);
        offlinePhotos.add(bodyPhoto1);
        offlinePhotos.add(bodyPhoto2);
        new OfflineSaveController().savePhotoList(offlinePhotos,context);

        return offlinePhotos;

    }

    /**
     * Test if photo is added to database online, offline, temporary
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws PhotoTooLargeException
     * @throws TooManyPhotosForSinglePatientRecord
     */
    @Test
    public void testSaveAddPhoto() throws ExecutionException, InterruptedException, PhotoTooLargeException, TooManyPhotosForSinglePatientRecord {

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Get bitmap
        InputStream is = context.getResources().openRawResource(R.drawable.testphoto);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);

        // Create photo
        Photo expectedPhoto = new Photo("recordID1", "problemID", "", bitmap, "");

        // Test
        new PhotoController().saveAddPhoto(context, expectedPhoto, "actualSave");
        new PhotoController().saveAddPhoto(context, expectedPhoto, "tempSave");

        // Compare
        Photo gotOnlinePhoto = (new ElasticsearchPhotoController.GetPhotosByRecordUUIDTask().execute("recordID1").get()).get(0);
        Photo gotOfflinePhoto = (new OfflineLoadController().loadPhotoList(context)).get(0);
        Photo gotOfflinePhotoTemp = (new OfflineLoadController().loadTempPhotoList(context)).get(0);

        String p1 = new Gson().toJson(expectedPhoto);
        String p2 = new Gson().toJson(gotOnlinePhoto);
        String p3 = new Gson().toJson(gotOfflinePhoto);
        String p4 = new Gson().toJson(gotOfflinePhotoTemp);
        assertEquals("compare added photo online", p1,p2);
        assertEquals("compare added photo offline", p1,p3);
        assertEquals("compare added photo offline temp", p1,p4);

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();
    }

    /**
     * Test for temporary functionality: clear, load, save temp to actual database
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws PhotoTooLargeException
     * @throws TooManyPhotosForSinglePatientRecord
     */
    @Test
    public void testTempFunctionality() throws ExecutionException, InterruptedException, PhotoTooLargeException, TooManyPhotosForSinglePatientRecord {

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Get bitmap
        InputStream is = context.getResources().openRawResource(R.drawable.testphoto);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);

        // Create photo
        Photo photo1 = new Photo("recordUUID", "problemID", "", bitmap, "");
        Photo photo2 = new Photo("recordUUID", "problemID1", "bodylocation", bitmap, "label");
        ArrayList<Photo> tempPhotos = new ArrayList<>();
        tempPhotos.add(photo1);
        tempPhotos.add(photo2);
        new OfflineSaveController().saveTempPhotoList(tempPhotos,context);

        // Test
        ArrayList<Photo> gotTempPhotos = new PhotoController().loadTempPhotos(context);
        new PhotoController().saveTempPhotosToDatabase(context, "recordUUID");

        // Compare
        ArrayList<Photo> onlinePhotos = new ElasticsearchPhotoController.GetPhotosByRecordUUIDTask().execute("recordUUID").get();
        ArrayList<Photo> offlinePhots = new OfflineLoadController().loadPhotoList(context);

        for (int i = 0; i < tempPhotos.size(); i++){
            String p1 = new Gson().toJson(tempPhotos.get(i));
            String p2 = new Gson().toJson(onlinePhotos.get(i));
            String p3 = new Gson().toJson(offlinePhots.get(i));
            assertEquals("compare each temp photo", p1,p2);
            assertEquals("compare each temp photo", p1,p3);
        }

        // Test clear
        new PhotoController().clearTempPhotos(context);
        ArrayList<Photo> tempPhotosCleared = new OfflineLoadController().loadTempPhotoList(context);
        assertEquals("compare each temp photo", 0,tempPhotosCleared.size());

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

    }


    /**
     * Test delete photo from online, offline database
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws PhotoTooLargeException
     * @throws TooManyPhotosForSinglePatientRecord
     */
    @Test
    public void testDeletePhoto() throws ExecutionException, InterruptedException, PhotoTooLargeException, TooManyPhotosForSinglePatientRecord {

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Get expected delete photo
        ArrayList<Photo> photos = insertPhotoIntoDatabase();
        Photo photo = null;
        for (Photo p:photos){
            if (p.getRecordUUID().equals("recordID")){
                photo = p;
                break;
            }
        }

        // Test
        new PhotoController().deleteBodyPhoto(context,"recordID",0);

        // Compare
        ArrayList<Photo> onlinePhotos = new ElasticsearchPhotoController.GetPhotosByRecordUUIDTask().execute("recordID1").get();
        ArrayList<Photo> offlinePhots = new OfflineLoadController().loadPhotoList(context);

        for (int i = 0; i < onlinePhotos.size(); i++){
            if (onlinePhotos.get(i).getUUID().equals(photo.getUUID())){
                assertTrue("online photo not deleted", false);
            }
        }

        for (int i = 0; i < offlinePhots.size(); i++){
            if (offlinePhots.get(i).getUUID().equals(photo.getUUID())){
                assertTrue("offline photo not deleted", false);
            }
        }


        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

    }







}
