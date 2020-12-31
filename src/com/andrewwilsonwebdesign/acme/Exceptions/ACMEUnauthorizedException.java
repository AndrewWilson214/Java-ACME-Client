package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEUnauthorizedException extends ACMEException{
    public ACMEUnauthorizedException(ACMEException e) {
        super(e);
    }
}
