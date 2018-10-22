package com.cmput301f18t20.medicalphotorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    protected Button LoginButton;
    protected Button SignUpButton;
    protected EditText UserIDText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.LoginButton);
        SignUpButton = findViewById(R.id.SignUpButton);
        UserIDText = findViewById(R.id.UserIDText);

        /* Needed for when a user focuses on the User ID text box */
        /* clear default text if it's in the box */
        UserIDText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onUserIDClick(v);
                }
            }
        });
    }

    /**Run when the UserID box is brought into focus or clicked on
     * @param view Unused as function only applies to one view anyways
     */
    public void onUserIDClick(View view) {
        /* clear default text if it's in the box */
        if (UserIDText.getText().toString().equals(
                this.getText(R.string.userID_prompt).toString())) {
            UserIDText.setText("");
        }
    }

    /**Run when the Login button is clicked on.  On successful login, transitions to home screen
     * for Provider or Patient.
     * @param view Unused as function only applies to one view anyways
     */
    public void onLoginClick(View view) {
        LoginButton.setBackgroundColor(0xFF476B00);

        //wait 1 second
        LoginButton.postDelayed(new Runnable() {

            @Override
            public void run() {
                LoginButton.setBackgroundColor(0xFF669900);
            }
        }, 1000);
    }

    /**Run when the Sign up button is clicked on.  Transitions to sign up page.
     * @param view Unused as function only applies to one view anyways
     */
    public void onSignUpClick(View view) {
        SignUpButton.setBackgroundColor(0xFF00759C);

        //wait 1 second
        SignUpButton.postDelayed(new Runnable() {

            @Override
            public void run() {
                SignUpButton.setBackgroundColor(0xFF0099CC);
            }
        }, 1000);
    }
}
