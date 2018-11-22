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
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Provider;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyProviderController {

    // GET PROVIDER
    public Provider getProvider(Context context, String userId) {

        // Initialize a stand by user in case user is not found (which is unlikely)
        Provider userNotFound = null;

        // Offline
        ArrayList<Provider> providers = new BrowseUserController().getProviderList(context);
        for (Provider user : providers) {
            if (userId.equals(user.getUserID())) {
                return user;
            }
        }

        // Online
        try {
            Provider onlineProvider = (new ElasticsearchProviderController.GetProviderTask().execute(userId).get()).get(0);
            return onlineProvider;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userNotFound;
    }


    // SAVE MODIFIED PROVIDER (COULD ALSO BE USED TO ADD)
    public void saveProvider(Context context, String userId, String email, String phone) {

        // Get most updated list of providers and create a provider
        ArrayList<Provider> providers = new BrowseUserController().getProviderList(context);

        // Modify (Remove old user from user list, both offline and online) (offline: may not need this when syncing)
        for (Provider u : new ArrayList<>(providers)) {
            if (userId.equals(u.getUserID())) {
                providers.remove(u);
            }
        }

        Provider provider = null;
        try {
            provider = new Provider(userId, email, phone);

            // Offline saves (may not need this when syncing)
            providers.add(provider);
            new OfflineSaveController().saveProviderList(providers, context);

            // Elastic search Saves
            new ElasticsearchProviderController.DeleteProvidersTask().execute(provider.getUserID());
            new ElasticsearchProviderController.AddProviderTask().execute(provider);

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            e.printStackTrace();
        }

    }
}
