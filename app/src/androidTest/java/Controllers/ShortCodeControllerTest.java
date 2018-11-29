package Controllers;

import android.content.Context;
import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import Activities.Login;
import Enums.USER_TYPE;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.LOGINActivity;
import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PATIENT;
import static org.junit.Assert.*;

public class ShortCodeControllerTest {

    Context context;
    String UserIDForTest = "ShortCodeControllerUserID";

    @Before
    public void clearCodeAndSecurityTokenDatabase() throws ExecutionException, InterruptedException {
        changeToTestIndex();

        //start up login so we can have a context for use with local storage controllers
        LOGINActivity.launchActivity(new Intent());

        //get the context
        this.context = LOGINActivity.getActivity().getBaseContext();

        //delete all security tokens in local storage and elasticsearch
        new ElasticsearchShortCodeController.DeleteByShortSecurityCodeTask().execute().get();
        IOLocalSecurityTokenController.deleteSecurityTokenOnDisk(context);

        //delete all codes from elasticsearch
        new ElasticsearchSecurityTokenController.DeleteSecurityTokenByUserIDTask().execute().get();
    }

    @Test(expected = failedToFetchSecurityTokenException.class)
    public void failedToFetchSecurityTokenException()
            throws failedToFetchSecurityTokenException, failedToAddShortCodeException {

        //try to add while there's no token generates failedToFetchSecurityTokenException
        ShortCodeController.AddCode(UserIDForTest, context);

    }

    @Test
    public void addCodeWithLocalToken() throws failedToFetchSecurityTokenException, failedToAddShortCodeException,
            ExecutionException, InterruptedException {

        //create security token for user
        SecurityToken securityToken = new SecurityToken(UserIDForTest, PATIENT);

        //add mock local security token for user
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(securityToken, context);

        //token should be available, so we should succeed in adding the short code
        ShortCode shortCode = ShortCodeController.AddCode(UserIDForTest, context);

        //wait for database to reflect changes
        Thread.sleep(5000);

        //fetch that short code from the database
        ShortCode retShortCode = new ElasticsearchShortCodeController.getByShortSecurityCodeTask()
                .execute(shortCode.getShortSecurityCode()).get();

        //result was not null
        assertNotEquals("returned short code was blank", null, retShortCode);

        //check that the two shortCodes are the same (ie, the shortcode is now in the database and
        //could be fetched on another device
        assertEquals("securityTokens were not the same",
                securityToken.getUserSecurityToken(),
                retShortCode.getSecurityToken().getUserSecurityToken());
    }

    @Test
    public void addCodeWithElasticsearchToken() throws failedToFetchSecurityTokenException,
            failedToAddShortCodeException, ExecutionException, InterruptedException {

        //create security token for user
        SecurityToken securityToken = new SecurityToken(UserIDForTest, PATIENT);

        //add mock elasticsearch security token for user
        new ElasticsearchSecurityTokenController.AddSecurityTokenTask().execute(securityToken).get();

        //wait for database to reflect changes
        Thread.sleep(5000);

        //token should be available, so we should succeed in adding the short code
        ShortCode shortCode = ShortCodeController.AddCode(UserIDForTest, context);

        //wait for database to reflect changes
        Thread.sleep(5000);

        //fetch that short code from the database
        ShortCode retShortCode = new ElasticsearchShortCodeController.getByShortSecurityCodeTask()
                .execute(shortCode.getShortSecurityCode()).get();

        //result was not null
        assertNotEquals("returned short code was blank", null, retShortCode);

        //check that the two shortCodes are the same (ie, the shortcode is now in the database and
        //could be fetched on another device
        assertEquals("securityTokens were not the same",
                securityToken.getUserSecurityToken(),
                retShortCode.getSecurityToken().getUserSecurityToken());
    }

    @Test
    public void GetAndStoreCode() {
    }
}