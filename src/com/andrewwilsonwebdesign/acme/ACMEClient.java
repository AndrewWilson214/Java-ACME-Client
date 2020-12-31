package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.util.*;
import java.security.KeyPair;
import com.google.gson.GsonBuilder;

public class ACMEClient {

    public ACMEJWSHelper jws;
    public ACMEDirectory directory;

    public ACMEClient(String directoryURL) throws MalformedURLException {
        jws = new ACMEJWSHelper(this);
        directory = new ACMEDirectory(directoryURL);
    }

    public ACMEAccount login(KeyPair keyPair) throws Exception {

        HashMap<String, Object> payload = new HashMap<>();

        return newAccount(keyPair, payload);

    }

    public ACMEAccount login(KeyPair keyPair, Boolean createIfAccountDoesNotExist) throws Exception {

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("onlyReturnExisting", !createIfAccountDoesNotExist);

        return newAccount(keyPair, payload);

    }

    public ACMEAccount login(KeyPair keyPair, String[] contactMethods, Boolean tosAgreement) throws Exception{
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("contact", contactMethods);
        payload.put("termsOfServiceAgreed", tosAgreement);

        return newAccount(keyPair, payload);
    }

    private ACMEAccount newAccount(KeyPair keyPair, HashMap<String, Object> payload) throws Exception {

        ACMEHTTPResponse response = jws.execute(directory.fetch().newAccount, keyPair, payload);

        String url = response.headers().firstValue("Location");

        if(url == null){
            //throw an error
            throw new NoSuchElementException("The Location header was not defined. Cannot get user's JWK key ID");
        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ACMEAccount account = gson.fromJson(response.body(), ACMEAccount.class);
        account.keyPair = keyPair;
        account.url = url;
        account.acmeClient = this;

        return account;
    }

}
