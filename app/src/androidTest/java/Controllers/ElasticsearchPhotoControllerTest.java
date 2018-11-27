package Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;

import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import Activities.AddRecordActivity;
import Activities.ViewRecordActivity;
import Exceptions.PhotoTooLargeException;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.assertEquals;


public class ElasticsearchPhotoControllerTest{

    private String
            recordUUIDinAddTest = "ImFromThePhotoAddTest";

    //@Rule
    //public final ActivityTestRule<ViewRecordActivity> mainActivity = new ActivityTestRule<>(ViewRecordActivity.class);

    @Test
    public void AddPhotoTask() throws InterruptedException,ExecutionException,
            PhotoTooLargeException{

        //create mock photo object and make sure image is under max byte limit
        //Context context = mainActivity.getActivity().getBaseContext();

        //InputStream is = context.getResources().openRawResource(R.drawable.testphoto);

        //inSampleSize reduces pixels that are processed and thus reducing the size (but at cost of resolution)
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 2;
        //Bitmap photoBitmap = BitmapFactory.decodeStream(is,null,options);
        Photo mockPhoto = new Photo( "Front",recordUUIDinAddTest);

        //add to database
        new ElasticsearchPhotoController.AddPhotoTask().execute(mockPhoto).get();
        //Log.d("swag","wtf");

        //allow time for change
        Thread.sleep(ControllerTestTimeout);

        //fetch photo from database
        Photo fetchedPhoto = new ElasticsearchPhotoController.GetPhotoByUUIDTask().execute(mockPhoto.getUUID()).get();

        assertEquals("Fetched photo is not equal",mockPhoto.getRecordUUID(),fetchedPhoto.getRecordUUID());
    }
}
