package Controllers;

import android.content.Context;
import android.content.Intent;

import com.cmput301f18t20.medicalphotorecord.SecurityTokenAndUserIDPair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileNotFoundException;

import Activities.ActivityBank;
import Activities.AddProblemActivity;
import Enums.USER_TYPE;
import androidx.test.rule.ActivityTestRule;

import static Activities.ActivityBank.LOGINActivity;
import static Enums.USER_TYPE.PROVIDER;
import static org.junit.Assert.*;

public class IOLocalSecurityTokenAndIDPairControllerTest {

    Context context = null;

    //create security and user uuid pair
    String userID = "userID";
    SecurityTokenAndUserIDPair securityTokenAndUserIDPair =
            new SecurityTokenAndUserIDPair(userID, PROVIDER);

    @Before
    public void setUp() {
        //get a context for use with testing
        LOGINActivity.launchActivity(new Intent());
        context = LOGINActivity.getActivity().getBaseContext();

        //delete security token
        IOLocalSecurityTokenAndIDPairController.deleteSecurityTokenAndUserIDPairOnDisk(context);
    }

    @Test
    public void loadSecurityTokenAndUserIDPairFromDisk() throws FileNotFoundException {

        //try to fetch SecurityTokenAndUserIDPair from disk, but it doesn't exist
        try {
            //load from disk
            SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair =
                    IOLocalSecurityTokenAndIDPairController
                            .loadSecurityTokenAndUserIDPairFromDisk(context);

            fail("Should have thrown a FileNotFoundException");

        } catch (FileNotFoundException e) {
            //expected behavior as we have deleted the file at the start of this test
        }

        //save to disk
        IOLocalSecurityTokenAndIDPairController.saveSecurityTokenAndUserIDPairToDisk(
                securityTokenAndUserIDPair, context);

        //load from disk
        SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair =
                IOLocalSecurityTokenAndIDPairController
                        .loadSecurityTokenAndUserIDPairFromDisk(context);

        assertEquals("ID was not the same",
                userID, fetchedSecurityTokenAndUserIDPair.getUserID());
    }

    @Test
    public void saveSecurityTokenAndUserIDPairToDisk() throws FileNotFoundException {

        //save to disk
        IOLocalSecurityTokenAndIDPairController.saveSecurityTokenAndUserIDPairToDisk(
                securityTokenAndUserIDPair, context);

        //load from disk
        SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair =
                IOLocalSecurityTokenAndIDPairController
                        .loadSecurityTokenAndUserIDPairFromDisk(context);

        assertEquals("ID was not the same", userID, fetchedSecurityTokenAndUserIDPair.getUserID());
    }

    @Test
    public void deleteSecurityTokenAndUserIDPair() throws FileNotFoundException {

        //save to disk
        IOLocalSecurityTokenAndIDPairController.saveSecurityTokenAndUserIDPairToDisk(
                securityTokenAndUserIDPair, context);

        //load from disk
        SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair =
                IOLocalSecurityTokenAndIDPairController
                        .loadSecurityTokenAndUserIDPairFromDisk(context);

        //make sure users are the same
        assertEquals("ID was not the same",
                userID, fetchedSecurityTokenAndUserIDPair.getUserID());

        //delete security token from disk
        IOLocalSecurityTokenAndIDPairController.deleteSecurityTokenAndUserIDPairOnDisk(context);

        //try to fetch SecurityTokenAndUserIDPair from disk, but it doesn't exist
        try {
            //load from disk
            SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair1 =
                    IOLocalSecurityTokenAndIDPairController
                            .loadSecurityTokenAndUserIDPairFromDisk(context);

            fail("Should have thrown a FileNotFoundException");

        } catch (FileNotFoundException e) {
            //expected behavior as we have deleted the file in this test
        }


    }
}