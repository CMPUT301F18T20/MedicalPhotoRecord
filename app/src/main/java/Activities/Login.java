package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.R;
import Controllers.ShortCodeController;
import Exceptions.NoSuchCodeException;
import Exceptions.failedToFetchShortCodeException;

import static android.widget.Toast.LENGTH_LONG;

public class Login extends AppCompatActivity {

    protected EditText CodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CodeText = findViewById(R.id.CodeText);
    }

    /**Run when the Login button is clicked on. Checks the code entered to see if it has
     * been generated from another device for sign on. On successful login, transitions to home screen
     * for Provider or Patient through
     * @param view Unused as function only applies to one view anyways
     */
    public void onLoginClick(View view) {
        String code = CodeText.getText().toString();

        if (code.length() > 0) {

            try {
                ShortCodeController.GetAndStoreSecurityToken(code, this);
                //allow the check security token task to handle getting the user
                //all logged into the correct activity
                Intent intent = new Intent(this, CheckSecurityToken.class);
                startActivity(intent);

            } catch (failedToFetchShortCodeException e) {
                Toast.makeText(this,
                        "Failed to access online database, please try again", LENGTH_LONG).show();
            } catch (NoSuchCodeException e) {
                Toast.makeText(this,
                        "Incorrect code, please try again", LENGTH_LONG).show();
            }
        }
    }

    /**Run when the Sign up button is clicked on.  Transitions to sign up page.
     * @param view Unused as function only applies to one view anyways
     */
    public void onSignUpClick(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
