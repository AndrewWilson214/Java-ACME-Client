package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMERateLimitedException extends ACMEException{
    public ACMERateLimitedException(ACMEException e) {
        super(e);
    }
}
