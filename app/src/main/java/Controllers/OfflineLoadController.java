package Controllers;

import android.content.Context;

import GlobalSettings.GlobalSettings;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Problem;
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
import static GlobalSettings.GlobalSettings.PROBLEMFILE;

public class OfflineLoadController {

    private static ArrayList<?> loadFromDisk(String filename, Context context) {

        ArrayList<?> fileList = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<?>>(){}.getType();
            fileList = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e){
            //TODO handle exception
            e.printStackTrace();
        }
        return fileList ;
    }

    // Load from file into patient list
    public static ArrayList<Patient> loadPatientList(Context context){
        return (ArrayList<Patient>) loadFromDisk(PATIENTFILE, context);
    }

    // Load from file into provider list
    public static ArrayList<Provider> loadProviderList(Context context){
        return (ArrayList<Provider>) loadFromDisk(PROVIDERFILE, context);
    }

    // Load from file into problem list
    public static ArrayList<Problem> loadProblemList(Context context){
        return (ArrayList<Problem>) loadFromDisk(PROBLEMFILE, context);
    }
}
