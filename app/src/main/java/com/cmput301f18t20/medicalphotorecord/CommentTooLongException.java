package com.cmput301f18t20.medicalphotorecord;

public class CommentTooLongException extends Throwable implements Exceptions {
    @Override
    public String getExceptionName() {
        return "Comment Too Long Exception";
    }
}
