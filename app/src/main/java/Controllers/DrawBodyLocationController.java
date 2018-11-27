package Controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.cmput301f18t20.medicalphotorecord.Photo;

import java.util.ArrayList;

import Exceptions.PhotoTooLargeException;

public class DrawBodyLocationController {

    public Bitmap getBitmapFromImageView(ImageView imgDraw){
        imgDraw.setDrawingCacheEnabled(true);
        imgDraw.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(imgDraw.getDrawingCache());
        imgDraw.setDrawingCacheEnabled(false);

        return bitmap;
    }
    public Photo createPhotoInstance(Bitmap bitmap,String bodylocation){
        ArrayList<Photo> photos = new ArrayList<>();
        Bitmap scaledBitmap =downscaleToMaxAllowedDimension(bitmap);
        Log.d("wow", Integer.toString(scaledBitmap.getByteCount()));
        try{
            Photo photo = new Photo(scaledBitmap,bodylocation);
            Log.d("swag","???");
            photos.add(photo);
        } catch(PhotoTooLargeException e1){
            Log.d("bodylocation", "File is too large");
        }
        return photos.get(0);
    }
    private static Bitmap downscaleToMaxAllowedDimension(Bitmap bitmap) {
        int MAX_ALLOWED_RESOLUTION = 200;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = MAX_ALLOWED_RESOLUTION;
            outHeight = (inHeight * MAX_ALLOWED_RESOLUTION) / inWidth;
        } else {
            outHeight = MAX_ALLOWED_RESOLUTION;
            outWidth = (inWidth * MAX_ALLOWED_RESOLUTION) / inHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);

        return resizedBitmap;
    }
}
