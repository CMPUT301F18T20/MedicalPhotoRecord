package com.cmput301f18t20.medicalphotorecord;

import android.location.Location;

import java.util.Date;

public class Record extends Entry {
    String comment;

    Record() {
        super();
    }

    Record(String creatorUserID) throws NonNumericUserIDException {
        super(creatorUserID);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 300) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }
}
