package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEOrderNotReadyException extends ACMEException{
    public ACMEOrderNotReadyException(ACMEException e) {
        super(e);
    }
}
