/*
 * Class name: ModifyProviderController
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/15/18 4:14 PM
 *
 * Last Modified: 11/15/18 4:14 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Controllers;

import android.content.Context;
import com.cmput301f18t20.medicalphotorecord.Provider;

import java.security.Policy;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.NoSuchUserException;

/**
 * ModifyProviderController
 * Can get provider object from userID
 * Can save modified provider object to online and offline database
 * @version 2.0
 * @see Provider
 */
public class ModifyProviderController {

    /**
     * Get provider object from appropriate database (online when there's wifi, offline when there's no wifi)
     * @param context: activity to be passed for offline save and load
     * @param userId
     * @return actualProvider object correspond to userID
     * @throws NoSuchUserException: if provider is not found in databases
     */
    public Provider getProvider(Context context, String userId) throws NoSuchUserException {

        // Online
        Provider onlineProvider = null;
        try {
            // Check if online provider exists
            ArrayList<Provider> onlineProviders = new ElasticsearchProviderController.GetProviderTask().execute(userId).get();
            if (onlineProviders.size() > 0){
                onlineProvider = onlineProviders.get(0);
            }else{
                throw new NoSuchUserException();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        Provider offlineProvider = null;
        ArrayList<Provider> providers = new OfflineLoadController().loadProviderList(context);
        for (Provider p:providers){
            if (userId.equals(p.getUserID())){
                offlineProvider = p;
            }
        }

        // Check if offline provider exists
        if (offlineProvider == null){
            throw new NoSuchUserException();
        }

        // Syncing
        Provider actualProvider = onlineProvider;
        return actualProvider;
    }


    /**
     * Takes in old provider, new modified email, new modified phone number
     * Sets new information to provider object
     * Save provider object to both online and offline database
     * @param context: activity to be passed for offline save and load
     * @param provider
     * @param email
     * @param phoneNumber
     */
    public void saveModifiedProvider(Context context, Provider provider, String email, String phoneNumber) {

        // Modify
        provider.setEmail(email);
        provider.setPhoneNumber(phoneNumber);

        // Online
        try {
            new ElasticsearchProviderController.SaveModifiedProvider().execute(provider).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Offline
        ArrayList<Provider> providers = new OfflineLoadController().loadProviderList(context);
        for (Provider p:new ArrayList<>(providers)){
            if (p.getUserID().equals(provider.getUserID())){
                providers.remove(p);
            }
        }
        providers.add(provider);
        new OfflineSaveController().saveProviderList(providers, context);
    }
}
