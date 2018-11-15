package Activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301f18t20.medicalphotorecord.R;

import GlobalSettings.GlobalSettings;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class PatientHomeMenuActivity extends AppCompatActivity {

    private String UserID;

    protected Button EditContactInfoButton,
            ListOfProblemsButton,
            ViewProfileButton,
            DeleteProfileButton,
            LogOutButton;

    protected TextView UserIDWelcomeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_menu);

        //set up the buttons and text edit
        EditContactInfoButton = findViewById(R.id.EditContactInfoButton);
        ListOfProblemsButton = findViewById(R.id.ListOfProblemsButton);
        ViewProfileButton = findViewById(R.id.ViewProfileButton);
        DeleteProfileButton = findViewById(R.id.DeleteProfile);
        LogOutButton = findViewById(R.id.LogOutButton);
        UserIDWelcomeBox = findViewById(R.id.UserIDWelcomeBox);

        //extract the user id from the intent
        Intent intent = getIntent();
        UserID = intent.getStringExtra(USERIDEXTRA);

        UserIDWelcomeBox.setText("Welcome " + UserID);
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(this, ModifyUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onListOfProblemsClick(View v) {
        Intent intent = new Intent(this, BrowseUserProblems.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onViewProfileClick(View v) {
        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onDeleteClick(View v) {
        //create fragment that comes up and asks if the user is sure
    }

    public void onLogoutClick(View v) {
        finish();
    }


}
