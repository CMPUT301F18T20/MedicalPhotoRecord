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

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import io.searchbox.core.Delete;

import static org.junit.Assert.*;

//add a delete index method to the controllers for use with testing
class ElasticsearchProviderControllerForTesting extends ElasticsearchProviderController {
    //TODO REFACTOR INDEX CHOICE TO GLOBALSETTINGS

    public void DeleteProviders() throws IOException {
        setClient();
        client.execute(new Delete.Builder("1")
                .index("cmput301f18t20test") //TODO set by global settings
                .type("Provider")
                .build());
    }
}

public class ElasticsearchProviderControllerTest {

    private String ProviderIDToAddInAddTest = "ImFromTheProviderAddTest";
    private String ProviderIDToGetInGetTest = "ImFromTheProviderGetTest";
    private String[] ProviderIDsToRetrieveInGetAllTest = {
            "ImFromProviderGetAllTest1",
            "ImFromProviderGetAllTest2",
            "ImFromProviderGetAllTest3"
    };

    ElasticsearchProviderControllerForTesting controller =
            new ElasticsearchProviderControllerForTesting();

    //@Before
    //TODO can't seem to get this to work.. does it HAVE to be an async task?
    public void WipeProvidersDatabase() throws IOException {
        controller.DeleteProviders();
    }

    @Test
    public void AddProviderTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {

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
    public void ProvidersHaveUniqueIDs() throws ExecutionException, InterruptedException {
        //should be executed on main index as that should have more examples
        ArrayList<Provider> providers =
                new ElasticsearchProviderController.GetProviderTask().execute().get();

        //providerSet will contain only the unique elements of providers
        HashSet<Provider> providersSet = new HashSet<>(providers);

        assertEquals("providers and providersSet were not the same size",
                providers.size(), providersSet.size());

        fail("This should not be passing yet and the fact that it is makes me suspicious");
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