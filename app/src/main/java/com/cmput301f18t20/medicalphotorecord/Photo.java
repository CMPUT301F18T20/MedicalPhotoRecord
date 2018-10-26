package com.cmput301f18t20.medicalphotorecord;

import android.graphics.Bitmap;

public class Photo {
    protected String name;
    protected String directory;
    protected byte[] byteimage = null; /* likely will need to convert to byte array for storage in elasticsearch */
    Bitmap bitmap;
    private static int maxBytes=65536;

    public void setBitmap(Bitmap inBitmap) throws PhotoTooLargeException {
        if (inBitmap.getByteCount() <= Photo.maxBytes) {
            this.bitmap = bitmap;
        } else {
            throw new PhotoTooLargeException();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
