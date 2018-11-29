package Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import Exceptions.NoSuchCodeException;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;
import Exceptions.failedToFetchShortCodeException;

import static Controllers.IOLocalSecurityTokenController.loadSecurityTokenFromDisk;

public class ShortCodeController {


    public static ShortCode AddCode(String UserID, Context context) throws
            failedToFetchSecurityTokenException, failedToAddShortCodeException {

        //get the user's security token
        //generate a short code and associate it with the fetched security token
        ShortCode shortCode = addShortCode(fetchSecurityToken(UserID, context));

        //make this function much easier to test, return generated short code
        return shortCode;
    }

    public static void  GetAndStoreCode(String code, Context context)
            throws failedToFetchShortCodeException, NoSuchCodeException {

        //fetch the matching code
        ShortCode shortCode = getShortCode(code);

        //save the contained security token to disk
        SaveSecurityToken(shortCode, context);

        //delete the shortCode from the database
        DeleteShortCode(shortCode);
    }

    private static void SaveSecurityToken(ShortCode shortCode, Context context) {
        IOLocalSecurityTokenController
                .saveSecurityTokenToDisk(shortCode.getSecurityToken(), context);
    }

    private static SecurityToken fetchSecurityToken(String UserID, Context context)
            throws failedToFetchSecurityTokenException {
        SecurityToken securityToken;

        //load from offline
        try {
            securityToken = loadSecurityTokenFromDisk(context);

            //not an issue if file isn't found, we'll go looking in elasticsearch
        } catch (FileNotFoundException e) {
            try {
                //load from elasticsearch
                securityToken = new ElasticsearchSecurityTokenController.getByUserIDTask()
                        .execute(UserID).get();
            } catch (ExecutionException e2) {
                throw new failedToFetchSecurityTokenException();
            } catch (InterruptedException e2) {
                throw new failedToFetchSecurityTokenException();
            }
        }

        //if we were unsuccessful in loading, securityToken will be null
        if (securityToken == null) {
            throw new failedToFetchSecurityTokenException();
        }

        return securityToken;
    }

    private static ShortCode addShortCode(SecurityToken securityToken)
            throws failedToAddShortCodeException {
        //generate the short code and add it to elastic search
        ShortCode shortCode = new ShortCode(securityToken);

        //add security token and small generated code to the elasticsearch
        try {
            new ElasticsearchShortCodeController.AddShortCodeTask().execute(shortCode).get();
        } catch (ExecutionException e2) {
            throw new failedToAddShortCodeException();
        } catch (InterruptedException e2) {
            throw new failedToAddShortCodeException();
        }

        //make it easy to test the parent function
        return shortCode;
    }

    private static ShortCode getShortCode(String codeToMatch)
            throws failedToFetchShortCodeException, NoSuchCodeException {

        ShortCode shortCode;

        //fetch the code from the elasticsearch
        try {
            //pass the code entered into the controller to see if it matches
            shortCode = new ElasticsearchShortCodeController.getByShortSecurityCodeTask()
                    .execute(codeToMatch).get();
        } catch (ExecutionException e) {
            throw new failedToFetchShortCodeException();
        } catch (InterruptedException e) {
            throw new failedToFetchShortCodeException();
        }

        if (shortCode == null) {
            throw new NoSuchCodeException();
        }

        return shortCode;
    }

    private static void DeleteShortCode(ShortCode shortCode) {
        //delete the passed in ShortCode from elasticsearch
        try {
            new ElasticsearchShortCodeController.DeleteByShortSecurityCodeTask()
                    .execute(shortCode.getShortSecurityCode()).get();
        } catch (ExecutionException e) {
            Log.d("DeleteShortCode", "EXECUTIONEXCEPTION");
        } catch (InterruptedException e) {
            Log.d("DeleteShortCode", "INTERRUPTEDEXCEPTION");
        }
    }
}
