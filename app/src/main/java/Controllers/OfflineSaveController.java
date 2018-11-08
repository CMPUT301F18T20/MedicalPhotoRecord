package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.User;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class OfflineSaveController {

    public void savePatientList(ArrayList<User> users, Context context){
        String PATIENTFILE = "patient_list.sav";

        try{
            FileOutputStream fos = context.openFileOutput(PATIENTFILE,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();
            gson.toJson(users, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
