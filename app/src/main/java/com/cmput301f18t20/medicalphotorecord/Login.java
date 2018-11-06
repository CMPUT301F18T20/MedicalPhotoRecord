package com.cmput301f18t20.medicalphotorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.searchly.jestdroid.DroidClientConfig;

import java.util.ArrayList;

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

        try {
            Record record = new Record("9944888855");
            record.setComment("test");
            new ElasticsearchRecordsController.AddRecordTask().execute(record);
        } catch (CommentTooLongException e) {
            e.printStackTrace();
        }
    }

    /**Run when the Sign up button is clicked on.  Transitions to sign up page.
     * @param view Unused as function only applies to one view anyways
     */
    public void onSignUpClick(View view) throws Exception {
        SignUpButton.setBackgroundColor(0xFF00759C);

        //TODO THIS QUERY NEEDS TO CHANGE
        //http://cmput301.softwareprocess.es:8080/cmput301f18t20/_search?size=1000&from=0
        ArrayList<Record> records = new ElasticsearchRecordsController.GetRecordsTask().execute("?size=1000&from=0").get();

        for (Record rec : records) {
            Log.d("Hello", rec.getCreatedByUserID());
        }

        //wait 1 second
        SignUpButton.postDelayed(new Runnable() {

            @Override
            public void run() {
                SignUpButton.setBackgroundColor(0xFF0099CC);
            }
        }, 1000);
    }
}
