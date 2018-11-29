package Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301f18t20.medicalphotorecord.R;

public class SlideshowActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private String recordUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        this.recordUUID ="tobedonelater2";
        viewPager = findViewById(R.id.slide_show_view_pager_id);
        this.viewPager.setAdapter(new SlideShowViewPagerAdapter(SlideshowActivity.this, this.recordUUID));
    }
}
