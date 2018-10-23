package com.cmput301f18t20.medicalphotorecord;

public class TitleTooLongException extends Throwable implements Exceptions {
    @Override
    public String getExceptionName() {
        return "Title Too Long Exception";
    }
}
