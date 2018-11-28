package Controllers;

import com.cmput301f18t20.medicalphotorecord.SecurityTokenAndUserIDPair;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import Enums.USER_TYPE;

import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PROVIDER;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.assertEquals;

public class ElasticsearchSecurityTokenAndUserIDPairControllerTest {

    private String UserIDForTest = "UserIDForSecurityTokenTest";

    @Before
    public void setUp() throws Exception {
        changeToTestIndex();
        
        //delete all entries
        new ElasticsearchSecurityTokenAndUserIDPairController
                .DeleteSecurityTokenAndUserIDPairByUserIDTask().execute().get();
    }

    public void assertUserIDNotInTokenDatabase(String userID) throws ExecutionException,
            InterruptedException  {

        //fetch from the securityTokenAndUserIDPair database by user id
        SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair = new
                ElasticsearchSecurityTokenAndUserIDPairController.getByUserIDTask()
                .execute(userID).get();

        //check that the new securityTokenAndUserIDPair is not in the database
        assertEquals("securityTokenAndUserIDPairs was in the database",
                null, fetchedSecurityTokenAndUserIDPair);
    }

    @Test
    public void DeleteSecurityTokenAndUserIDPairByUserIDTask() throws ExecutionException,
            InterruptedException {

        //add in entry
        AddSecurityTokenAndUserIDPairTask();

        //delete created entry
        new ElasticsearchSecurityTokenAndUserIDPairController
                .DeleteSecurityTokenAndUserIDPairByUserIDTask().execute(UserIDForTest).get();

        assertUserIDNotInTokenDatabase(UserIDForTest);
    }
    
    @Test
    public void getByUserSecurityTokenTask() throws ExecutionException, InterruptedException {

        //make sure pair doesn't already exist
        assertUserIDNotInTokenDatabase(UserIDForTest);

        //create new securityTokenAndUserIDPair
        SecurityTokenAndUserIDPair newSecurityTokenAndUserIDPair =
                new SecurityTokenAndUserIDPair(UserIDForTest, PROVIDER);

        //add new securityTokenAndUserIDPair to the securityTokenAndUserIDPair database
        new ElasticsearchSecurityTokenAndUserIDPairController.AddSecurityTokenAndUserIDPairTask()
                .execute(newSecurityTokenAndUserIDPair).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the securityTokenAndUserIDPair database by security token
        SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair = new
                ElasticsearchSecurityTokenAndUserIDPairController.getByUserSecurityTokenTask()
                .execute(newSecurityTokenAndUserIDPair.getUserSecurityToken()).get();

        //check that the new securityTokenAndUserIDPair is now in the database
        assertEquals("securityTokenAndUserIDPairs were not equal",
                newSecurityTokenAndUserIDPair.getUserID(),
                fetchedSecurityTokenAndUserIDPair.getUserID());
    }
    
    @Test
    public void getByUserIDTask() throws ExecutionException, InterruptedException {
        //the code is essentially identical
        AddSecurityTokenAndUserIDPairTask();
    }
    
    @Test
    public void AddSecurityTokenAndUserIDPairTask() throws ExecutionException, InterruptedException {

        //make sure pair doesn't already exist
        assertUserIDNotInTokenDatabase(UserIDForTest);

        //create new securityTokenAndUserIDPair
        SecurityTokenAndUserIDPair newSecurityTokenAndUserIDPair =
                new SecurityTokenAndUserIDPair(UserIDForTest, PROVIDER);

        //add new securityTokenAndUserIDPair to the securityTokenAndUserIDPair database
        new ElasticsearchSecurityTokenAndUserIDPairController.AddSecurityTokenAndUserIDPairTask()
                .execute(newSecurityTokenAndUserIDPair).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the securityTokenAndUserIDPair database
        SecurityTokenAndUserIDPair fetchedSecurityTokenAndUserIDPair = new
                ElasticsearchSecurityTokenAndUserIDPairController.getByUserIDTask()
                .execute(newSecurityTokenAndUserIDPair.getUserID()).get();

        //check that the new securityTokenAndUserIDPair is now in the database
        assertEquals("securityTokenAndUserIDPairs were not equal",
                newSecurityTokenAndUserIDPair.getUserSecurityToken(),
                fetchedSecurityTokenAndUserIDPair.getUserSecurityToken());
    }
}