package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.SecurityToken;

import java.io.FileNotFoundException;

import Controllers.IOLocalSecurityTokenController;
import Enums.USER_TYPE;

import static Enums.USER_TYPE.PATIENT;
import static GlobalSettings.GlobalSettings.USERIDEXTRA;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class CheckSecurityToken extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_security_token);
    }

    @Override
    public void onResume() {
        super.onResume();
        launchCorrectActivity();
    }

    public void launchCorrectActivity() {
        //attempt to load the security token from local storage and start the relevant activity
        try {
            SecurityToken securityToken = IOLocalSecurityTokenController
                    .loadSecurityTokenFromDisk(this);

            Intent intent;

            //load user id from security token
            String UserID = securityToken.getUserID();

            //launch the correct activity for the user.  If the security token indicates user
            //type, then launch that activity for them, otherwise, launch login.
            switch (securityToken.getUserType()) {
                case PATIENT :
                    intent = new Intent(this, PatientHomeMenuActivity.class);
                    break;
                case PROVIDER:
                    intent = new Intent(this, ProviderHomeMenuActivity.class);
                    break;
                default:
                    intent = new Intent(this, Login.class);
                    Toast.makeText(this,
                            "Unable to determine user type from token", LENGTH_LONG).show();
            }

            //put userID into the intent
            intent.putExtra(USERIDEXTRA, UserID);

            startActivity(intent);

        } catch (FileNotFoundException e) {

            Toast.makeText(this, "Unable to load existing user file", LENGTH_LONG).show();
            //start login by default
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}
