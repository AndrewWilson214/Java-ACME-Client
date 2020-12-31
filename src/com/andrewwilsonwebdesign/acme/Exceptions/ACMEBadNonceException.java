package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEBadNonceException extends ACMEException{
    public ACMEBadNonceException(ACMEException e) {
        super(e);
    }
}
