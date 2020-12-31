package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEAlreadyRevokedException extends ACMEException{
    public ACMEAlreadyRevokedException(ACMEException e) {
        super(e);
    }
}
