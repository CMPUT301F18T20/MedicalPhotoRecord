package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import java.util.concurrent.ExecutionException;

import Enums.USER_TYPE;

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
