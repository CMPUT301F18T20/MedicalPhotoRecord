package Controllers;

import android.content.Context;

import GlobalSettings.GlobalSettings;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.cmput301f18t20.medicalphotorecord.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static GlobalSettings.GlobalSettings.PATIENTFILE;
import static GlobalSettings.GlobalSettings.PHOTOFILE;
import static GlobalSettings.GlobalSettings.PROVIDERFILE;
import static GlobalSettings.GlobalSettings.PROBLEMFILE;
import static GlobalSettings.GlobalSettings.RECORDFILE;

public class OfflineLoadController {

    // This looks ugly but need to do it, since GSON does not translate generic type until run time
    // And we uses these functions when app is ran (need to get userid from last intent)

    // Load patient list from offline file
    public static ArrayList<Patient> loadPatientList(Context context) {

        ArrayList<Patient> fileList = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(PATIENTFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Patient>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return fileList ;
    }

    // Load provider list from offline file
    public static ArrayList<Provider> loadProviderList(Context context) {

        ArrayList<Provider> fileList = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(PROVIDERFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Provider>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return fileList ;
    }


    // Load from file into problem list, needs to actually recopy the code since GSON does not translate generic type until run time
    public static ArrayList<Problem> loadProblemList(Context context){
        ArrayList<Problem> fileList = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(PROBLEMFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Problem>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        }
        return fileList ;
    }

    // Load from file into record list, needs to actually recopy the code since GSON does not translate generic type until run time
    public static ArrayList<Record> loadRecordList(Context context){
        ArrayList<Record> fileList = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(RECORDFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Record>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        }
        return fileList ;
    }

    // Load from file into photo list, needs to actually recopy the code since GSON does not translate generic type until run time
    public static ArrayList<Photo> loadPhotoList(Context context){
        ArrayList<Photo> fileList = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(PHOTOFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Photo>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        }
        return fileList ;
    }
}
