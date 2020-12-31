package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMECompoundException extends ACMEException{
    public ACMECompoundException(ACMEException e) {
        super(e);
    }
}
