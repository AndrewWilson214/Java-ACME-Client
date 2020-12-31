package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEServerInternalException extends ACMEException{
    public ACMEServerInternalException(ACMEException e) {
        super(e);
    }
}
