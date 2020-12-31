package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEConnectionException extends ACMEException{
    public ACMEConnectionException(ACMEException e) {
        super(e);
    }
}
