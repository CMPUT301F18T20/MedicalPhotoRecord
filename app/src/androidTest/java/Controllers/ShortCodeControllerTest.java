package Controllers;

import android.content.Context;
import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import Activities.Login;
import Enums.USER_TYPE;
import Exceptions.NoSuchCodeException;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;
import Exceptions.failedToFetchShortCodeException;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.LOGINActivity;
import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PATIENT;
import static org.junit.Assert.*;

public class ShortCodeControllerTest {

    Context context;
    String UserIDForTest = "ShortCodeControllerUserID";
    //create security token for user
    SecurityToken securityToken = new SecurityToken(UserIDForTest, PATIENT);

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

        //get return from adding short code
        ShortCode shortCode = addElasticsearchSecurityTokenAndCreateCode();

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
    public void GetAndStoreCode() throws failedToFetchSecurityTokenException,
            failedToAddShortCodeException, failedToFetchShortCodeException, NoSuchCodeException,
            ExecutionException, InterruptedException, FileNotFoundException {

        //get return from adding short code
        ShortCode shortCode = addElasticsearchSecurityTokenAndCreateCode();

        //getAndStoreCode will get security token from short code and take a local copy of the
        //security token stored inside
        ShortCodeController.GetAndStoreSecurityToken(shortCode.getShortSecurityCode(), context);

        //security token now exists on local storage
        SecurityToken securityTokenOnDisk = IOLocalSecurityTokenController
                .loadSecurityTokenFromDisk(context);

        //make sure the loaded security token was not null
        assertNotEquals("Security token was not stored correctly on disk", null,
                securityTokenOnDisk);

        //assert local security token has same uuid as elasticsearch master
        assertEquals("Security token did not have the same key",
                shortCode.getSecurityToken().getUserSecurityToken(),
                securityTokenOnDisk.getUserSecurityToken());

        //code has been deleted from elasticsearch
        //fetch that short code from the database
        ShortCode shortCodeFromElasticsearch = new ElasticsearchShortCodeController
                .getByShortSecurityCodeTask().execute(shortCode.getShortSecurityCode()).get();

        //result was null
        assertEquals("short code was not deleted",
                null, shortCodeFromElasticsearch);

    }

    private ShortCode addElasticsearchSecurityTokenAndCreateCode() throws InterruptedException,
            ExecutionException, failedToFetchSecurityTokenException, failedToAddShortCodeException {

        //add mock elasticsearch security token for user
        new ElasticsearchSecurityTokenController.AddSecurityTokenTask().execute(securityToken).get();

        //wait for database to reflect changes
        Thread.sleep(5000);

        //token should be available, so we should succeed in adding the short code
        ShortCode shortCode = ShortCodeController.AddCode(UserIDForTest, context);

        //wait for database to reflect changes
        Thread.sleep(5000);

        return shortCode;
    }
}