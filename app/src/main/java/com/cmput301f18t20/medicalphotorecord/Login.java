package com.cmput301f18t20.medicalphotorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.LoginButton);

        /* sets background to a deep blue */
        //getWindow().getDecorView().setBackgroundColor(0xff264a73);
        //getWindow().getDecorView().setBackgroundColor(0xfffef501);

    }

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
}
