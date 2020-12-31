package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class ACMEDirectory {

    private final URL directoryURL;
    private ACMEDirectoryData cache = null;

    @SuppressWarnings("unused")
    public ACMEDirectory(String directoryURL) throws MalformedURLException {
        this.directoryURL = new URL(directoryURL);
    }

    @SuppressWarnings("unused")
    public ACMEDirectoryData fetch() throws IOException {

        if(this.cache == null) {

            HttpsURLConnection urlConnection = (HttpsURLConnection) directoryURL.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String input;
            StringBuilder body = new StringBuilder();
            while((input = in.readLine()) != null){
                body.append(input);
            }

            urlConnection.disconnect();

            Gson gson = new Gson();
            this.cache = gson.fromJson(body.toString(), ACMEDirectoryData.class);

        }

        return this.cache;

    }

}



class ACMEDirectoryData {

    @SuppressWarnings("unused")
    public String keyChange;
    public String newAccount;
    public String newNonce;
    public String newOrder;
    @SuppressWarnings("unused")
    public String revokeCart;

}