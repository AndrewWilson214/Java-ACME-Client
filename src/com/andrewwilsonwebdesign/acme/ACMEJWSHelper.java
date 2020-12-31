package com.andrewwilsonwebdesign.acme;

import com.andrewwilsonwebdesign.acme.Exceptions.ACMEException;
import com.andrewwilsonwebdesign.acme.Exceptions.ExceptionAdapter;
import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.RSAKey;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.interfaces.*;
import java.util.*;

class ACMEJWSHelper {

    private String nextNonce;
    public ACMEClient acmeClient;
    private final ExceptionAdapter exceptionAdapter;

    public ACMEJWSHelper(ACMEClient acmeClient) {
        exceptionAdapter = new ExceptionAdapter();
        this.acmeClient = acmeClient;
    }

    public ACMEHTTPResponse execute(String url, KeyPair keyPair, HashMap<String, Object> payload) throws Exception {

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .customParam("nonce", this.fetchNonce())
                .customParam("url", url)
                .jwk(getJWK(keyPair.getPublic()))
                .build();

        return runRequest(url, buildBody(keyPair.getPrivate(), header, payload));
    }

    public ACMEHTTPResponse execute(String url, ACMEAccount account, HashMap<String, Object> payload) throws Exception {

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .customParam("nonce", this.fetchNonce())
                .customParam("url", url)
                .keyID(account.url)
                .build();

        return runRequest(url, buildBody(account.keyPair.getPrivate(), header, payload));
    }

    private String buildBody(PrivateKey privateKey, JWSHeader header, HashMap<String, Object> payload) throws Exception {

        Payload thePayload;
        if(payload == null){
            thePayload = new Payload("");
        }else {
            thePayload = new Payload(payload);
        }

        JWSObject jwsObject = new JWSObject(header, thePayload);

        jwsObject.sign(new RSASSASigner(privateKey));

        String serialized = jwsObject.serialize();
        String[] parts = serialized.split("\\.");

        HashMap<String, Object> returnData = new HashMap<>();
        returnData.put("protected", parts[0]);
        returnData.put("payload", parts[1]);
        returnData.put("signature", parts[2]);

        Gson gson = new Gson();
        return gson.toJson(returnData);

    }

    private ACMEHTTPResponse runRequest(String url, String body) throws Exception {

        URL urlObject = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) urlObject.openConnection();

        try {
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/jose+json");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            urlConnection.setFixedLengthStreamingMode(body.length());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            writer.write(body, 0, body.length());
            writer.close();

            InputStream streamToRead;
            if(urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() < 300) {
                streamToRead = urlConnection.getInputStream();
            }else{
                streamToRead = urlConnection.getErrorStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(streamToRead));
            StringBuilder responseBody = new StringBuilder();
            String input;
            while((input = reader.readLine()) != null){
                responseBody.append(input);
            }
            reader.close();

            ACMEHTTPHeaders headers = new ACMEHTTPHeaders(urlConnection.getHeaderFields());
            ACMEHTTPResponse response = new ACMEHTTPResponse(headers, responseBody.toString());

            urlConnection.disconnect();

            String contentType = response.headers().firstValue("Content-Type");
            if (contentType != null) {
                if (contentType.equals("application/problem+json")) {
                    throw exceptionAdapter.getException(response.body());
                } else if (!(contentType.equals("application/json") || contentType.equals("application/pem-certificate-chain"))) {
                    throw new IOException("Invalid Content-Type: " + contentType + ". This ACME Client expects application/jose+json or application/pem-certificate-chain");
                }
            } else {
                throw new IOException("No ACME Content-Type is present.");
            }

            String nonce = response.headers().firstValue("Replay-Nonce");
            if (nonce != null) this.nextNonce = nonce;

            urlConnection.disconnect();

            return response;
        }catch(ACMEException e){
            e.printStackTrace();
            System.exit(0);
            return null;
        }

    }

    public JWK getJWK(PublicKey publicKey) {

        return new RSAKey.Builder((RSAPublicKey) publicKey)
                .build();

    }

    private String fetchNonce() throws Exception {

        if(nextNonce != null){
            String nonce = nextNonce;
            nextNonce = null;
            return nonce;
        }

        URL urlObject = new URL(acmeClient.directory.fetch().newNonce);
        HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();

        urlConnection.setRequestMethod("HEAD");

        ACMEHTTPHeaders headers = new ACMEHTTPHeaders(urlConnection.getHeaderFields());

        String nonce = headers.firstValue("Replay-Nonce");
        if(nonce == null) throw new Exception("Replay-Nonce header was not present.");

        return nonce;

    }

}
