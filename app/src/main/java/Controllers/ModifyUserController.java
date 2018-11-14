package Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Activities.BrowseUserActivity;
import Activities.ModifyUserActivity;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyUserController {

    private ArrayList<Patient> users;
    private Context context;


    public ModifyUserController(Context context) {

        // Offline
        this.context = context;
    }

    public ArrayList<Patient> getUsers() {

        // Offline
        this.users = new OfflineLoadController().loadPatientList(this.context);

        // Online
        try {
            this.users = new ElasticsearchPatientController.GetPatientTask().execute().get();
        } catch (InterruptedException e) {
            Log.d("ElasticsearchProviderCo",
                    "Computation threw an exception. " + context.toString());
            Log.d("ElasticsearchProviderCo", e.getStackTrace().toString());
        } catch (ExecutionException e) {
            Log.d("ElasticsearchProviderCo",
                    "Current thread was interrupted while waiting. " + context.toString());
            Log.d("ElasticsearchProviderCo", e.getStackTrace().toString());
        }

        // Need some function to sync this
        return this.users;
    }

    public User getUser(String userId) {

        // Initialize a stand by user in case user is not found (which is unlikely)
        User userNotFound = null;

        // Get user List, get user from userId
        this.users = getUsers();
        for (User user : this.users) {
            if (userId.equals(user.getUserID())) {
                return user;
            }
        }
        return userNotFound;
    }

    public void saveUser(Context context, String userId, String gotEmail, String gotPhone) {

        // Offline local Saves
        // Remove old user from user list
        this.users = new OfflineLoadController().loadPatientList(context);

        // Modify
        for (User u : this.users) {
            if (u.getUserID() == userId) {
                this.users.remove(u);
            }
        }

        // Create new user to be added
        Patient user = null;
        try {
            user = new Patient(userId, gotEmail, gotPhone);

            // Save to database
            this.users.add(user);
            new OfflineSaveController().savePatientList(users, context);

            Toast.makeText(context, "Your infos have been saved locally", Toast.LENGTH_LONG).show();

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(context, "User id has to be longer than 8 characters", Toast.LENGTH_LONG).show();
        }

        // Elastic search Saves


    }
}
