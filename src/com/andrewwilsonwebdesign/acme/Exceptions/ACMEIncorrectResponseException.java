package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEIncorrectResponseException extends ACMEException{
    public ACMEIncorrectResponseException(ACMEException e) {
        super(e);
    }
}
