package Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f18t20.medicalphotorecord.BodyLocation;
import com.cmput301f18t20.medicalphotorecord.R;

public class BodyLocationActivity extends AppCompatActivity {
    protected ImageView body_location,body_overlay;
    protected TextView choose_text;
    private String userID,problemUUID;



    public ImageView getImage(){
        return this.body_overlay;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_location);

        this.body_location = (ImageView)findViewById(R.id.body_location);
        this.body_overlay= (ImageView)findViewById(R.id.body_location_overlay);
        this.choose_text = (TextView)findViewById(R.id.body_location_welcome);

        //get userID
        Intent intent = getIntent();
        this.userID = intent.getStringExtra("USERIDEXTRA");
        this.problemUUID = intent.getStringExtra("PROBLEMIDEXTRA");

        this.body_overlay.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action =event.getAction();
                int x = (int)event.getX();
                int y = (int)event.getY();
                int redValue,greenValue,blueValue;

                //https://stackoverflow.com/questions/16939380/how-do-i-get-color-of-where-i-click
                switch(action) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_UP:
                        ImageView img = (ImageView)findViewById(R.id.body_location_overlay);
                        img.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(img.getDrawingCache());
                        img.setDrawingCacheEnabled(false);
                        int pixel = bitmap.getPixel(x,y);

                        redValue = Color.red(pixel);
                        blueValue = Color.blue(pixel);
                        greenValue = Color.green(pixel);
                        chosenLocation(redValue,blueValue,greenValue);
                }
                return false;
            }
        });
    }

    private void chosenLocation(int red, int blue, int green) {

        //chosen head area -- red
        if (red == 255 && blue == 0 && green == 0){
            Toast.makeText(this,"You chose the head area", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("MODE",1);
            intent.putExtra("BODYLOCATION","head");
            intent.putExtra("CHOSENBODYPART",R.drawable.head);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        //chosen chest area -- grey
        else if (red == 179 && blue == 179 && green == 179){
            Toast.makeText(this,"You chose the chest area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","chest");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.chest);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 0 && blue == 128 && green == 128){
            Toast.makeText(this,"You chose the right arm area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","rightArm");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.right_arm);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 128 && blue == 128 && green == 0){
            Toast.makeText(this,"You chose the right hand area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","rightHand");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.right_arm);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 108 && blue == 83 && green == 83){
            Toast.makeText(this,"You chose the left arm area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","leftArm");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.left_arm);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 233 && blue == 175 && green == 175){
            Toast.makeText(this,"You chose the left hand area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","leftHand");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.left_arm);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 255 && blue == 0 && green == 102){
            Toast.makeText(this,"You chose the abdomen area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","abs");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.abs);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 255 && blue == 85 && green == 221){
            Toast.makeText(this,"You chose the left leg area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","leftLeg");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.left_leg);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 85 && blue == 85 && green == 255){
            Toast.makeText(this,"You chose the left foot area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","leftFoot");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.left_leg);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 0 && blue == 255 && green   == 0){
            Toast.makeText(this,"You chose the right leg area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","rightLeg");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.right_leg);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
        else if (red == 42 && blue == 255 && green == 212){
            Toast.makeText(this,"You chose the right foot area",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,DrawBodyLocationActivity.class);
            intent.putExtra("BODYLOCATION","rightFoot");
            intent.putExtra("MODE",1);
            intent.putExtra("CHOSENBODYPART",R.drawable.right_leg);
            intent.putExtra("USERIDEXTRA",this.userID);
            intent.putExtra("PROBLEMIDEXTRA",this.problemUUID);
            startActivity(intent);
        }
    }
}

