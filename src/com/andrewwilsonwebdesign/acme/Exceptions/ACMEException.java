package com.andrewwilsonwebdesign.acme.Exceptions;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class ACMEException extends Exception{

    @Expose
    public String type;
    @Expose
    public String title;
    @Expose
    public int status;
    @Expose
    public String detail;
    @Expose
    public String instance;
    @Expose
    public ACMEException[] subproblems;
    public HashMap<String, String> identifier;

    public ACMEException(ACMEException e){
        this.type = e.type;
        this.title = e.title;
        this.status = e.status;
        this.detail = e.detail;
        this.instance = e.instance;
        this.subproblems = e.subproblems;
        this.identifier = e.identifier;
    }

}
