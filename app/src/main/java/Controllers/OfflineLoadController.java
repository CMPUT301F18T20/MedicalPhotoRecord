package Controllers;

import android.content.Context;

import GlobalSettings.GlobalSettings;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
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
import static GlobalSettings.GlobalSettings.PROVIDERFILE;

public class OfflineLoadController {

    // Load from file into patient list
    public static ArrayList<Patient> loadPatientList(Context context){
        ArrayList<Patient> fileList = new ArrayList<Patient>();

        try{
            FileInputStream fis = context.openFileInput(PATIENTFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Patient>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        }
        return(fileList);
    }

    // Load from file into provider list
    public static ArrayList<Provider> loadProviderList(Context context){
        ArrayList<Provider> fileList = new ArrayList<Provider>();

        try{
            FileInputStream fis = context.openFileInput(PROVIDERFILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Provider>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        }
        return(fileList);
    }
}
