package Controllers;

import android.content.Context;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;

import static Controllers.IOLocalSecurityTokenController.loadSecurityTokenFromDisk;
import static android.widget.Toast.LENGTH_LONG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ShortCodeController {
    public static Boolean AddCode(String UserID, Context context) throws
            failedToFetchSecurityTokenException, failedToAddShortCodeException {

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
            return FALSE;
        }

        //add security token and small generated code to the elasticsearch
        ShortCode shortCode = new ShortCode(securityToken);

        try {
            new ElasticsearchShortCodeController.AddShortCodeTask().execute(shortCode).get();
        } catch (ExecutionException e2) {
            throw new failedToAddShortCodeException();
        } catch (InterruptedException e2) {
            throw new failedToAddShortCodeException();
        }

        return TRUE;
    }
}
