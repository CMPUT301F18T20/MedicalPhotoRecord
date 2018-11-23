package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.ArrayList;

import Controllers.ElasticsearchPatientController;
import Controllers.ElasticsearchProviderController;

import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class ProviderHomeMenuActivity extends AppCompatActivity {

    private String UserID;

    private Provider provider = null;

    protected Button EditContactInfoButton,
            ListOfPatientsButton,
            ViewProfileButton,
            DeleteProfileButton,
            LogOutButton;

    protected TextView UserIDWelcomeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home_menu);

        //set up the buttons and text edit
        EditContactInfoButton = findViewById(R.id.EditContactInfoButton);
        ListOfPatientsButton = findViewById(R.id.ListOfPatientsButton);
        ViewProfileButton = findViewById(R.id.ViewProfileButton);
        DeleteProfileButton = findViewById(R.id.DeleteProfile);
        LogOutButton = findViewById(R.id.LogOutButton);
        UserIDWelcomeBox = findViewById(R.id.UserIDWelcomeBox);

        //extract the user id from the intent
        Intent intent = getIntent();
        UserID = intent.getStringExtra(USERIDEXTRA);
        String newText = "Welcome " + UserID;

        UserIDWelcomeBox.setText(newText);

        FetchProviderFile();
    }

    private void FetchProviderFile() {
        try {
            //get the user info for the signed in patient
            ArrayList<Provider> providers = new ElasticsearchProviderController.GetProviderTask().execute(UserID).get();

            //grab the first (and hopefully only) provider in the results
            this.provider = providers.get(0);

        } catch (Exception e) {
            Toast.makeText(this, "Exception while fetching patient file from database",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void onEditClick(View v) {
        Intent intent = new Intent(this, ModifyProviderActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onListOfPatientsClick(View v) {
        Intent intent = new Intent(this, BrowseUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onViewProfileClick(View v) {
        FetchProviderFile();

        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        intent.putExtra(EMAILEXTRA, provider.getEmail());
        intent.putExtra(PHONEEXTRA, provider.getPhoneNumber());
        startActivity(intent);
    }

    public void onDeleteClick(View v) {
        //create fragment that comes up and asks if the user is sure
    }

    public void onLogoutClick(View v) {
        finish();
    }


}
