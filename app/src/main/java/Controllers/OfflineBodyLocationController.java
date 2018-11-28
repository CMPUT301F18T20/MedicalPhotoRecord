package Controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static GlobalSettings.GlobalSettings.BODYLOCATIONFILE;
import static GlobalSettings.GlobalSettings.PATIENTRECORDFILE;

public class OfflineBodyLocationController {


    public void saveBodyPhoto(Context context,ArrayList<Photo> photos) {
        try{
            FileOutputStream fos = context.openFileOutput(BODYLOCATIONFILE,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();
            gson.toJson(photos, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        } catch (IOException e){
            //TODO handle exception
            e.printStackTrace();
        }
    }

    public ArrayList<Photo> loadBodyPhotos(Context context){
        ArrayList<Photo> photolist = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(BODYLOCATIONFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Photo>>() {
            }.getType();
            photolist = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return photolist;
    }

}
