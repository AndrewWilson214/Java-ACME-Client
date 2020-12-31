package com.andrewwilsonwebdesign.acme.Exceptions;

public class ACMEMalformedException extends ACMEException{

    public ACMEMalformedException(ACMEException e){
        super(e);
    }

}
