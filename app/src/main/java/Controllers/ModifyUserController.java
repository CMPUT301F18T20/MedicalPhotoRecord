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

    private ArrayList<User> users;
    private ElasticsearchPatientController elasticsearchPatientController = new ElasticsearchPatientController();
    private OfflineSaveController offlineSaveController = new OfflineSaveController();
    private OfflineLoadController offlineLoadController = new OfflineLoadController();
    private Context context;


    public ModifyUserController(Context context){

        // Offline
        this.context = context;
        this.users = offlineLoadController.loadPatientList(context);
    }

    public User getUser(int position){
        return this.users.get(position);
    }

    public void saveUser(Context context, int position, String gotUserId, String gotEmail, String gotPhone){

        // Offline local Saves

        // Remove old user from user list
        this.users = offlineLoadController.loadPatientList(context);
        this.users.remove(position);

        // Create new user to be added
        User user = null;
        try {
            user = new User(gotUserId, gotEmail, gotPhone);

            // Save to database
            this.users.add(user);
            offlineSaveController.savePatientList(users,context);

            Toast.makeText(context, "Your infos have been saved locally",Toast.LENGTH_LONG).show();

        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(context, "User id has to be longer than 8 characters",Toast.LENGTH_LONG).show();
        }

    }
}
