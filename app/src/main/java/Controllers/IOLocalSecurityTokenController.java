package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static GlobalSettings.GlobalSettings.SECURITYTOKENFILE;

public class IOLocalSecurityTokenController {

    // Load security token and id pair from
    public static SecurityToken loadSecurityTokenFromDisk(Context context)
            throws FileNotFoundException {

        //init object
        SecurityToken returnSecurityToken = null;
        ArrayList<SecurityToken> fetchReturn = null;

        //get a reader
        FileInputStream fis = context.openFileInput(SECURITYTOKENFILE);
        InputStreamReader isr = new InputStreamReader(fis);

        //fetch object
        Gson gson = new Gson();
        Type securityTokenAndUserIDType = new TypeToken<ArrayList<SecurityToken>>(){}.getType();
        fetchReturn = gson.fromJson(isr, securityTokenAndUserIDType);

        //if there's no token, throw error
        if (fetchReturn.size() < 1) {
            throw new FileNotFoundException();
        }

        //set the return value
        returnSecurityToken = fetchReturn.get(0);

        //return our fetched object
        return returnSecurityToken;
    }

    public static void saveSecurityTokenToDisk(
            SecurityToken securityToken, Context context) {

        //change format of input to work with OfflineSaveController's save method
        ArrayList<SecurityToken> arrayListFromToAllowWriteToWork = new ArrayList<>();
        arrayListFromToAllowWriteToWork.add(securityToken);

        //save security token to file
        OfflineSaveController.writeToDisk(SECURITYTOKENFILE, context, arrayListFromToAllowWriteToWork);
    }

    public static boolean deleteSecurityTokenOnDisk(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, SECURITYTOKENFILE);
        return file.delete();
    }
}
