package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEBadSignatureAlgorithmException extends ACMEException{
    public ACMEBadSignatureAlgorithmException(ACMEException e) {
        super(e);
    }
}
