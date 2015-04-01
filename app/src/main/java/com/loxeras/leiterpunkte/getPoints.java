package com.loxeras.leiterpunkte;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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
 * Created by dario.duff on 25.03.2015.
 */
public class getPoints extends AsyncTask<String, Void, Void> {
    private ProgressDialog dialog;
    public String result1=null;
    private String line=null;
    private InputStream is = null;
    public String leiter = "";

    public getPoints(MainActivity mainActivity) {
        dialog = new ProgressDialog(mainActivity);
    }

    Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            String subed;
            subed = result1.substring(17,result1.length()-4);
            MainActivity.punkte.setText(subed);

        }
    };




    @Override
    protected void onPreExecute() {
        dialog.setMessage("Bitte warten...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

            Message msg=new Message();
            msg.obj="";
            mHandler.sendMessage(msg);


    }

    @Override
    protected Void doInBackground(String... params) {
        try {

            leiter = params[0];

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("leiter", params[0]));

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
                result1 = sb.toString();
                Log.e("string:", result1);
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }
            Thread.sleep(200);

            return null;
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            //  Toast.makeText(getApplicationContext(), e.toString() + "Invalid IP Address",
            // Toast.LENGTH_LONG).show();
        }

        return null;

    }


}
