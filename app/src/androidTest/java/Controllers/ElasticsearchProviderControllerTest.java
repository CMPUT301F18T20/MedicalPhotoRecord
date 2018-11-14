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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;

import static Controllers.ElasticsearchProviderController.matchAllquery;
import static org.junit.Assert.*;

//add a delete index method to the controllers for use with testing
class ElasticsearchProviderControllerForTesting extends ElasticsearchProviderController {
    //TODO REFACTOR INDEX CHOICE TO GLOBALSETTINGS

    public void DeleteProviders() throws IOException {
        setClient();

        client.execute(new DeleteByQuery.Builder(matchAllquery)
                .addIndex("cmput301f18t20") //TODO set by global settings
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

    ElasticsearchProviderControllerForTesting controller =
            new ElasticsearchProviderControllerForTesting();

    //remove all entries from Provider database
    @Before
    public void WipeProvidersDatabase() throws IOException, InterruptedException {
        controller.DeleteProviders();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);
    }

    @Test
    public void AddProviderTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException, IOException {

        //create new provider
        Provider newProvider = new Provider(ProviderIDToAddInAddTest);

        //fetch providers from database
        ArrayList<Provider> providers =
                new ElasticsearchProviderController.GetProviderTask().execute().get();

        //for entry in providers:
        for (Provider provider : providers) {

            //assert entry doesn't have our userID
            assertNotEquals("UserID already in database",
                    provider.getUserID(), newProvider.getUserID());
        }

        //add new provider to the provider database
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

        //re fetch from the provider database
        providers = new ElasticsearchProviderController.GetProviderTask().execute(newProvider.getUserID()).get();

        //check that the new provider is now in the database
        boolean newProviderInDatabase = false;
        for (Provider provider : providers) {
            if (provider.getUserID().equals(newProvider.getUserID())) {
                newProviderInDatabase = true;
                break;
            }
        }

        assertEquals("New Provider not in database", newProviderInDatabase, true);
    }

    @Test
    public void ProvidersHaveUniqueIDs() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        Provider newProvider = new Provider(ProviderIDForUniquenessTest);

        //add same provider twice
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

        //fetch providers
        ArrayList<Provider> providers =
                new ElasticsearchProviderController.GetProviderTask().execute().get();

        assertEquals("Should only be one entry in the results",
                providers.size(), 1);
    }

    @Test
    public void getProviderTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new provider
        Provider newProvider = new Provider(ProviderIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new provider to the provider database
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(5000);

        //re fetch from the provider database
        ArrayList<Provider> providers = new ElasticsearchProviderController.GetProviderTask()
                .execute(newProvider.getUserID()).get();

        assertTrue("providers array not at least 1 member long", providers.size() >= 1);

        Provider fetchedProvider = providers.get(0);

        assertEquals("fetched provider userID not equal",
                newProvider.getUserID(), fetchedProvider.getUserID());

        assertEquals("fetched provider email not equal",
                newProvider.getEmail(), fetchedProvider.getEmail());

        assertEquals("fetched provider phone not equal",
                newProvider.getPhoneNumber(), fetchedProvider.getPhoneNumber());
    }

    @Test
    public void getProvidersTest() throws ExecutionException,
            InterruptedException, UserIDMustBeAtLeastEightCharactersException {
        ArrayList<Provider> expectedProviders = new ArrayList<>();
        ArrayList<Boolean> expectedProviderInResults = new ArrayList<>();

        //add all expected users in
        for (String providerID : ProviderIDsToRetrieveInGetAllTest) {
            Provider newProvider = new Provider(providerID);

            //add new provider to expected returns
            expectedProviders.add(newProvider);
            expectedProviderInResults.add(false);

            //add new provider to the provider database
            new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

            //Ensure database has time to reflect the change
            Thread.sleep(5000);
        }

        //Get objects from database for all the entered provider IDs
        ArrayList<Provider> results = new ElasticsearchProviderController.GetProviderTask()
                .execute(ProviderIDsToRetrieveInGetAllTest).get();

        assertTrue("there should be as many results as providers we queried. We got " +
                        results.size() + " results instead of expected " +
                        ProviderIDsToRetrieveInGetAllTest.length,
                results.size() == ProviderIDsToRetrieveInGetAllTest.length);

        //get all users
        results = new ElasticsearchProviderController.GetProviderTask().execute().get();

        //compare results to what we expected to find.
        //The three providers we added should now be there
        for (Provider provider : results) {
            for (int i = 0; i < expectedProviders.size(); i++) {

                //track which expected providers are seen in the results
                if (provider.getUserID().equals(expectedProviders.get(i).getUserID())) {
                    expectedProviderInResults.set(i, true);
                }
            }
        }

        //check that we saw all the expected providers in the results
        for (boolean providerSeenInResults : expectedProviderInResults) {
            assertTrue("Provider missing from results", providerSeenInResults);
        }
    }

}