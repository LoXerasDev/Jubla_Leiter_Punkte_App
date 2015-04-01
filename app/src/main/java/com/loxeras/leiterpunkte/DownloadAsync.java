package com.loxeras.leiterpunkte;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dario.duff on 25.03.2015.
 * Downloads apk from server. Change values "DataOutputStream", "URL" and "installApp" context.
 */
public class DownloadAsync extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;

    public DownloadAsync(MainActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Lade Daten. Bitte warten...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL u = new URL(MainActivity.url); //EDIT
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            final DataOutputStream fos = new DataOutputStream(new FileOutputStream(MainActivity.outputFile));  //EDIT
            fos.write(buffer);
            fos.flush();
            fos.close();
            if(dialog.isShowing())
                dialog.dismiss();

        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e + "");

        } catch (IOException e) {
            Log.e("IOException", e + "");

        }
        //Installiert die APK mit vorgegebenem context
        installAPK.installApp(com.loxeras.leiterpunkte.MainActivity.context);       //EDIT
        return null;
    }

}