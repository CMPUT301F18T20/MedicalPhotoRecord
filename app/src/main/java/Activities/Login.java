package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301f18t20.medicalphotorecord.R;

public class Login extends AppCompatActivity {

    protected Button LoginButton, SignUpButton;
    protected EditText UserIDText;

    public final static int REQUEST_CODE_SIGNUP = 1;
    public static final String USER_ID_EXTRA = "UserID";

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
        LoginButton.setBackgroundColor(0xFF476B00);
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
                        data.getStringExtra(USER_ID_EXTRA));
            }
        }
    }
}
