/*
 * Class name: ModifyProviderControllerTest
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 03/12/18 1:00 AM
 *
 * Last Modified: 03/12/18 1:00 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;
import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Provider;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.AddRecordActivity;
import Enums.INDEX_TYPE;
import Exceptions.NoSuchUserException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import GlobalSettings.GlobalSettings;
import androidx.test.rule.ActivityTestRule;

import static GlobalSettings.GlobalTestSettings.ControllerTestTimeout;
import static junit.framework.TestCase.assertEquals;


public class ModifyProviderControllerTest {

    @After
    @Before
    public void WipeOnlineDatabase() throws ExecutionException, InterruptedException {
        //make sure we are using the testing index instead of main index
        GlobalSettings.INDEXTYPE = INDEX_TYPE.TEST;

        new ElasticsearchProviderController.DeleteProvidersTask().execute().get();

        //Ensure database has time to reflect the change
        Thread.sleep(ControllerTestTimeout);
    }

    @After
    @Before
    public void wipeOfflineDatabase(){
        Context context = AddRecordActivity.getActivity().getBaseContext();
        ArrayList<Provider> emptyProviders = new ArrayList<>();
        new OfflineSaveController().saveProviderList(emptyProviders, context);
    }

    @Rule
    public ActivityTestRule<AddRecordActivity> AddRecordActivity =
            new ActivityTestRule<>(AddRecordActivity.class);

    @Test
    public void testGetProvider() throws
            UserIDMustBeAtLeastEightCharactersException, ExecutionException,
            InterruptedException, NoSuchUserException {

        Context context = AddRecordActivity.getActivity().getBaseContext();

        // Create new patient
        Provider testProvider = new Provider("testProvider","testEmail","1111111111");

        // Save them to online and offline database
        new ElasticsearchProviderController.AddProviderTask().execute(testProvider).get();
        Thread.sleep(ControllerTestTimeout);
        ArrayList<Provider> providers = new ArrayList<>();
        providers.add(testProvider);
        new OfflineSaveController().saveProviderList(providers,context);


        // Test getProvider
        Provider onlineProvider = new ModifyProviderController().getProvider(context, testProvider.getUserID());
        Provider offlineProvider = new ModifyProviderController().getProvider(context, testProvider.getUserID());


        // Compare 3 objects, convert to gson string since date is giving some problem
        String providerString = new Gson().toJson(testProvider);
        String onlineProviderString = new Gson().toJson(onlineProvider);
        String offlineProviderString = new Gson().toJson(offlineProvider);
        assertEquals("provider got from online database is not the same", providerString, onlineProviderString);
        assertEquals("provider got from offline database is not the same", providerString, offlineProviderString);

    }

    @Test
    public void testSaveModifiedProvider() throws UserIDMustBeAtLeastEightCharactersException, ExecutionException, InterruptedException {

        Context context = AddRecordActivity.getActivity().getBaseContext();

        // Create new patient and modified patient
        Provider testProvider = new Provider("testProvider","","");
        String newEmail = "modEmail";
        String newNumber = "2222222222";

        // Save them to online and offline database
        new ElasticsearchProviderController.AddProviderTask().execute(testProvider).get();
        Thread.sleep(ControllerTestTimeout);
        ArrayList<Provider> providers = new ArrayList<>();
        providers.add(testProvider);
        new OfflineSaveController().saveProviderList(providers,context);

        // Test saveModifyPatient
        new ModifyProviderController().saveModifiedProvider(context,testProvider,newEmail,newNumber);
        Thread.sleep(ControllerTestTimeout);

        // Compare 3 objects, convert to gson string since date is giving some problem
        testProvider.setEmail(newEmail);
        testProvider.setPhoneNumber(newNumber);

        Provider onlineProvider = new ElasticsearchProviderController.GetProviderTask().execute(testProvider.getUserID()).get().get(0);
        Provider offlineProvider = getOfflineProvider(context, testProvider.getUserID());

        String testProviderString = new Gson().toJson(testProvider);
        String onlineProviderString = new Gson().toJson(onlineProvider);
        String offlineProviderString = new Gson().toJson(offlineProvider);
        assertEquals("modified online providers are not the same", testProviderString, onlineProviderString);
        assertEquals("modified offline providers are not the same", testProviderString, offlineProviderString);
    }

    private Provider getOfflineProvider(Context context, String userID){
        ArrayList<Provider> providers1 = new OfflineLoadController().loadProviderList(context);
        for (Provider p : providers1) {
            if (userID.equals(p.getUserID())) {
                return p;
            }
        }
        return null;
    }

}
