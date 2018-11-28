package Controllers;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PROVIDER;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.assertEquals;

public class ElasticsearchSecurityTokenControllerTest {

    private String UserIDForTest = "UserIDForSecurityTokenTest";

    @Before
    public void setUp() throws Exception {
        changeToTestIndex();
        
        //delete all entries
        new ElasticsearchSecurityTokenController
                .DeleteSecurityTokenByUserIDTask().execute().get();
    }

    private void assertUserIDNotInTokenDatabase(String userID) throws ExecutionException,
            InterruptedException  {

        //fetch from the securityToken database by user id
        SecurityToken fetchedSecurityToken = new
                ElasticsearchSecurityTokenController.getByUserIDTask()
                .execute(userID).get();

        //check that the new securityToken is not in the database
        assertEquals("securityToken was in the database",
                null, fetchedSecurityToken);
    }

    @Test
    public void DeleteSecurityTokenByUserIDTask() throws ExecutionException,
            InterruptedException {

        //add in entry
        AddSecurityTokenTask();

        //delete created entry
        new ElasticsearchSecurityTokenController
                .DeleteSecurityTokenByUserIDTask().execute(UserIDForTest).get();

        assertUserIDNotInTokenDatabase(UserIDForTest);
    }
    
    @Test
    public void getByUserSecurityTokenTask() throws ExecutionException, InterruptedException {

        //make sure pair doesn't already exist
        assertUserIDNotInTokenDatabase(UserIDForTest);

        //create new securityToken
        SecurityToken newSecurityToken =
                new SecurityToken(UserIDForTest, PROVIDER);

        //add new securityToken to the securityToken database
        new ElasticsearchSecurityTokenController.AddSecurityTokenTask()
                .execute(newSecurityToken).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the securityToken database by security token
        SecurityToken fetchedSecurityToken = new
                ElasticsearchSecurityTokenController.getByUserSecurityTokenTask()
                .execute(newSecurityToken.getUserSecurityToken()).get();

        //check that the new securityToken is now in the database
        assertEquals("securityTokens were not equal",
                newSecurityToken.getUserID(),
                fetchedSecurityToken.getUserID());
    }
    
    @Test
    public void getByUserIDTask() throws ExecutionException, InterruptedException {
        //the code is essentially identical
        AddSecurityTokenTask();
    }
    
    @Test
    public void AddSecurityTokenTask() throws ExecutionException, InterruptedException {

        //make sure pair doesn't already exist
        assertUserIDNotInTokenDatabase(UserIDForTest);

        //create new securityToken
        SecurityToken newSecurityToken =
                new SecurityToken(UserIDForTest, PROVIDER);

        //add new securityToken to the securityToken database
        new ElasticsearchSecurityTokenController.AddSecurityTokenTask()
                .execute(newSecurityToken).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the securityToken database
        SecurityToken fetchedSecurityToken = new
                ElasticsearchSecurityTokenController.getByUserIDTask()
                .execute(newSecurityToken.getUserID()).get();

        //check that the new securityToken is now in the database
        assertEquals("securityTokens were not equal",
                newSecurityToken.getUserSecurityToken(),
                fetchedSecurityToken.getUserSecurityToken());
    }
}