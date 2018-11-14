package Controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;

import Activities.BrowseUserActivity;
import Activities.ModifyUserActivity;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class ModifyUserController {

    private ArrayList<Patient> users;
    private ElasticsearchPatientController elasticsearchPatientController = new ElasticsearchPatientController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();
    private OfflineLoadController offlineLoadController = new OfflineLoadController();
    private Context context;


    public ModifyUserController(Context context){

        // Offline
        this.context = context;
    }

    public User getUser(String userId){

        // Initialize a stand by user in case user is not found (which is unlikely)
        User userNotFound = null;

        // Get user List, get user from userId
        this.users = offlineLoadController.loadPatientList(this.context);
        for (User user:this.users){
            if (user.getUserID() == userId){
                return user;
            }
        }
        return userNotFound;
    }

    public void saveUser(Context context, String userId, String gotEmail, String gotPhone){

        // Offline local Saves
        // Remove old user from user list
        this.users = offlineLoadController.loadPatientList(context);

        // Modify
        for (User u:this.users){
            if (u.getUserID() == userId){
                this.users.remove(u);
            }
        }

        // Create new user to be added
        User user = null;
        try {
            user = new User(userId, gotEmail, gotPhone);

            // Save to database
            this.users.add(user);
            offlineSaveController.savePatientList(users,context);

            Toast.makeText(context, "Your infos have been saved locally",Toast.LENGTH_LONG).show();

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(context, "User id has to be longer than 8 characters",Toast.LENGTH_LONG).show();
        }

        // Elastic search Saves


    }
}
