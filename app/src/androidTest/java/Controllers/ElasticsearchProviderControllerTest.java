/*
 * Class name: ElasticsearchProviderControllerForTesting
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 13/11/18 8:00 PM
 *
 * Last Modified: 13/11/18 6:48 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import com.cmput301f18t20.medicalphotorecord.Provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import Enums.INDEX_TYPE;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import io.searchbox.core.DeleteByQuery;

import static GlobalSettings.GlobalSettings.getIndex;
import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static org.junit.Assert.*;

//add a delete type method to the controllers for use with testing
class ElasticsearchProviderControllerForTesting extends ElasticsearchProviderController {

    public void DeleteProviders() throws IOException {
        setClient();

        client.execute(new DeleteByQuery.Builder(matchAllquery)
                .addIndex(getIndex())
                .addType("Provider")
                .build());
    }
}

public class ElasticsearchProviderControllerTest {

    private String
            ProviderIDToAddInAddTest = "ImFromTheProviderAddTest",
            ProviderIDToGetInGetTest = "ImFromTheProviderGetTest",
            ProviderIDForUniquenessTest = "ImFromTheProviderUniquenessTest";
    private String[] ProviderIDsToRetrieveInGetAllTest = {
            "ImFromProviderGetAllTest1",
            "ImFromProviderGetAllTest2",
            "ImFromProviderGetAllTest3"
    };

    private String[] ProviderIDsToRetrieveInGetAllBUGTest = {
            "ImFromProviderGetAllBUGTest1",
            "ImFromProviderGetAllBUGTest2",
            "ImFromProviderGetAllBUGTest3",
            "ImFromProviderGetAllBUGTest4",
            "ImFromProviderGetAllBUGTest5",
            "ImFromProviderGetAllBUGTest6",
            "ImFromProviderGetAllBUGTest7",
            "ImFromProviderGetAllBUGTest8",
            "ImFromProviderGetAllBUGTest9",
            "ImFromProviderGetAllBUGTest10",
            "ImFromProviderGetAllBUGTest11",
            "ImFromProviderGetAllBUGTest12",
            "ImFromProviderGetAllBUGTest13",
            "ImFromProviderGetAllBUGTest14",
    };

    //set index to testing index and remove all entries from Provider database
    @After
    @Before
    public void WipeProvidersDatabase() throws IOException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProviderControllerForTesting().DeleteProviders();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @Test
    //pass
    public void AddProviderTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, IOException {

        //create new Provider
        Provider newProvider = new Provider(ProviderIDToAddInAddTest);

        //fetch Providers from database
        ArrayList<Provider> Providers =
                new ElasticsearchProviderController.GetProviderTask().execute().get();

        //for entry in Providers:
        for (Provider Provider : Providers) {

            //assert entry doesn't have our userID
            assertNotEquals("UserID already in database",
                    Provider.getUserID(), newProvider.getUserID());
        }

        //add new Provider to the Provider database
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the Provider database
        Providers = new ElasticsearchProviderController.GetProviderTask().execute(newProvider.getUserID()).get();

        //check that the new Provider is now in the database
        boolean newProviderInDatabase = false;
        for (Provider Provider : Providers) {
            if (Provider.getUserID().equals(newProvider.getUserID())) {
                newProviderInDatabase = true;
                break;
            }
        }

        assertEquals("New Provider not in database", newProviderInDatabase, true);
    }

    @Test
    //fail
    public void ProvidersHaveUniqueIDs() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        Provider newProvider = new Provider(ProviderIDForUniquenessTest);

        //add same Provider twice
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch Providers
        ArrayList<Provider> Providers =
                new ElasticsearchProviderController.GetProviderTask().execute().get();

        assertEquals("Should only be one entry in the results",
                1, Providers.size());
    }

    @Test
    //pass
    public void getProviderTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new Provider
        Provider newProvider = new Provider(ProviderIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new Provider to the Provider database
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //re fetch from the Provider database
        ArrayList<Provider> Providers = new ElasticsearchProviderController.GetProviderTask()
                .execute(newProvider.getUserID()).get();

        assertTrue("Providers array not at least 1 member long", Providers.size() >= 1);

        Provider fetchedProvider = Providers.get(0);

        assertEquals("fetched Provider userID not equal",
                newProvider.getUserID(), fetchedProvider.getUserID());

        assertEquals("fetched Provider email not equal",
                newProvider.getEmail(), fetchedProvider.getEmail());

        assertEquals("fetched Provider phone not equal",
                newProvider.getPhoneNumber(), fetchedProvider.getPhoneNumber());
    }

    @Test
    //pass
    public void getProvidersTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        AssertProvidersCanBeAddedAndThenBatchFetched(ProviderIDsToRetrieveInGetAllTest);
    }

    @Test
    //fail
    public void getProvidersBUGTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {

        //Can't fetch more than 10 results right now, this checks for the existence of that bug
        AssertProvidersCanBeAddedAndThenBatchFetched(ProviderIDsToRetrieveInGetAllBUGTest);
    }

    private void AssertProvidersCanBeAddedAndThenBatchFetched(String[] suppliedUserIDs)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException {
        ArrayList<Provider> expectedProviders = new ArrayList<>();
        ArrayList<Boolean> expectedProviderInResults = new ArrayList<>();

        //add all expected users in
        for (String ProviderID : suppliedUserIDs) {
            Provider newProvider = new Provider(ProviderID);

            //add new Provider to expected returns
            expectedProviders.add(newProvider);
            expectedProviderInResults.add(false);

            //add new Provider to the Provider database
            new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //Get objects from database for all the entered Provider IDs
        ArrayList<Provider> results = new ElasticsearchProviderController.GetProviderTask()
                .execute(suppliedUserIDs).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedUserIDs.length > 10 && results.size() == 10) {
            assertTrue("BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as Providers we queried. We got " +
                            results.size() + " results instead of expected " +
                            suppliedUserIDs.length,
                    results.size() == suppliedUserIDs.length);
        }

        assertTrue("there should be as many results as Providers we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedUserIDs.length,
                results.size() == suppliedUserIDs.length);

        //get all users
        results = new ElasticsearchProviderController.GetProviderTask().execute().get();

        //compare results to what we expected to find.
        //The three Providers we added should now be there
        for (Provider Provider : results) {
            for (int i = 0; i < expectedProviders.size(); i++) {

                //track which expected Providers are seen in the results
                if (Provider.getUserID().equals(expectedProviders.get(i).getUserID())) {
                    expectedProviderInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected Providers in the results
        for (boolean ProviderSeenInResults : expectedProviderInResults) {
            assertTrue("Provider missing from results", ProviderSeenInResults);
        }
    }

}