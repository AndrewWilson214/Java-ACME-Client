package com.andrewwilsonwebdesign.acme.Exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class ExceptionAdapter {

    private HashMap<String, Class> exceptions;

    public ExceptionAdapter(){
        exceptions = new HashMap<>();
        exceptions.put("accountDoesNotExist", ACMEAccountDoesNotExistException.class);
        exceptions.put("alreadyRevoked", ACMEAlreadyRevokedException.class);
        exceptions.put("badCSR", ACMEBadCSRException.class);
        exceptions.put("badNonce", ACMEBadNonceException.class);
        exceptions.put("badPublicKey", ACMEBadPublicKeyException.class);
        exceptions.put("badRevocationReason", ACMEBadRevocationReasonException.class);
        exceptions.put("badSignatureAlgorithm", ACMEBadSignatureAlgorithmException.class);
        exceptions.put("caa", ACMECAAException.class);
        exceptions.put("compound", ACMECompoundException.class);
        exceptions.put("connection", ACMEConnectionException.class);
        exceptions.put("dns", ACMEDNSException.class);
        exceptions.put("externalAccountRequired", ACMEExternalAccountRequiredException.class);
        exceptions.put("incorrectResponse", ACMEIncorrectResponseException.class);
        exceptions.put("invalidContact", ACMEInvalidContactException.class);
        exceptions.put("malformed", ACMEMalformedException.class);
        exceptions.put("orderNotReady", ACMEOrderNotReadyException.class);
        exceptions.put("rateLimited", ACMERateLimitedException.class);
        exceptions.put("rejectedIdentifier", ACMERejectedIdentifierException.class);
        exceptions.put("serverInternal", ACMEServerInternalException.class);
        exceptions.put("tls", ACMETLSException.class);
        exceptions.put("unauthorized", ACMEUnauthorizedException.class);
        exceptions.put("unsupportedContact", ACMEUnsupportedContactException.class);
        exceptions.put("unsupportedIdentifier", ACMEUnsupportedIdentifierException.class);
        exceptions.put("userActionRequired", ACMEUserActionRequiredException.class);
    }

    public Exception getException(String json){

        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            ACMEException exception = gson.fromJson(json, ACMEException.class);

            String[] keyParts = exception.type.split(":");
            String key = keyParts[keyParts.length - 1];
            if(exceptions.containsKey(key)){
                try{
                    return (ACMEException) exceptions.get(key).getConstructor(ACMEException.class).newInstance(exception);
                }catch(Exception e){
                    return exception;
                }
            }else{
                return exception;
            }

        }catch(Exception e){
            return new Exception(json);
        }
    }

}
