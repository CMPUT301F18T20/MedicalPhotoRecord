package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.User;

import java.util.ArrayList;

public class BrowseUserController {

    private ArrayList<User> users;
    private ElasticsearchPatientController elasticsearchPatientController = new ElasticsearchPatientController();
    private OfflineLoadController offlineLoadController = new OfflineLoadController();


    public ArrayList<User> getUserList(Context context){

        // Offline
        this.users = this.offlineLoadController.loadPatientList(context);
        return this.users;
    }
}
