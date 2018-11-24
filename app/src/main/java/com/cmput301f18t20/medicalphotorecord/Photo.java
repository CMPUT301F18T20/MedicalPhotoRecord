/*
 * Class name: Photo
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 08/11/18 7:47 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import android.graphics.Bitmap;
import android.util.Log;

import Exceptions.PhotoTooLargeException;

public class Photo {

    private String recordUUID;
    private String bodyLocation; // front or back, body parts
    private String name;
    private String directory;

    private byte[] byteimage = null; /* likely will need to convert to byte array for storage in elasticsearch */
    private Bitmap bitmap;
    private static int maxBytes=65536;

    public Photo(String recordUUID, String bodyLocation, Bitmap bitmap) throws PhotoTooLargeException {
        this.recordUUID = recordUUID;
        this.bodyLocation = bodyLocation;
        setBitmap(bitmap);
    }

    public void setBitmap(Bitmap inBitmap) throws PhotoTooLargeException {
        if (inBitmap.getByteCount() <= Photo.maxBytes) {
            this.bitmap = bitmap;
        } else {
            Log.d ("Photo Exception","Photo size too large");
            throw new PhotoTooLargeException();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
