package Controllers;

import android.content.Context;
import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.SecurityTokenAndUserUUIDPair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileNotFoundException;

import Activities.ActivityBank;
import Activities.AddProblemActivity;
import Enums.USER_TYPE;
import Exceptions.SecurityTokenNotFound;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.LOGINActivity;
import static Enums.USER_TYPE.PROVIDER;
import static org.junit.Assert.*;

public class IOLocalSecurityTokenAndUUIDPairControllerTest {

    Context context = null;

    //create security and user uuid pair
    String userUUID = "userUUID";
    SecurityTokenAndUserUUIDPair securityTokenAndUserUUIDPair =
            new SecurityTokenAndUserUUIDPair(userUUID, PROVIDER);

    @Before
    public void setUp() {
        //get a context for use with testing
        LOGINActivity.launchActivity(new Intent());
        context = LOGINActivity.getActivity().getBaseContext();

        //delete security token
        IOLocalSecurityTokenAndUUIDPairController.deleteSecurityTokenAndUserUUIDPairOnDisk(context);
    }

    @Test
    public void loadSecurityTokenAndUserUUIDPairFromDisk() throws FileNotFoundException,
            SecurityTokenNotFound {

        //try to fetch SecurityTokenAndUserUUIDPair from disk, but it doesn't exist
        try {
            //load from disk
            SecurityTokenAndUserUUIDPair fetchedSecurityTokenAndUserUUIDPair =
                    IOLocalSecurityTokenAndUUIDPairController
                            .loadSecurityTokenAndUserUUIDPairFromDisk(context);

            fail("Should have thrown a FileNotFoundException");

        } catch (FileNotFoundException e) {
            //expected behavior as we have deleted the file at the start of this test
        }

        //save to disk
        IOLocalSecurityTokenAndUUIDPairController.saveSecurityTokenAndUserUUIDPairToDisk(
                securityTokenAndUserUUIDPair, context);

        //load from disk
        SecurityTokenAndUserUUIDPair fetchedSecurityTokenAndUserUUIDPair =
                IOLocalSecurityTokenAndUUIDPairController
                        .loadSecurityTokenAndUserUUIDPairFromDisk(context);

        assertEquals("UUID was not the same",
                userUUID, fetchedSecurityTokenAndUserUUIDPair.getUserUUID());
    }

    @Test
    public void saveSecurityTokenAndUserUUIDPairToDisk() throws FileNotFoundException, SecurityTokenNotFound {

        //save to disk
        IOLocalSecurityTokenAndUUIDPairController.saveSecurityTokenAndUserUUIDPairToDisk(
                securityTokenAndUserUUIDPair, context);

        //load from disk
        SecurityTokenAndUserUUIDPair fetchedSecurityTokenAndUserUUIDPair =
                IOLocalSecurityTokenAndUUIDPairController
                        .loadSecurityTokenAndUserUUIDPairFromDisk(context);

        assertEquals("UUID was not the same", userUUID, fetchedSecurityTokenAndUserUUIDPair.getUserUUID());
    }

    @Test
    public void deleteSecurityTokenAndUserUUIDPair() throws FileNotFoundException, SecurityTokenNotFound {

        //save to disk
        IOLocalSecurityTokenAndUUIDPairController.saveSecurityTokenAndUserUUIDPairToDisk(
                securityTokenAndUserUUIDPair, context);

        //load from disk
        SecurityTokenAndUserUUIDPair fetchedSecurityTokenAndUserUUIDPair =
                IOLocalSecurityTokenAndUUIDPairController
                        .loadSecurityTokenAndUserUUIDPairFromDisk(context);

        //make sure users are the same
        assertEquals("UUID was not the same",
                userUUID, fetchedSecurityTokenAndUserUUIDPair.getUserUUID());

        //delete security token from disk
        IOLocalSecurityTokenAndUUIDPairController.deleteSecurityTokenAndUserUUIDPairOnDisk(context);

        //try to fetch SecurityTokenAndUserUUIDPair from disk, but it doesn't exist
        try {
            //load from disk
            SecurityTokenAndUserUUIDPair fetchedSecurityTokenAndUserUUIDPair1 =
                    IOLocalSecurityTokenAndUUIDPairController
                            .loadSecurityTokenAndUserUUIDPairFromDisk(context);

            fail("Should have thrown a FileNotFoundException");

        } catch (FileNotFoundException e) {
            //expected behavior as we have deleted the file in this test
        }


    }
}