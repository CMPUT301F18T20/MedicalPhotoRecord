package Controllers;

import android.content.Context;
import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static Activities.ActivityBank.LOGINActivity;
import static Enums.USER_TYPE.PROVIDER;
import static org.junit.Assert.*;

public class IOLocalSecurityTokenControllerTest {

    Context context = null;

    //create security and user id pair
    String userID = "userID";
    SecurityToken securityToken =
            new SecurityToken(userID, PROVIDER);

    @Before
    public void setUp() {
        //get a context for use with testing
        LOGINActivity.launchActivity(new Intent());
        context = LOGINActivity.getActivity().getBaseContext();

        //delete security token
        IOLocalSecurityTokenController.deleteSecurityTokenOnDisk(context);
    }

    @Test
    public void loadSecurityTokenFromDisk() throws FileNotFoundException {

        //try to fetch SecurityToken from disk, but it doesn't exist
        try {
            //load from disk
            SecurityToken fetchedSecurityToken =
                    IOLocalSecurityTokenController
                            .loadSecurityTokenFromDisk(context);

            fail("Should have thrown a FileNotFoundException");

        } catch (FileNotFoundException e) {
            //expected behavior as we have deleted the file at the start of this test
        }

        //save to disk
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(
                securityToken, context);

        //load from disk
        SecurityToken fetchedSecurityToken =
                IOLocalSecurityTokenController
                        .loadSecurityTokenFromDisk(context);

        assertEquals("ID was not the same",
                userID, fetchedSecurityToken.getUserID());
    }

    @Test
    public void saveSecurityTokenToDisk() throws FileNotFoundException {

        //save to disk
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(
                securityToken, context);

        //load from disk
        SecurityToken fetchedSecurityToken =
                IOLocalSecurityTokenController
                        .loadSecurityTokenFromDisk(context);

        assertEquals("ID was not the same", userID, fetchedSecurityToken.getUserID());
    }

    @Test
    public void deleteSecurityToken() throws FileNotFoundException {

        //save to disk
        IOLocalSecurityTokenController.saveSecurityTokenToDisk(
                securityToken, context);

        //load from disk
        SecurityToken fetchedSecurityToken =
                IOLocalSecurityTokenController
                        .loadSecurityTokenFromDisk(context);

        //make sure users are the same
        assertEquals("ID was not the same",
                userID, fetchedSecurityToken.getUserID());

        //delete security token from disk
        IOLocalSecurityTokenController.deleteSecurityTokenOnDisk(context);

        //try to fetch SecurityToken from disk, but it doesn't exist
        try {
            //load from disk
            SecurityToken fetchedSecurityToken1 =
                    IOLocalSecurityTokenController
                            .loadSecurityTokenFromDisk(context);

            fail("Should have thrown a FileNotFoundException");

        } catch (FileNotFoundException e) {
            //expected behavior as we have deleted the file in this test
        }


    }
}