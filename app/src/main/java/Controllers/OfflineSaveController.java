package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.Record;
import com.cmput301f18t20.medicalphotorecord.User;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static GlobalSettings.GlobalSettings.PATIENTFILE;
import static GlobalSettings.GlobalSettings.PATIENTRECORDFILE;
import static GlobalSettings.GlobalSettings.PHOTOFILE;
import static GlobalSettings.GlobalSettings.PROVIDERFILE;
import static GlobalSettings.GlobalSettings.PROBLEMFILE;
import static GlobalSettings.GlobalSettings.RECORDFILE;


public class OfflineSaveController {

    private void writeToDisk(String filename, Context context, ArrayList<?> users) {
        try{
            FileOutputStream fos = context.openFileOutput(filename,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();
            gson.toJson(users, writer);
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

    public void savePatientList(ArrayList<Patient> patients, Context context){
        writeToDisk(PATIENTFILE, context, patients);
    }

    public void saveProviderList(ArrayList<Provider> providers, Context context){
        writeToDisk(PROVIDERFILE, context, providers);
    }

    public void saveProblemList(ArrayList<Problem> problems, Context context){
        writeToDisk(PROBLEMFILE, context, problems);
    }

    public void saveRecordList(ArrayList<Record> records, Context context){
        writeToDisk(RECORDFILE, context, records);
    }
    public void savePatientRecordLIst(ArrayList<PatientRecord> records, Context context){
        writeToDisk(PATIENTRECORDFILE, context, records);

    public void savePhotoList(ArrayList<Photo> photos, Context context){
        writeToDisk(PHOTOFILE, context, photos);
    }
}
