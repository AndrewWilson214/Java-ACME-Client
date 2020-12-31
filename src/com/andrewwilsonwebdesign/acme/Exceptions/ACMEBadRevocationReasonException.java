package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEBadRevocationReasonException extends ACMEException{
    public ACMEBadRevocationReasonException(ACMEException e) {
        super(e);
    }
}
