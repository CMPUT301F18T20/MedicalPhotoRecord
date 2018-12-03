package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import java.util.concurrent.ExecutionException;

import Enums.USER_TYPE;

/**
 * SecurityTokenController
 * Can add a security token for login to online and offline database
 * @version 1.0
 */
public class SecurityTokenController {

    public static void addSecurityToken(String UserID, USER_TYPE user_type, Context context)
            throws ExecutionException, InterruptedException {
        SecurityToken securityToken = new SecurityToken(UserID, user_type);

        //save to elasticsearch
        new ElasticsearchSecurityTokenController.AddSecurityTokenTask().execute(securityToken).get();

        //save locally
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(securityToken, context);
    }
}
