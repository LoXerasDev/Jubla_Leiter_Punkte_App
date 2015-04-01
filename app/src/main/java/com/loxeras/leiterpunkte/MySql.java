package com.loxeras.leiterpunkte;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by dario.duff on 19.03.2015.
 */
public class MySql {


    InputStream is=null;
    String line = "";
    String result;

    public Boolean insert(final String value_time,final String value_accountName,final String value_leiter, final String value_punkte, final String value_grund)
    {
            Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // this.parent = parent;
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("time",value_time));
                    nameValuePairs.add(new BasicNameValuePair("email",value_accountName));
                    nameValuePairs.add(new BasicNameValuePair("leiter", value_leiter));
                    nameValuePairs.add(new BasicNameValuePair("punkte",value_punkte));
                    nameValuePairs.add(new BasicNameValuePair("grund",value_grund));
                    Log.e("post context", "" + nameValuePairs.toString());

                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://www.old.jubla-uzna.ch/insert.php");
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        Log.e("pass 1", "connection success ");
                    } catch (Exception e) {
                        Log.e("Fail 1", e.toString());
                        //  Toast.makeText(getApplicationContext(), e.toString() + "Invalid IP Address",
                        // Toast.LENGTH_LONG).show();
                    }

                    try {
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        Log.e("pass 2", "connection success ");
                    } catch (Exception e) {
                        Log.e("Fail 2", e.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        return true;
    }

    public String getPoints(final String value_leiter) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // this.parent = parent;
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    // nameValuePairs.add(new BasicNameValuePair("time",value_time));
                    // nameValuePairs.add(new BasicNameValuePair("email",value_accountName));
                    nameValuePairs.add(new BasicNameValuePair("leiter", value_leiter));
                    //nameValuePairs.add(new BasicNameValuePair("punkte",value_punkte));
                    // nameValuePairs.add(new BasicNameValuePair("grund",value_grund));
                    Log.e("post context", "" + nameValuePairs.toString());

                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://www.old.jubla-uzna.ch/getPoints.php");
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        Log.e("pass 1", "connection success ");
                    } catch (Exception e) {
                        Log.e("Fail 1", e.toString());
                        //  Toast.makeText(getApplicationContext(), e.toString() + "Invalid IP Address",
                        // Toast.LENGTH_LONG).show();
                    }

                    try {
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        result = sb.toString();
                        Log.e("string:", result);
                        Log.e("pass 2", "connection success ");
                    } catch (Exception e) {
                        Log.e("Fail 2", e.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        return result;
    }


}
