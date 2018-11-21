package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;

import java.util.concurrent.ExecutionException;
import Exceptions.NoSuchUserException;

import Controllers.LoginController;
import GlobalSettings.GlobalSettings;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class Login extends AppCompatActivity {

    protected Button LoginButton, SignUpButton;
    protected EditText UserIDText;

    public final static int REQUEST_CODE_SIGNUP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.LoginButton);
        SignUpButton = findViewById(R.id.SignUpButton);
        UserIDText = findViewById(R.id.UserIDText);
    }

    /**Run when the Login button is clicked on.  On successful login, transitions to home screen
     * for Provider or Patient.
     * @param view Unused as function only applies to one view anyways
     */
    public void onLoginClick(View view) {
        String userID = UserIDText.getText().toString();
        startCorrectActivity(userID);
    }

    public void startCorrectActivity(String userID) {
        try {

            Intent intent;

            //Login controller tells us which type of user just logged in
            //This function will throw a NoSuchUserException if the user doesn't exist
            switch (LoginController.WhichActivityToStartAfterLogin(userID)) {
                case PATIENT:
                    intent = new Intent(this, PatientHomeMenuActivity.class);
                    break;
                case PROVIDER:
                    intent = new Intent(this, ProviderHomeMenuActivity.class);
                    break;
                default:
                    throw new NoSuchUserException();
            }

            //add user id to the intent
            intent.putExtra(USERIDEXTRA, userID);

            startActivity(intent);

        } catch (NoSuchUserException e) {
            Toast.makeText(this, "User not found",
                    Toast.LENGTH_LONG).show();

        } catch (InterruptedException e) {
            Toast.makeText(this, "Was interrupted, please try again",
                    Toast.LENGTH_LONG).show();

        } catch (ExecutionException e) {
            Toast.makeText(this, "Execution Exception, please try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**Run when the Sign up button is clicked on.  Transitions to sign up page.
     * @param view Unused as function only applies to one view anyways
     */
    public void onSignUpClick(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if the return is from a successful signup, set the userID entry to the newly created user
        if (requestCode == REQUEST_CODE_SIGNUP) {
            if (resultCode == RESULT_OK) {
                UserIDText.setText(
                        data.getStringExtra(USERIDEXTRA));
            }
        }
    }
}
