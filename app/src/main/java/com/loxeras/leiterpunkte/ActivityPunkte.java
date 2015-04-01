package com.loxeras.leiterpunkte;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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


public class ActivityPunkte extends Activity {

    Spinner leiter;
    private static final int REQUEST_CODE_EMAIL = 1;
    InputStream is = null;
    String result = null;
    String line = null;
    public int code;
    private ProgressDialog progress;
    ProgressDialog dialog;

    public String value_accountName = null;
    public String value_leiter = null;
    public String value_grund = null;
    public String value_punkte;
    public String value_time;
    public String nix;
    public Boolean grund_edited = false;
    public Boolean punkte_edited = false;
    public ListView lv;

    MySql MySql = new MySql();

    protected String[] _options = {"Kessler Julia", "Meier Raphael", "Meile Janine", "Bär Kevin", "Boers Sebas", "Bosetti Simona", "Domeniconi Rahel", "Duff Dario", "Duff Silvio", "Duft Cedric"
            , "Eichmüller Dario", "Eichmüller Remo", "Gebs Jessica", "Giger Nino", "Hollenstein Benedikt", "Kalberer Simon", "Knaus Katharina", "Krieg Larissa"
            , "Lendi Simona", "Lussy Yannic", "Meier Matthias", "Rickenbach Seppi", "Schirmer Raphael", "Schubiger Simon", "Schwyter Julia", "Spring Lisa", "Steiner Adrian"
            , "Steiner Lea", "Thoma Saskia", "Widmer Miriam", "Zahner Fabienne"};
    protected boolean[] _selections = new boolean[_options.length];

    Button upload, download;
    EditText grund, punkte;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkte);

        lv = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

       getList();
       //adapter.notifyDataSetChanged();
      // lv.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_multyinsert) {
            showDialog(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPoints() {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                        nameValuePairs.add(new BasicNameValuePair("leiter", value_leiter));
                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://www.old.jubla-uzna.ch/getPoints.php");
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            is = entity.getContent();
                        } catch (Exception e) {
                            Log.e("Fail 1", e.toString());
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

    public void getList(){
        for( int i = 0; i < _options.length; i++ ){
            value_leiter = _options[ i ];
        try {
            list.add(getPoints());

        }catch(Exception e){
            Log.e("FAIL",e.toString());
        }
        }



    }
}


