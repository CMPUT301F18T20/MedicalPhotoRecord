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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ModifyProviderController {
    ArrayList<Provider> providers;
    private BrowseUserController browseUserController = new BrowseUserController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();

    // GET PROVIDER
    public Provider getProvider(Context context, String userId) {

        // Initialize a stand by user in case user is not found (which is unlikely)
        Provider userNotFound = null;

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

    public void saveProvider(Context context, Provider newProvider) {

        // Get most updated list of provider
        this.providers = this.browseUserController.getProviderList(context);

        // Modify (Remove old user from user list, both offline and online) (offline: may not need this when syncing)
        for (Provider u : new ArrayList<Provider>(this.providers)) {
            if (newProvider.getUserID().equals(u.getUserID())) {
                this.providers.remove(u);
            }
        }

        // Offline saves (may not need this when syncing)
        this.providers.add(newProvider);
        offlineSaveController.saveProviderList(providers, context);

        // Online Saves
        try {

            new ElasticsearchProviderController.SaveModifiedProvider().execute(newProvider).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
