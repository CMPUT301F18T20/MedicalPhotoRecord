package Controllers;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class DrawBodyLocationController {

    public Bitmap createBitmapFromImage(ImageView imgDraw){
        //https://stackoverflow.com/questions/38735387/get-bitmap-attached-to-imageview-with-background
        imgDraw.setDrawingCacheEnabled(true);
        imgDraw.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imgDraw.layout(0, 0, imgDraw.getMeasuredWidth(), imgDraw.getMeasuredHeight());

        imgDraw.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(imgDraw.getDrawingCache());
        imgDraw.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
