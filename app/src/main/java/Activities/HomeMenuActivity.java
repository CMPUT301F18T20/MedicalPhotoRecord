package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.ShortCode;
import com.cmput301f18t20.medicalphotorecord.User;

import Controllers.ShortCodeController;
import Exceptions.failedToAddShortCodeException;
import Exceptions.failedToFetchSecurityTokenException;

import static GlobalSettings.GlobalSettings.EMAILEXTRA;
import static GlobalSettings.GlobalSettings.PHONEEXTRA;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;

public abstract class HomeMenuActivity extends AppCompatActivity {
    protected String UserID;
    protected TextView UserIDWelcomeBox;
    protected User user;
    private ShortCode shortCode; //for use with testing, do not include in UML


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        //set up text edit
        UserIDWelcomeBox = findViewById(R.id.UserIDWelcomeBox);

        //extract the user id from the intent
        Intent intent = getIntent();
        UserID = intent.getStringExtra(USERIDEXTRA);

        //set the welcome message
        String newText = "Welcome " + UserID;
        UserIDWelcomeBox.setText(newText);
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(this, ModifyProviderActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        startActivity(intent);
    }

    public void onViewProfileClick(View v) {
        FetchUserFile();

        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(USERIDEXTRA, UserID);
        intent.putExtra(EMAILEXTRA, this.user.getEmail());
        intent.putExtra(PHONEEXTRA, this.user.getPhoneNumber());
        startActivity(intent);
    }

    //generate login code for another device to use for login using security token
    public void onGenerateCodeClick(View v) {
        try {
            this.shortCode = ShortCodeController.AddCode(this.UserID, this);
            Toast.makeText(this,
                    "Added code " + shortCode.getShortSecurityCode(), LENGTH_LONG).show();
        } catch(failedToFetchSecurityTokenException e) {
            Toast.makeText(this,
                    "Unable to load a security token for this user", LENGTH_LONG).show();
        } catch(failedToAddShortCodeException e) {
            Toast.makeText(this, "Unable to add the short code", LENGTH_LONG).show();
        }
    }

    public ShortCode getShortCode() {
        return shortCode;
    }

    //returns the id of the layout to load for onCreate.
    //Something like R.layout.activity_patient_home_menu
    abstract protected int getLayout();

    //return the class that one would start if they were going to modify this object
    //so like ModifyProviderActivity.class
    abstract protected Class<?> getModifyActivityClass();

    //get the user's file from the elasticsearch database
    abstract protected void FetchUserFile();
}
