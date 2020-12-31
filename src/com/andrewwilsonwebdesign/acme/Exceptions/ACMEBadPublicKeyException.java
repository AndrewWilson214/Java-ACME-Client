package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEBadPublicKeyException extends ACMEException{
    public ACMEBadPublicKeyException(ACMEException e) {
        super(e);
    }
}
