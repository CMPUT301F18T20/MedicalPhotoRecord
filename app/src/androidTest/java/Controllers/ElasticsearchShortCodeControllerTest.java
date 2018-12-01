package Controllers;

import com.cmput301f18t20.medicalphotorecord.SecurityToken;
import com.cmput301f18t20.medicalphotorecord.ShortCode;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static Activities.ActivityBank.changeToTestIndex;
import static Enums.USER_TYPE.PATIENT;
import static Enums.USER_TYPE.PROVIDER;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.*;

public class ElasticsearchShortCodeControllerTest {
    private SecurityToken SecurityTokenForTest = 
            new SecurityToken("UserIDForSecurityTokenTest", PATIENT);
    ShortCode newShortCode;

    @Before
    public void setUp() throws Exception {
        changeToTestIndex();

        //delete all entries
        new ElasticsearchShortCodeController.DeleteByShortSecurityCodeTask().execute().get();
    }

    @Test
    public void DeleteShortCodeByUserIDTask() throws ExecutionException,
            InterruptedException {

        //add in entry and assert it's there
        AddShortCodeTask();

        //delete created entry
        new ElasticsearchShortCodeController
                .DeleteByShortSecurityCodeTask().execute(newShortCode.getShortSecurityCode()).get();

        //fetch from the shortCode database again, now it should be null
        ShortCode fetchedShortCode = new ElasticsearchShortCodeController
                .getByShortSecurityCodeTask().execute(newShortCode.getShortSecurityCode()).get();

        //check that the new shortCode is not in the database
        assertEquals("shortCode was in the database", null, fetchedShortCode);
    }


    @Test
    public void getByShortSecurityCode() throws ExecutionException, InterruptedException {
        //the code is essentially identical
        AddShortCodeTask();
    }

    @Test
    public void AddShortCodeTask() throws ExecutionException, InterruptedException {
        //create new shortCode
        newShortCode = new ShortCode(SecurityTokenForTest);

        //add new shortCode to the shortCode database
        new ElasticsearchShortCodeController.AddShortCodeTask().execute(newShortCode).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch from the shortCode database
        ShortCode fetchedShortCode = new ElasticsearchShortCodeController.getByShortSecurityCodeTask()
                .execute(newShortCode.getShortSecurityCode()).get();

        // check that the new shortCode is now in the database by comparing UUID of
        // mock security token and fetched security token
        assertEquals("shortCodes were not equal",
                newShortCode.getSecurityToken().getUserSecurityToken(),
                fetchedShortCode.getSecurityToken().getUserSecurityToken());
    }
}