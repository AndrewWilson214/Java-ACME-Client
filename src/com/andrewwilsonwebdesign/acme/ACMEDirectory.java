package com.andrewwilsonwebdesign.acme;

import com.google.gson.Gson;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class ACMEDirectory {

    private URL directoryURL;
    private ACMEDirectoryData cache = null;

    public ACMEDirectory(String directoryURL) throws MalformedURLException {
        this.directoryURL = new URL(directoryURL);
    }

    public ACMEDirectoryData fetch() throws IOException, InterruptedException {

        if(this.cache == null) {

            HttpsURLConnection urlConnection = (HttpsURLConnection) directoryURL.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String input;
            String body = "";
            while((input = in.readLine()) != null){
                body += input;
            }

            urlConnection.disconnect();

            Gson gson = new Gson();
            this.cache = gson.fromJson(body, ACMEDirectoryData.class);

        }

        return this.cache;

    }

}



class ACMEDirectoryData {

    public String keyChange;
    public String newAccount;
    public String newNonce;
    public String newOrder;
    public String revokeCart;

}