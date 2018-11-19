package Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

import com.cmput301f18t20.medicalphotorecord.R;

public class  SetReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
    }

    public void onSetTimeClick(View v){
        Intent intent = new Intent(this, SetAlarm.class);
        startActivity(intent);
    }

}
