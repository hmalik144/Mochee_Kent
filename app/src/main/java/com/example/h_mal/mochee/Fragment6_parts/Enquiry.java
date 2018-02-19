package com.example.h_mal.mochee.Fragment6_parts;

/**
 * Created by h_mal on 06/02/2018.
 */

public class Enquiry {

    private String enqName;
    private String enqEmail;
    private String enqSubject;
    private String enqMessage;

    public Enquiry(String enqName, String enqEmail, String enqSubject, String enqMessage) {
        this.enqName = enqName;
        this.enqEmail = enqEmail;
        this.enqSubject = enqSubject;
        this.enqMessage = enqMessage;
    }

    public String getEnqName() {
        return enqName;
    }

    public String getEnqEmail() {
        return enqEmail;
    }

    public String getEnqSubject() {
        return enqSubject;
    }

    public String getEnqMessage() {
        return enqMessage;
    }
}
