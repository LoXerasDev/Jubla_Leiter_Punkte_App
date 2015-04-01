package com.loxeras.leiterpunkte;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by dario.duff on 24.03.2015.
 */


public class checkVersion extends Thread {

    //Defines the URL from the APK
    public static final String APP_URL = "http://www.old.jubla-uzna.ch/android/leiterpunkte/version.html";
    //---------------------------------

    public String line = "";
    public String result = "";
    public Boolean b = true;
    public String getVersion() throws  IOException{
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpContext localContext = new BasicHttpContext();
                    HttpGet httpGet = new HttpGet(APP_URL);
                    HttpResponse response = httpClient.execute(httpGet, localContext);


                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    response.getEntity().getContent()
                            )
                    );

                    while ((line = reader.readLine()) != null){
                        result += line;
                    }
                    b = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        while(b == true) {

        }
        return result;

    }


}


