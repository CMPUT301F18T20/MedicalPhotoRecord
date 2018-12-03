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
 * Testing for method (getPhotoForRecord, getPhotoForProblem, getBodyPhotoForRecord) in PhotoControllerTest
 * @version 1.0
 * @see PhotoController
 */
public class PhotoControllerTest {

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
     * Insert photos into database for later testing
     * @return array list of photos
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
     * Test get correct photos for a record
     * @throws PhotoTooLargeException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testGetPhotosForRecord() throws PhotoTooLargeException, ExecutionException, InterruptedException {

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Photo> allPhotos = insertPhotoIntoDatabase();

        // Get expected record photo
        ArrayList<Photo> expectedRecordPhotos = new ArrayList<>();
        for (Photo p:allPhotos){
            if (p.getRecordUUID().equals("recordID1")){
                expectedRecordPhotos.add(p);
            }
        }

        // Test
        ArrayList<Photo> gotRecordPhotos = new PhotoController().getPhotosForRecord(context,"recordID1");

        // Compare
        for (int i = 0; i < expectedRecordPhotos.size(); i++){
            String p1 = new Gson().toJson(expectedRecordPhotos.get(i));
            String p2 = new Gson().toJson(gotRecordPhotos.get(i));
            assertEquals("compare each record photo", p1,p2);

        }

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();
    }


    /**
     * Test correctly get all photos for all records for a problem
     * @throws PhotoTooLargeException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testGetPhotosForProblem() throws PhotoTooLargeException, ExecutionException, InterruptedException {

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Photo> allPhotos = insertPhotoIntoDatabase();

        // Get expected record photo
        ArrayList<Photo> expectedPhotos = new ArrayList<>();
        for (Photo p:allPhotos){
            if (p.getProblemUUID().equals("problemID1")){
                expectedPhotos.add(p);
            }
        }

        // Test
        ArrayList<Photo> gotPhotos = new PhotoController().getPhotosForProblem(context, "problemID1");

        // Compare
        for (int i = 0; i < expectedPhotos.size(); i++){
            String p1 = new Gson().toJson(expectedPhotos.get(i));
            String p2 = new Gson().toJson(gotPhotos.get(i));
            assertEquals("compare each problem record photo", p1,p2);

        }

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();
    }


    /**
     * Test if correctly get all body location photos for a specific record
     * @throws PhotoTooLargeException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testGetBodyPhotosForRecord() throws PhotoTooLargeException, ExecutionException, InterruptedException {

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Photo> allPhotos = insertPhotoIntoDatabase();

        // Get expected record photo
        ArrayList<Photo> expectedPhotos = new ArrayList<>();
        for (Photo p : allPhotos) {
            if (p.getBodyLocation().equals("bodyLocation")) {
                expectedPhotos.add(p);
            }
        }

        // Test
        ArrayList<Photo> gotPhotos = new PhotoController().getBodyPhotosForRecord(context, "recordID");

        // Compare
        for (int i = 0; i < expectedPhotos.size(); i++) {
            String p1 = new Gson().toJson(expectedPhotos.get(i));
            String p2 = new Gson().toJson(gotPhotos.get(i));
            assertEquals("compare each body photo", p1, p2);

        }

        // Wipe database
        WipeOnlineDatabase();
        wipeOfflineDatabase();
    }
}
