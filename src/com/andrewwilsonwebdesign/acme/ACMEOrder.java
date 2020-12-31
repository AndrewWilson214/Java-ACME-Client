package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.ByteArrayInputStream;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bouncycastle.util.encoders.Base64;

public class ACMEOrder {

    /* JSON */
    @Expose public ACMEOrderStatus status;
    @Expose public String expires;
    @Expose public String notBefore;
    @Expose public String notAfter;
    @Expose public List<Map<String, String>> identifiers;
    @Expose public String[] authorizations;
    @Expose public String finalize;
    @Expose public String certificate;
    @Expose public String error;

    /* INJECTED */
    public ACMEAccount account;
    public String url;

    @SuppressWarnings("unused")
    public ACMEOrder(){

    }

    @SuppressWarnings("unused")
    public ACMEOrder(String url, ACMEAccount account){
        this.url = url;
        this.account = account;
    }

    @SuppressWarnings("unused")
    public ACMEAuthorization[] fetchAllAuthorizations() throws Exception {
        ACMEAuthorization[] auths = new ACMEAuthorization[authorizations.length];
        for(int i = 0; i < authorizations.length; i++){
            ACMEAuthorization auth = new ACMEAuthorization(authorizations[i], this);
            auths[i] = auth.fetch();
        }
        return auths;
    }

    @SuppressWarnings("unused")
    public ACMEOrder fetch() throws Exception {

        ACMEHTTPResponse response = account.acmeClient.jws.execute(url, this.account, null);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ACMEOrder newOrder = gson.fromJson(response.body(), ACMEOrder.class);
        newOrder.account = account;
        newOrder.url = url;

        return newOrder;
    }

    @SuppressWarnings("unused")
    public ACMEOrder finalizeOrder(String csrText) throws Exception {
       HashMap<String, Object> payload = new HashMap<>();
       payload.put("csr", csrText);

       ACMEHTTPResponse response = account.acmeClient.jws.execute(this.finalize, this.account, payload);

       Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
       ACMEOrder newOrder = gson.fromJson(response.body(), ACMEOrder.class);
       newOrder.account = this.account;
       newOrder.url = this.url;

       return newOrder;
    }

    @SuppressWarnings("unused")
    public X509Certificate[] getCertificates() throws Exception {

        ACMEHTTPResponse response = account.acmeClient.jws.execute(this.certificate, this.account, null);

        List<X509Certificate> certificates = new ArrayList<>();

        String responseNoBreaks = response.body().replace("\n", "").replace("\r", "");

        Pattern p = Pattern.compile("-----BEGIN CERTIFICATE-----(?<b64>.+?)-----END CERTIFICATE-----", Pattern.DOTALL);
        Matcher matcher = p.matcher(responseNoBreaks);

        while(matcher.find()){
            String b64;
            try{
                b64 = matcher.group(1);
            }catch(Exception e){
                throw new Exception("Server responded with an invalid certificate format. Expected application/pem-certificate-chain");
            }

            byte[] certBytes = Base64.decode(b64);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certBytes));

            certificates.add((X509Certificate) cert);

        }

        X509Certificate[] certs = new X509Certificate[certificates.size()];
        certs = certificates.toArray(certs);
        return certs;

    }

}


