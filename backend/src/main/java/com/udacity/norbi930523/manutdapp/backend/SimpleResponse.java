package com.udacity.norbi930523.manutdapp.backend;

/** The object model for the data we are sending through endpoints */
public class SimpleResponse {

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}