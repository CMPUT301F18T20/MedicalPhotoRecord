package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.SecurityTokenAndUserIDPair;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static GlobalSettings.GlobalSettings.SECURITYTOKENFILE;

public class IOLocalSecurityTokenAndIDPairController {

    // Load security token and uuid pair from
    public static SecurityTokenAndUserIDPair loadSecurityTokenAndUserIDPairFromDisk(Context context)
            throws FileNotFoundException {

        //init object
        SecurityTokenAndUserIDPair returnSecurityTokenAndUserIDPair = null;
        ArrayList<SecurityTokenAndUserIDPair> fetchReturn = null;

        //get a reader
        FileInputStream fis = context.openFileInput(SECURITYTOKENFILE);
        InputStreamReader isr = new InputStreamReader(fis);

        //fetch object
        Gson gson = new Gson();
        Type securityTokenAndUserIDType = new TypeToken<ArrayList<SecurityTokenAndUserIDPair>>(){}.getType();
        fetchReturn = gson.fromJson(isr, securityTokenAndUserIDType);

        //if there's no token, throw error
        if (fetchReturn.size() < 1) {
            throw new FileNotFoundException();
        }

        //set the return value
        returnSecurityTokenAndUserIDPair = fetchReturn.get(0);

        //return our fetched object
        return returnSecurityTokenAndUserIDPair ;
    }

    public static void saveSecurityTokenAndUserIDPairToDisk(
            SecurityTokenAndUserIDPair securityTokenAndUserIDPair, Context context) {

        //change format of input to work with OfflineSaveController's save method
        ArrayList<SecurityTokenAndUserIDPair> arrayListFromToAllowWriteToWork = new ArrayList<>();
        arrayListFromToAllowWriteToWork.add(securityTokenAndUserIDPair);

        //save security token to file
        OfflineSaveController.writeToDisk(SECURITYTOKENFILE, context, arrayListFromToAllowWriteToWork);
    }

    public static boolean deleteSecurityTokenAndUserIDPairOnDisk(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, SECURITYTOKENFILE);
        return file.delete();
    }
}
