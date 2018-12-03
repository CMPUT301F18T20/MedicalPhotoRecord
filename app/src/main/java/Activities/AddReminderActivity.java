package Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.MyBroadcastReceiver;
import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.R;

import java.util.Calendar;
import java.util.Date;

import Controllers.OfflineProblemController;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

//This activity is prototype of a video in youtube, see reference,I made plenty changes, cause the original broadcaster wont display notification for api>26
public class AddReminderActivity extends AppCompatActivity {

    TimePicker timePicker;
    TextView textView;

    //fetch the problem for get problem.gettitle
    int mHour,mMin;
    String title, message, problemUUID;
    Problem problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);
        this.problem = new OfflineProblemController().getProblem(AddReminderActivity.this, this.problemUUID);
        this.timePicker = (TimePicker)findViewById(R.id.timePicker);
        this.textView = (TextView)findViewById(R.id.timeText);
        this.title = "Reminder for "+problem.getTitle()+" is ON";
        this.message = "It is time to update your photos for the Problem: "+problem.getTitle();

        textView.setText(textView.getText().toString()+":  "+problem.getTitle());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMin = minute;
            }
        });
    }

    public void setTimer (View v){

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY,mHour);
        cal_alarm.set(Calendar.MINUTE,mMin);
        cal_alarm.set(Calendar.SECOND,0);

        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }

        Intent i = new Intent(AddReminderActivity.this,MyBroadcastReceiver.class);
        i.putExtra("title", title);
        i.putExtra("message",message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminderActivity.this,24444,i,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);

        Toast.makeText(this, "The Reminder for: "+problem.getTitle()+" is now set", Toast.LENGTH_SHORT).show();
    }
}
