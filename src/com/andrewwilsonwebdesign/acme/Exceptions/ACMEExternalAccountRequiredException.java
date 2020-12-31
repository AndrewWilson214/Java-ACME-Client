package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEExternalAccountRequiredException extends ACMEException{
    public ACMEExternalAccountRequiredException(ACMEException e) {
        super(e);
    }
}
