package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.R;



public class DrawBodyLocationActivity extends AppCompatActivity {
    protected ImageView imgDraw;
    protected Button saveButton;
    private int chosen_area;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_body_location);
        imgDraw = (ImageView)findViewById(R.id.draw_layer);
        saveButton = (Button)findViewById(R.id.draw_save);

        Intent intent = getIntent();
        this.chosen_area = intent.getIntExtra("CHOSENBODYPART",0);
        imgDraw.setImageResource(this.chosen_area);

    }

    public void onDoneClick(View view){
        //TODO retrieve contents of imgDraw after user has drawn an X and pass it to AddRecordActivity to set

    }
}
