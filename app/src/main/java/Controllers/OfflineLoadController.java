package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class OfflineLoadController {

    // Load from file into patient list
    public ArrayList<User> loadPatientList(Context context){
        String PATIENTFILE = "patient_list.sav";
        ArrayList<User> fileList = new ArrayList<User>();

        try{
            FileInputStream fis = context.openFileInput(PATIENTFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listTweetType = new TypeToken<ArrayList<User>>(){}.getType();
            fileList = gson.fromJson(reader, listTweetType);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return(fileList);
    }
}
