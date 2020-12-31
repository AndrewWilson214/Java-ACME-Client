package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEUserActionRequiredException extends ACMEException{
    public ACMEUserActionRequiredException(ACMEException e) {
        super(e);
    }
}
