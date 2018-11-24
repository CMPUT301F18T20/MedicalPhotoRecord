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
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import Exceptions.PhotoTooLargeException;

public class Photo {

    private String recordUUID;
    private String bodyLocation; // front or back, body parts
    private String name;
    private String directory;

    private byte[] byteimage = null; /* likely will need to convert to byte array for storage in elasticsearch */
    private Bitmap bitmap;
    private String bitmapString;  // need to save as string for bitmap data to be properly saved in offline database
    private static int maxBytes=65536;

    public Photo(String recordUUID, String bodyLocation, Bitmap bitmap) throws PhotoTooLargeException {
        this.recordUUID = recordUUID;
        this.bodyLocation = bodyLocation;
        setBitmap(bitmap);
        saveBitMapAsString();
    }

    public String getRecordUUID(){
        return this.recordUUID;
    }

    public void setBitmap(Bitmap inBitmap) throws PhotoTooLargeException {
        if (inBitmap.getByteCount() <= Photo.maxBytes) {
            this.bitmap = inBitmap;
        } else {
            Log.d ("Photo Exception","Photo size too large" + String.valueOf(inBitmap.getByteCount()));
            throw new PhotoTooLargeException();
        }
    }

    public void saveBitMapAsString(){

        // Turn bitmap -> byte  -> string
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        byte[] bitmapByte = output.toByteArray();
        this.bitmapString = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
    }

    public Bitmap getBitmapFromString() {

        // Turn string -> byte  -> bitmap
        byte[] imageByte = Base64.decode(this.bitmapString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
        return bitmap;
    }
}
