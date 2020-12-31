package com.andrewwilsonwebdesign.acme;

import java.util.*;

public class ACMEHTTPResponse {

    private final ACMEHTTPHeaders headerFields;
    private final String body;

    public ACMEHTTPResponse(ACMEHTTPHeaders headerFields, String body){
        this.headerFields = headerFields;
        this.body = body;
    }

    public ACMEHTTPHeaders headers(){
        return headerFields;
    }

    public String body(){
        return body;
    }

}

class ACMEHTTPHeaders{

    HashMap<String, List<String>> headers;

    @SuppressWarnings("unused")
    public ACMEHTTPHeaders(Map<String, List<String>> map){
        headers = new HashMap<>();
        headers.putAll(map);
    }

    @SuppressWarnings("unused")
    public String firstValue(String key){
        List<String> value = headers.get(key);
        if(value != null){
            if(value.get(0) != null){
                return value.get(0);
            }
        }
        return null;
    }

}
