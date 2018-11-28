package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.PatientRecord;
import com.cmput301f18t20.medicalphotorecord.Photo;
import com.cmput301f18t20.medicalphotorecord.R;
import com.cmput301f18t20.medicalphotorecord.Record;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import Controllers.AddDeleteRecordController;
import Controllers.OfflineBodyLocationController;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

import static GlobalSettings.GlobalSettings.USERIDEXTRA;

public class AddRecordActivity extends AppCompatActivity {

    private EditText record_title_edit;
    private EditText record_description_edit;
    private TextView record_date_text;
    private Button save_record_button;
    private ImageButton add_image_button;
    private Button set_geolocation_button;
    private ImageButton add_front_bodylocation_button;
    private ImageButton add_back_bodylocation_button;

    private Date record_date = new Date();
    private String record_title;
    private String record_description;
    private String userId;
    private String problemUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        // Get Views
        this.record_title_edit = (EditText)findViewById(R.id.add_record_title_id);
        this.record_description_edit = (EditText)findViewById(R.id.add_record_description_id);
        this.record_date_text = (TextView) findViewById(R.id.record_date_id);
        this.save_record_button = (Button)findViewById(R.id.record_add_button_id);
        this.set_geolocation_button = (Button)findViewById(R.id.set_geolocation_button_id);
        this.add_front_bodylocation_button = (ImageButton)findViewById(R.id.add_R_bodyLocaiton_id);
        this.add_image_button = (ImageButton)findViewById(R.id.record_add_image_id);

        this.record_date_text.setText(this.record_date.toString());

        Intent intent = getIntent();
        this.userId = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra("PROBLEMIDEXTRA");

        ArrayList<Photo> bodyPhotos = new ArrayList<>();
        bodyPhotos = new OfflineBodyLocationController().loadBodyPhotos(this);
        //Log.d("swag","size: "+ bodyPhotos.size());
        init();

    }

    public void addRecordButton(View view){

        // get record info
        this.record_title = this.record_title_edit.getText().toString();
        this.record_description = this.record_description_edit.getText().toString();

        PatientRecord record = null;
        Log.d("swag", userId + record_title + record_date + record_description);
        try{
            record = new AddDeleteRecordController().createRecord(this.problemUUID, this.userId, this.record_title, this.record_date, this.record_description);
        } catch (UserIDMustBeAtLeastEightCharactersException e) {
            Toast.makeText(AddRecordActivity.this, "Your userId has to contains more than 8 characters",Toast.LENGTH_LONG).show();
        } catch (TitleTooLongException e) {
            Toast.makeText(AddRecordActivity.this, "Your title is too long",Toast.LENGTH_LONG).show();
        }

        new AddDeleteRecordController().saveRecord(this,record);
        Toast.makeText(AddRecordActivity.this, "Your new record has been added!",Toast.LENGTH_LONG).show();
    }

    //On click listener on geo button.
    private void init(){
        Button btnMap = (Button) findViewById(R.id.set_geolocation_button_id);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecordActivity.this, AddMapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
