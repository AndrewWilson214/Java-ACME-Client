package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ACMEAccount {

    /* JSON */
    @Expose public HashMap<String, String> key;
    @Expose public String[] contact;
    @Expose public String initialIp;
    @Expose public String createdAt;
    @Expose public String status;
    @Expose public String orders;

    /* INJECTED */
    public KeyPair keyPair;
    public String url;
    public ACMEClient acmeClient;

    @SuppressWarnings("unused")
    public ACMEAccount(KeyPair keyPair, String url, ACMEClient acmeClient){
        this.keyPair = keyPair;
        this.url = url;
        this.acmeClient = acmeClient;
    }

    @SuppressWarnings("unused")
    public ACMEOrder newOrder(String[] domains) throws Exception {
        List<Map<String, String>> identifiers = new ArrayList<>();
        for (String domain : domains) {
            Map<String, String> identifier = new HashMap<>();
            identifier.put("type", "dns");
            identifier.put("value", domain);
            identifiers.add(identifier);
        }

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("identifiers", identifiers);

        ACMEHTTPResponse response = acmeClient.jws.execute(acmeClient.directory.fetch().newOrder, this, payload);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ACMEOrder order = gson.fromJson(response.body(), ACMEOrder.class);
        order.account = this;
        order.url = response.headers().firstValue("Location");

        return order;

    }

    @SuppressWarnings("unused")
    public ACMEAccount updateContactDetails(String[] contactDetails) throws Exception{

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("contact", contactDetails);

        ACMEHTTPResponse response = acmeClient.jws.execute(url, this, payload);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ACMEAccount account = gson.fromJson(response.body(), ACMEAccount.class);
        account.keyPair = keyPair;
        account.url = url;
        account.acmeClient = acmeClient;

        return account;

    }

    @SuppressWarnings("unused")
    public void deactivate() throws Exception {

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("status", "deactivated");

        ACMEHTTPResponse response = acmeClient.jws.execute(url, this, payload);

    }

    @SuppressWarnings("unused")
    public ACMEAccount fetch() throws Exception {

        ACMEHTTPResponse response = acmeClient.jws.execute(url, this, null);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ACMEAccount newAccount = gson.fromJson(response.body(), ACMEAccount.class);
        newAccount.keyPair = keyPair;
        newAccount.url = url;
        newAccount.acmeClient = acmeClient;

        return newAccount;

    }

}
