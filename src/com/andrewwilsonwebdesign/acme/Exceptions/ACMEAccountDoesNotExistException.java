package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEAccountDoesNotExistException extends ACMEException{
    public ACMEAccountDoesNotExistException(ACMEException e) {
        super(e);
    }
}
