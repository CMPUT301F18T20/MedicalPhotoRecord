package com.cmput301f18t20.medicalphotorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        User user = new User("9944888855");
        user.setEmail("hello@gmail.com");
        user.setPhoneNumber("00778854545");
        new ElasticsearchUserController.AddUserTask().execute(user);
    }
}
