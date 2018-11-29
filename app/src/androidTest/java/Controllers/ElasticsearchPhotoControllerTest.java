package Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddRecordActivity;
import Activities.BrowseUserActivity;
import Enums.INDEX_TYPE;
import Exceptions.PhotoTooLargeException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static Controllers.Utils.nameGen;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class ElasticsearchPhotoControllerTest{

    private String
            recordUUIDinAddTest = "ImFromThePhotoAddTest",
            recordUUIDinGetPhotoByUUIDTest = "ImFromTheGetPhotoByUUIDTest",
            recordUUIDToRetrieveThroughRecordUUID = "ImFromTheGetPhotosByRecordUUIDTest",
            recordUUIDinDeleteTest = "ImFromThePhotoAddTest",
            problemUUIDforTests = "ImTheProblemUUID";


    @Rule
    public final ActivityTestRule<BrowseUserActivity> mainActivity = new ActivityTestRule<>(BrowseUserActivity.class);

    @Before
    @After
    public void WipeDatabase() throws ExecutionException, InterruptedException{
        //ensure index is in testing mode
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        //Delete all photos in database
        new ElasticsearchPhotoController.DeletePhotosTask().execute().get();

        //Ensure time for processing
        Thread.sleep(ControllerTestTimeout);
    }

    @Test
    public void DeletePhotoTask() throws InterruptedException,ExecutionException,
            PhotoTooLargeException{
        //create mock photo object and make sure image is under max byte limit
        Context context = mainActivity.getActivity().getBaseContext();

        InputStream is = context.getResources().openRawResource(R.drawable.testphoto);

        //inSampleSize reduces pixels that are processed and thus reducing the size (but at cost of resolution)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap photoBitmap = BitmapFactory.decodeStream(is,null,options);
        Photo mockPhoto = new Photo(recordUUIDinDeleteTest,problemUUIDforTests,"true",photoBitmap,"front");
        Photo mockPhoto2 = new Photo(recordUUIDinDeleteTest,problemUUIDforTests,"true",photoBitmap,"back");

        //add into database
        new ElasticsearchPhotoController.AddPhotoTask().execute(mockPhoto).get();
        new ElasticsearchPhotoController.AddPhotoTask().execute(mockPhoto2).get();

        //let time for database to make change
        Thread.sleep(ControllerTestTimeout);

        //delete photo in database
        new ElasticsearchPhotoController.DeletePhotosTask().execute(mockPhoto.getUUID()).get();

        //let time for change
        Thread.sleep(ControllerTestTimeout);

        //attempt to get photo out of database
        Photo photo = new ElasticsearchPhotoController.GetPhotoByPhotoUUIDTask().execute(mockPhoto.getUUID()).get();

        assertNull("Photo is still in database", photo);
    }

    @Test
    public void AddPhotoTask() throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        //create mock photo object and make sure image is under max byte limit
        Context context = mainActivity.getActivity().getBaseContext();

        InputStream is = context.getResources().openRawResource(R.drawable.testphoto);

        //inSampleSize reduces pixels that are processed and thus reducing the size (but at cost of resolution)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap photoBitmap = BitmapFactory.decodeStream(is,null,options);
        Photo mockPhoto = new Photo(recordUUIDinDeleteTest,problemUUIDforTests,"true",photoBitmap,"front");

        //add to database
        new ElasticsearchPhotoController.AddPhotoTask().execute(mockPhoto).get();

        //allow time for change
        Thread.sleep(ControllerTestTimeout);

        //fetch photo from database
        Photo fetchedPhoto = new ElasticsearchPhotoController.GetPhotoByPhotoUUIDTask().execute(mockPhoto.getUUID()).get();

        assertEquals("Fetched photo is not equal",mockPhoto.getRecordUUID(),fetchedPhoto.getRecordUUID());
    }

    @Test
    public void GetPhotoByUUIDTaskTest() throws InterruptedException,ExecutionException,
    PhotoTooLargeException{
        //create mock photo object and make sure image is under max byte limit
        Context context = mainActivity.getActivity().getBaseContext();

        InputStream is = context.getResources().openRawResource(R.drawable.testphoto);

        //inSampleSize reduces pixels that are processed and thus reducing the size (but at cost of resolution)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap photoBitmap = BitmapFactory.decodeStream(is,null,options);
        Photo mockPhoto = new Photo(recordUUIDinDeleteTest,problemUUIDforTests,"true",photoBitmap,"front");

        //add to database
        new ElasticsearchPhotoController.AddPhotoTask().execute(mockPhoto).get();

        //allow time for change
        Thread.sleep(ControllerTestTimeout);

        //fetch photo from database
        Photo fetchedPhoto = new ElasticsearchPhotoController.GetPhotoByPhotoUUIDTask().execute(mockPhoto.getUUID()).get();

        assertEquals("Fetched photo is not equal",mockPhoto.getRecordUUID(),fetchedPhoto.getRecordUUID());
    }

    @Test
    public void GetPhotosByRecordUUIDTaskTest()
            throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        AssertPhotosCanBeAddedAndThenBatchFetched(3);
    }

    @Test
    public void GetPhotosByRecordUUIDTaskBUGTest()
            throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        AssertPhotosCanBeAddedAndThenBatchFetched(20);
    }

    private void AssertPhotosCanBeAddedAndThenBatchFetched(int numPhotoTestObjects)
            throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        //create mock photo object and make sure image is under max byte limit
        Context context = mainActivity.getActivity().getBaseContext();


        BitmapFactory.Options options = new BitmapFactory.Options();

        ArrayList<Photo> expectedPhotos = new ArrayList<>();
        ArrayList<Boolean> expectedPhotosInResults = new ArrayList<>();

       //stores byte size of each bitmap for comparison
        ArrayList<String> byteSizeList= new ArrayList<String>();
        //inSampleSize reduces pixels that are processed and thus reducing the size (but at cost of resolution)
        int sampleSize = 2;
        //if even, then bodylocation is not true, if odd then bodylocation is false
        int isBody = 0;
        String label;
        String bodylocation;
        for (int i =0;i<numPhotoTestObjects;i++){
            InputStream is = context.getResources().openRawResource(R.drawable.testphoto);
            options.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
            byteSizeList.add(Integer.toString(bitmap.getByteCount()));

            if ((isBody%2) == 0){
                bodylocation = "true";
                label = "front";
            } else{
                bodylocation = "false";
                label = "";
            }
            //create photo instance
            Photo photo = new Photo(recordUUIDToRetrieveThroughRecordUUID,problemUUIDforTests
                    ,bodylocation,bitmap,label);

            //add to database
            new ElasticsearchPhotoController.AddPhotoTask().execute(photo).get();

            //add new photo object to expected returns
            expectedPhotos.add(photo);
            expectedPhotosInResults.add(false);

            /*increment isBody so bodylocation is different. sampleSize so Byte count in bitmap is different
             */
            isBody++;
            sampleSize += 2;
        }
        //Ensure time for change
        Thread.sleep(ControllerTestTimeout);

        //Make sure each of the added photos can be retrieved individually
        for (int i =0; i<numPhotoTestObjects; i++){
            //fetch photos from ES database
            Photo fetchedPhoto = new ElasticsearchPhotoController.GetPhotoByPhotoUUIDTask()
                    .execute(expectedPhotos.get(i).getUUID()).get();


            assertEquals("Fetched photo has different photoUUID"
                ,expectedPhotos.get(i).getUUID(),fetchedPhoto.getUUID());

            assertEquals("Fetched photo has different bodylocation identifier"
                    ,expectedPhotos.get(i).getBodyLocation(), fetchedPhoto.getBodyLocation());

            assertEquals("Fetched photo has different number of bytes in bitmap"
                ,byteSizeList.get(i).toString()
                    , Integer.toString(fetchedPhoto.getBitmapFromString().getByteCount()));

        }

        //Get objects from database associated with record UUID
        ArrayList<Photo> results = new ElasticsearchPhotoController.GetPhotosByRecordUUIDTask()
                .execute(recordUUIDToRetrieveThroughRecordUUID)
                .get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/161
        if(numPhotoTestObjects> 10 && results.size() == 10){
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/161 " +
                    "there should be as many results as photos we queried. We got exactly " +
                    "ten results instead of expected "
                    + numPhotoTestObjects
                    ,results.size() == numPhotoTestObjects);

        }
        Assert.assertTrue("there should be as many results as patientRecords we queried. We got " +
                        results.size() + " results instead of expected " +
                        numPhotoTestObjects,
                results.size() == numPhotoTestObjects);

        //Compare results to what we expect to find
        for (Photo photo: results){
            for(int i = 0; i<numPhotoTestObjects;i++){
                if(photo.getUUID().equals(expectedPhotos.get(i).getUUID())){
                    expectedPhotosInResults.set(i,true);
                }
            }
        }
        //check if fetched all expected photos
        for (boolean photoSeen: expectedPhotosInResults){
            assertTrue("Missing photo from result set", photoSeen);
        }
    }

    @Test
    public void GetPhotosByProblemUUIDTaskTest()
            throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        FetchWithProblemUUIDTest(3);
    }

    @Test
    public void GetPhotosByProblemUUIDTaskBUGTest()
            throws InterruptedException,ExecutionException,
            PhotoTooLargeException {

        FetchWithProblemUUIDTest(20);
    }
    private void FetchWithProblemUUIDTest(int numPhotoTestObjects)
            throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        //create different records
        String[] recordUUIDs = nameGen(recordUUIDToRetrieveThroughRecordUUID,numPhotoTestObjects);
        //create mock photo object and make sure image is under max byte limit
        Context context = mainActivity.getActivity().getBaseContext();

        BitmapFactory.Options options = new BitmapFactory.Options();

        ArrayList<Photo> expectedPhotos = new ArrayList<>();
        ArrayList<Boolean> expectedPhotosInResults = new ArrayList<>();

        //stores byte size of each bitmap for comparison
        ArrayList<String> byteSizeList= new ArrayList<String>();
        //inSampleSize reduces pixels that are processed and thus reducing the size (but at cost of resolution)
        int sampleSize = 2;
        //if even, then bodylocation is true, if odd then bodylocation is false
        int isBody = 0;
        String label;
        String bodylocation;
        for (int i =0;i<numPhotoTestObjects;i++){
            InputStream is = context.getResources().openRawResource(R.drawable.testphoto);
            options.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
            byteSizeList.add(Integer.toString(bitmap.getByteCount()));

            if ((isBody%2) == 0){
                bodylocation = "true";
                label = "front";

            } else{
                bodylocation = "false";
                label = "";
            }
            //create photo instance with different recordUUIDs
            Photo photo = new Photo(recordUUIDs[i],problemUUIDforTests
                    ,bodylocation,bitmap,label);
            //add to database
            new ElasticsearchPhotoController.AddPhotoTask().execute(photo).get();

            //add new photo object to expected returns
            expectedPhotos.add(photo);
            expectedPhotosInResults.add(false);

            //increment isBody so bodylocation is different. sampleSize so Byte count in bitmap is different
             //
            isBody++;
            sampleSize += 2;
        }
        //Ensure time for change
        Thread.sleep(ControllerTestTimeout);

        //Make sure each of the added photos can be retrieved individually
        for (int i =0; i<numPhotoTestObjects; i++){
            //fetch photos from ES database
            Photo fetchedPhoto = new ElasticsearchPhotoController.GetPhotoByPhotoUUIDTask()
                    .execute(expectedPhotos.get(i).getUUID()).get();

            assertEquals("Fetched photo has different recordUUID"
                    ,expectedPhotos.get(i).getRecordUUID(), fetchedPhoto.getRecordUUID());

            assertEquals("Fetched photo has different photoUUID"
                    ,expectedPhotos.get(i).getUUID(),fetchedPhoto.getUUID());

            assertEquals("Fetched photo has different bodylocation identifier"
                    ,expectedPhotos.get(i).getBodyLocation(), fetchedPhoto.getBodyLocation());

            assertEquals("Fetched photo has different number of bytes in bitmap"
                    ,byteSizeList.get(i).toString()
                    , Integer.toString(fetchedPhoto.getBitmapFromString().getByteCount()));

            assertEquals("Fetched photo has different label"
                    ,expectedPhotos.get(i).getLabel(),fetchedPhoto.getLabel());
        }

        //Get objects from database associated with problem UUID
        ArrayList<Photo> results = new ElasticsearchPhotoController.GetPhotosByProblemUUIDTask()
                .execute(problemUUIDforTests)
                .get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/161
        if(numPhotoTestObjects> 10 && results.size() == 10){
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoPatientRecord/issues/161 " +
                            "there should be as many results as photos we queried. We got exactly " +
                            "ten results instead of expected "
                            + numPhotoTestObjects
                    ,results.size() == numPhotoTestObjects);

        }
        Assert.assertTrue("there should be as many results as patientRecords we queried. We got " +
                        results.size() + " results instead of expected " +
                        numPhotoTestObjects,
                results.size() == numPhotoTestObjects);

        //Compare results to what we expect to find
        for (Photo photo: results){
            for(int i = 0; i<numPhotoTestObjects;i++){
                if(photo.getUUID().equals(expectedPhotos.get(i).getUUID())){
                    expectedPhotosInResults.set(i,true);
                }
            }
        }
        //check if fetched all expected photos
        for (boolean photoSeen: expectedPhotosInResults){
            assertTrue("Missing photo from result set", photoSeen);
        }
    }

}
