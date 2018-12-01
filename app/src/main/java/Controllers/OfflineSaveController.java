package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.GeoLocation;
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

import static GlobalSettings.GlobalSettings.GEOFILE;
import static GlobalSettings.GlobalSettings.PATIENTFILE;
import static GlobalSettings.GlobalSettings.PATIENTRECORDFILE;
import static GlobalSettings.GlobalSettings.PHOTOFILE;
import static GlobalSettings.GlobalSettings.PROVIDERFILE;
import static GlobalSettings.GlobalSettings.PROBLEMFILE;
import static GlobalSettings.GlobalSettings.RECORDFILE;
import static GlobalSettings.GlobalSettings.TEMPGEOFILE;
import static GlobalSettings.GlobalSettings.TEMPPHOTOFILE;


/**
 * OfflineSaveController
 * Can write to database all patients list offline
 * Can write to database all providers list offline
 * Can write to database all problems list offline
 * Can write to database all records list offline
 * Can write to database all patient records list offline
 * Can write to database all photos list offline
 * Can write to database all temporary photos list offline
 * @version 2.0
 * @see Patient
 * @see Provider
 * @see Problem
 * @see Record
 * @see PatientRecord
 * @see Photo
 */
public class OfflineSaveController {

    /**
     * Write to disk file a list of object
     * @param filename: in GLOBAL SETTINGS
     * @param context: activity to be passed for offline save and load
     * @param users
     */
    protected static void writeToDisk(String filename, Context context, ArrayList<?> users) {
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

    /**
     * Write to disk patient list
     * @param patients
     * @param context: activity to be passed for offline save and load
     */
    public void savePatientList(ArrayList<Patient> patients, Context context){
        writeToDisk(PATIENTFILE, context, patients);
    }

    /**
     * Write to disk provider list
     * @param providers
     * @param context: activity to be passed for offline save and load
     */
    public void saveProviderList(ArrayList<Provider> providers, Context context){
        writeToDisk(PROVIDERFILE, context, providers);
    }

    /**
     * Write to disk problem list
     * @param problems
     * @param context: activity to be passed for offline save and load
     */
    public void saveProblemList(ArrayList<Problem> problems, Context context){
        writeToDisk(PROBLEMFILE, context, problems);
    }

    /**
     * Write to disk record list
     * @param records
     * @param context: activity to be passed for offline save and load
     */
    public void saveRecordList(ArrayList<Record> records, Context context){
        writeToDisk(RECORDFILE, context, records);
    }

    /**
     * Write to disk patient record list
     * @param records
     * @param context: activity to be passed for offline save and load
     */
    public void savePatientRecordLIst(ArrayList<PatientRecord> records, Context context) {
        writeToDisk(PATIENTRECORDFILE, context, records);

    }

    /**
     * Write to disk photo list
     * @param photos
     * @param context: activity to be passed for offline save and load
     */
    public void savePhotoList(ArrayList<Photo> photos, Context context){
        writeToDisk(PHOTOFILE, context, photos);
    }

    /**
     * Write to disk temp photo list
     * @param photos
     * @param context: activity to be passed for offline save and load
     */
    public void saveTempPhotoList(ArrayList<Photo> photos, Context context){
        writeToDisk(TEMPPHOTOFILE, context, photos);
    }

    public void saveGeoLocationList(ArrayList<GeoLocation> geoLocations, Context context){
        writeToDisk(GEOFILE,context,geoLocations);
    }

    public void saveTempGeoLocationList(ArrayList<GeoLocation> geoLocations, Context context){
        writeToDisk(TEMPGEOFILE,context,geoLocations);
    }
}
