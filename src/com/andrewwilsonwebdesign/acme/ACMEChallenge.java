package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64URL;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ACMEChallenge {

    @Expose public String type;
    @Expose public String url;
    @Expose public String status;
    @Expose public String token;
    @Expose public String validated;

    public ACMEAuthorization authorization;

    public void complete() throws Exception {

        HashMap<String, Object> payload = new HashMap<>();

        ACMEHTTPResponse response = authorization.order.account.acmeClient.jws.execute(url, authorization.order.account, payload);

    }

    public String getKeyAuthorizationString() throws NoSuchAlgorithmException {
        Gson gson = new Gson();

        JWK jwk = authorization.order.account.acmeClient.jws.getJWK(authorization.order.account.keyPair.getPublic());
        Map<String, Object> jwkMap = jwk.toJSONObject();
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.putAll(jwkMap);

        String jwkJson = gson.toJson(treeMap);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(jwkJson.getBytes(StandardCharsets.UTF_8));

        return this.token + "." + Base64URL.encode(encodedHash).toString();
    }

}