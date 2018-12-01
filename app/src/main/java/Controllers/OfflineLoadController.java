package Controllers;

import android.content.Context;

import GlobalSettings.GlobalSettings;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;

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
import static GlobalSettings.GlobalSettings.PATIENTRECORDFILE;
import static GlobalSettings.GlobalSettings.PHOTOFILE;
import static GlobalSettings.GlobalSettings.PROVIDERFILE;
import static GlobalSettings.GlobalSettings.PROBLEMFILE;
import static GlobalSettings.GlobalSettings.RECORDFILE;
import static GlobalSettings.GlobalSettings.TEMPPHOTOFILE;

/**
 * OfflineLoadController
 * Can get all patients offline
 * Can get all providers offline
 * Can get all problems offline
 * Can get all records offline
 * Can get all patient records offline
 * Can get all photos offline
 * Can get all temporary photos offline
 * @version 2.0
 * @see Patient
 * @see Provider
 * @see Problem
 * @see Record
 * @see PatientRecord
 * @see Photo
 */
public class OfflineLoadController {

    // This looks ugly but need to do it, since GSON does not translate generic type until run time
    // And we uses these functions when app is ran (need to get userid from last intent)

    /**
     * Load patient list from offline file
     * @param context: activity to be passed for offline save and load
     * @return patient list
     */
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

    /**
     * Load provider list from offline file
     * @param context: activity to be passed for offline save and load
     * @return provider list
     */
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


    /**
     * Load from file into problem list
     * @param context: activity to be passed for offline save and load
     * @return problem list
     */
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

    /**
     * Load from file into record list
     * @param context: activity to be passed for offline save and load
     * @return record list
     */
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

    /**
     * Load from file into patient record list
     * @param context: activity to be passed for offline save and load
     * @return patient record list
     */
    public static ArrayList<PatientRecord> loadPatientRecordList(Context context) {
        ArrayList<PatientRecord> fileList = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(PATIENTRECORDFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<PatientRecord>>() {
            }.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * Load from file into photo list
     * @param context: activity to be passed for offline save and load
     * @return photo list
     */
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

    /**
     * Load from file into temp photo list
     * @param context: activity to be passed for offline save and load
     * @return temp photo list
     */
    public static ArrayList<Photo> loadTempPhotoList(Context context){
        ArrayList<Photo> fileList = new ArrayList<>();
        try{
            FileInputStream fis = context.openFileInput(TEMPPHOTOFILE);
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
