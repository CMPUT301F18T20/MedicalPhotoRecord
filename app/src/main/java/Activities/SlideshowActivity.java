package Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301f18t20.medicalphotorecord.R;

import static GlobalSettings.GlobalSettings.PROBLEMIDEXTRA;

public class SlideshowActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private String problemUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        // Get problem uuid
        Intent intent = getIntent();
        //this.problemUUID = intent.getStringExtra(PROBLEMIDEXTRA);

        this.problemUUID = "problemuuid1";

        // Pass problem uuid to adapter to show slide show
        viewPager = findViewById(R.id.slide_show_view_pager_id);
        this.viewPager.setAdapter(new SlideShowViewPagerAdapter(SlideshowActivity.this, this.problemUUID));
    }
}
