package com.cmput301f18t20.medicalphotorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.searchly.jestdroid.DroidClientConfig;

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
    }

    /**Run when the Login button is clicked on.  On successful login, transitions to home screen
     * for Provider or Patient.
     * @param view Unused as function only applies to one view anyways
     */
    public void onLoginClick(View view) {
        LoginButton.setBackgroundColor(0xFF476B00);

        DroidClientConfig config = new DroidClientConfig
                .Builder("http://cmput301.softwareprocess.es:8080/")
                .build();

/*
        try {
            Record record = new Record("99448855");
            record.setComment("890");
            new ElasticsearchRecordsController.AddRecordTask().execute(new Record("99113344"));
        } catch (NonNumericUserIDException e) {
            e.printStackTrace();
        } catch (CommentTooLongException e) {
            e.printStackTrace();
        }
        */

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
