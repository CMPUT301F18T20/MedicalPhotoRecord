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

    protected final String UUID = java.util.UUID.randomUUID().toString();
    private String recordUUID;
    private String problemUUID;
    private String bodyLocation; // front or back, body parts
    private String label;
    private boolean isViewedBodyPhoto;


    private byte[] byteimage = null; /* likely will need to convert to byte array for storage in elasticsearch */
    private Bitmap bitmap;
    private String bitmapString;  // need to save as string for bitmap data to be properly saved in offline database
    private static int maxBytes=65536;

    public Photo() {}

    public Photo(String recordUUID, String problemUUID, String bodyLocation, Bitmap bitmap, String label) throws PhotoTooLargeException {
        this.recordUUID = recordUUID;
        this.problemUUID = problemUUID;
        this.bodyLocation = bodyLocation;
        this.label = label;
        setBitmap(bitmap);
        saveBitMapAsString();
    }

    // set to true if the current image is displayed as current body photo
    public void setIsViewedBodyPhoto(Boolean isViewedBodyPhoto){
        this.isViewedBodyPhoto = isViewedBodyPhoto;
    }

    public boolean isViewedBodyPhoto() {
        return this.isViewedBodyPhoto;
    }

    public String getRecordUUID(){
        return this.recordUUID;
    }

    public String getBodyLocation(){
        return this.bodyLocation;
    }

    public String getLabel(){
        return this.label;
    }

    public String getProblemUUID() {return this.problemUUID;}

    public String getUUID(){ return this.UUID;}

    public void setRecordUUID(String recordUUID){
        this.recordUUID = recordUUID;
    }

    public void setBitmap(Bitmap inBitmap) throws PhotoTooLargeException {

        // Check for photo size of compressed bit map
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
