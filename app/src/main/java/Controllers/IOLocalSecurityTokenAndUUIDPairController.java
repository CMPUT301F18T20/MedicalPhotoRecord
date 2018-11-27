package Controllers;

import android.content.Context;
import android.util.Log;

import com.cmput301f18t20.medicalphotorecord.SecurityTokenAndUserUUIDPair;
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

import Exceptions.SecurityTokenNotFound;

import static GlobalSettings.GlobalSettings.SECURITYTOKENFILE;

public class IOLocalSecurityTokenAndUUIDPairController {

    // Load security token and uuid pair from
    public static SecurityTokenAndUserUUIDPair loadSecurityTokenAndUserUUIDPairFromDisk(Context context)
            throws FileNotFoundException, SecurityTokenNotFound {

        //init object
        SecurityTokenAndUserUUIDPair returnSecurityTokenAndUserUUIDPair = null;
        ArrayList<SecurityTokenAndUserUUIDPair> fetchReturn = null;

        //get a reader
        FileInputStream fis = context.openFileInput(SECURITYTOKENFILE);
        InputStreamReader isr = new InputStreamReader(fis);

        //fetch object
        Gson gson = new Gson();
        Type securityTokenAndUserUUIDType = new TypeToken<ArrayList<SecurityTokenAndUserUUIDPair>>(){}.getType();
        fetchReturn = gson.fromJson(isr, securityTokenAndUserUUIDType);

        //if there's no token, throw error
        if (fetchReturn.size() < 1) {
            throw new SecurityTokenNotFound();
        }

        //set the return value
        returnSecurityTokenAndUserUUIDPair = fetchReturn.get(0);

        //return our fetched object
        return returnSecurityTokenAndUserUUIDPair ;
    }

    public static void saveSecurityTokenAndUserUUIDPairToDisk(
            SecurityTokenAndUserUUIDPair securityTokenAndUserUUIDPair, Context context) {

        //change format of input to work with OfflineSaveController's save method
        ArrayList<SecurityTokenAndUserUUIDPair> arrayListFromToAllowWriteToWork = new ArrayList<>();
        arrayListFromToAllowWriteToWork.add(securityTokenAndUserUUIDPair);

        //save security token to file
        OfflineSaveController.writeToDisk(SECURITYTOKENFILE, context, arrayListFromToAllowWriteToWork);
    }

    public static boolean deleteSecurityTokenAndUserUUIDPairOnDisk(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, SECURITYTOKENFILE);
        return file.delete();
    }
}
