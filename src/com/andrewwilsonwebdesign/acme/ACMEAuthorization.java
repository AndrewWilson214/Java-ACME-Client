package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;


import java.util.HashMap;

public class ACMEAuthorization {

    /* JSON */
    @Expose public String status;
    @Expose public String expires;
    @Expose public HashMap<String, String> identifier;
    @Expose public ACMEChallenge[] challenges;
    @Expose public Boolean wildcard;

    /* INJECTED */
    public String url;
    public ACMEOrder order;
    public Boolean partial;

    public ACMEAuthorization(String url, ACMEOrder order){
        partial = true;
        this.url = url;
        this.order = order;
    }

    public ACMEAuthorization fetch() throws Exception {
        partial = false;

        ACMEHTTPResponse response = order.account.acmeClient.jws.execute(url, order.account, null);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ACMEAuthorization newAuthorization = gson.fromJson(response.body(), ACMEAuthorization.class);

        for(int i = 0; i < newAuthorization.challenges.length; i++){
            // DEPENDENCY INJECTION
            newAuthorization.challenges[i].authorization = this;
        }

        return newAuthorization;
    }

    public ACMEChallenge getChallenge(String type){
        for(int i = 0; i < challenges.length; i++){
            ACMEChallenge challenge = challenges[i];
            if(challenge.type.equals(type)){
                return challenge;
            }
        }
        return null;
    }

}
