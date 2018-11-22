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

import android.support.annotation.NonNull;

import com.cmput301f18t20.medicalphotorecord.Provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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
    private String
            ProviderIDForModifyTest = "ImFromModifyTest",
            ProviderOriginalEmail = "Original@gmail.com",
            ProviderOriginalPhone = "780-555-1234",
            ProviderModifiedEmail = "Modified@gmail.com",
            ProviderModifiedPhone = "587-555-9876";

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
        Thread.sleep(ControllerTestTimeout);

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
    //fail
    public void ProvidersHaveUniqueIDs() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        Provider newProvider = new Provider(ProviderIDForUniquenessTest);

        //add same provider twice
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //fetch providers
        ArrayList<Provider> providers =
                new ElasticsearchProviderController.GetProviderTask().execute().get();

        assertEquals("Should only be one entry in the results",
                1, providers.size());
    }

    @Test
    //pass
    public void getProviderTest() throws ExecutionException, InterruptedException,
            UserIDMustBeAtLeastEightCharactersException {
        //On test index
        //create new provider
        Provider newProvider = new Provider(ProviderIDToGetInGetTest,
                "Hello@gmail.com", "7805551234");

        //add new provider to the provider database
        new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

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

    private void AssertProvidersCanBeAddedAndThenBatchFetched(@NonNull String[] suppliedUserIDs)
            throws ExecutionException, UserIDMustBeAtLeastEightCharactersException,
            InterruptedException {
        ArrayList<Provider> expectedProviders = new ArrayList<>();
        ArrayList<Boolean> expectedProviderInResults = new ArrayList<>();

        //add all expected users in
        for (String providerID : suppliedUserIDs) {
            Provider newProvider = new Provider(providerID);

            //add new provider to expected returns
            expectedProviders.add(newProvider);
            expectedProviderInResults.add(false);

            //add new provider to the provider database
            new ElasticsearchProviderController.AddProviderTask().execute(newProvider).get();
        }

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //make sure each of the added users is individually fetchable
        for (int i = 0; i < suppliedUserIDs.length; i++) {
            //fetch new Provider from the Provider database
            ArrayList<Provider> Providers = new ElasticsearchProviderController
                    .GetProviderTask().execute(suppliedUserIDs[i]).get();

            //grab the first Provider from the result (test will fail if there's no
            //results in output, which is good)
            Provider Provider = Providers.get(0);

            assertEquals("Fetched Provider was different from one added",
                    Provider.getUserID(), expectedProviders.get(i).getUserID());

        }

        //Get objects from database for all the entered provider IDs
        ArrayList<Provider> results = new ElasticsearchProviderController.GetProviderTask()
                .execute(suppliedUserIDs).get();

        //test for bug https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161
        if (suppliedUserIDs.length > 10 && results.size() == 10) {
            assertTrue("IF SEEN, REOPEN BUG https://github.com/CMPUT301F18T20/MedicalPhotoRecord/issues/161 " +
                            "there should be as many results as providers we queried. We got exactly " +
                            "ten results instead of expected " + suppliedUserIDs.length,
                    results.size() == suppliedUserIDs.length);
        }

        assertTrue("there should be as many results as providers we queried. We got " +
                        results.size() + " results instead of expected " +
                        suppliedUserIDs.length,
                results.size() == suppliedUserIDs.length);

        //get all users
        results = new ElasticsearchProviderController.GetProviderTask().execute().get();

        //compare results to what we expected to find.
        //The providers we added should now be there
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


    @Test
    public void modifyProviderSavesChanges() throws UserIDMustBeAtLeastEightCharactersException,
            InterruptedException, ExecutionException {
        Provider provider = new Provider(ProviderIDForModifyTest,
                ProviderOriginalEmail,
                ProviderOriginalPhone);

        new ElasticsearchProviderController.AddProviderTask().execute(provider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //modify user
        provider.setEmail(ProviderModifiedEmail);
        provider.setPhoneNumber(ProviderModifiedPhone);

        //save modification
        new ElasticsearchProviderController.SaveModifiedProvider().execute(provider).get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);

        //get the returned provider, hopefully modified
        Provider returnedProvider = new ElasticsearchProviderController.
                GetProviderTask().execute(provider.getUserID()).get().get(0);

        //check the object was changed
        assertEquals(ProviderModifiedEmail, returnedProvider.getEmail());
        assertEquals(ProviderModifiedPhone, returnedProvider.getPhoneNumber());
    }

}