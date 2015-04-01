package com.loxeras.leiterpunkte;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    Spinner leiter;
    private static final int REQUEST_CODE_EMAIL = 1;
    InputStream is=null;
    String result=null;
    String line=null;
    ProgressDialog dialog;

    public String value_accountName = null;
    public String value_leiter = null;
    public String value_grund = "dummy";
    public String value_punkte;
    public String value_time;
    public String nix;
    public Boolean grund_edited = false;
    public Boolean punkte_edited = false;

    public int versionCode = 0;
    public int newversion = 0;
    public static File outputFile;
    public File file;
    public static String url = "http://www.old.jubla-uzna.ch/android/leiterpunkte/app-debug.apk";

    public static Boolean listcreate = false;
    public static String punkteliste = "";

    public static Context context;

    MySql MySql = new MySql();

    protected CharSequence[] _options = { "Kessler Julia","Meier Raphael","Meile Janine","Bär Kevin","Boers Sebas","Bosetti Simona","Domeniconi Rahel","Duff Dario","Duff Silvio","Duft Cedric"
            ,"Eichmüller Dario","Eichmüller Remo","Gebs Jessica","Giger Nino","Hollenstein Benedikt","Kalberer Simon","Knaus Katharina","Krieg Larissa"
            ,"Lendi Simona","Lussy Yannic","Meier Matthias","Rickenbach Seppi","Schirmer Raphael","Schubiger Simon","Schwyter Julia","Spring Lisa","Steiner Adrian"
            ,"Steiner Lea","Thoma Saskia","Widmer Miriam","Zahner Fabienne" };
    protected boolean[] _selections =  new boolean[ _options.length ];

    Button upload,download;
   static EditText grund,punkte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leiter = (Spinner) findViewById(R.id.leiter);
        upload = (Button) findViewById(R.id.upload);
        download = (Button) findViewById(R.id.punkte_abrufen);
        grund = (EditText) findViewById(R.id.grund);
        punkte = (EditText) findViewById(R.id.punkte);



        context = getApplicationContext();


        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        checkVersion checkVersion = new checkVersion();

        try {
            newversion = Integer.valueOf(checkVersion.getVersion());
            //String vc = checkversion.getVersion();
        } catch (IOException e) {
            e.printStackTrace();
        }
        compareVerison();











        getMail();
        upload.setEnabled(false);

        grund.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(grund.length() == 0){
                    grund_edited = false;
                }
                if(punkte_edited == grund_edited == true){
                    upload.setEnabled(true);
                }else{upload.setEnabled(false);}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                grund_edited = true;
                if(grund.length() == 0){
                    grund_edited = false;
                }
                if(punkte_edited == grund_edited == true){
                    upload.setEnabled(true);
                }else{upload.setEnabled(false);}

            }
        });
        punkte.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(punkte.length() == 0){
                    punkte_edited = false;
                }
                if(punkte_edited == grund_edited == true){
                    upload.setEnabled(true);
                }else{upload.setEnabled(false);}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                punkte_edited = true;
                if(punkte.length() == 0){
                    punkte_edited = false;
                }
                if(punkte_edited == grund_edited == true){
                    upload.setEnabled(true);
                }else{upload.setEnabled(false);}
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                value_grund = grund.getText().toString();
                value_punkte = punkte.getText().toString();
                value_leiter = leiter.getSelectedItem().toString();
                value_time = String.valueOf(getTime());


                if (isOnline() == true) {
                   MySql.insert(value_time,value_accountName,value_leiter, value_punkte, value_grund);
                } else {
                    Toast.makeText(getApplicationContext(), "Keine Verbindung zum Internet!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOnline() == true) {
                    value_leiter = leiter.getSelectedItem().toString();

                    getPoints gpoint = new getPoints(MainActivity.this);
                    gpoint.execute(leiter.getSelectedItem().toString());

                    /*dialog = new ProgressDialog(MainActivity.this);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("Lade Daten. Bitte warten...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                   // dialog.show();


                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("UI thread", "I am the UI thread");
                            try {
                                nix = getPoints1();
                                while(nix == result && nix != ""){
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }catch (Exception e) {
                            }

                            dialog.dismiss();
                            String subed;
                            subed = result.substring(17,result.length()-4);
                            punkte.setText(subed);
                        }
                    });*/
                }else{
                    Toast.makeText(getApplicationContext(),"Keine Verbindung zum Internet!",
                    Toast.LENGTH_LONG).show();
                }
            }
        });
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
             showDialog( 0 );


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addLeiter() {
        List<String> list = new ArrayList<String>();

        list.add("Kessler Julia");
        list.add("Meier Raphael");
        list.add("Meile Janine");
        list.add("Bär Kevin");
        list.add("Boers Sebas");
        list.add("Bosetti Simona");
        list.add("Domeniconi Rahel");
        list.add("Duff Dario");
        list.add("Duff Silvio");
        list.add("Duft Cedric");
        list.add("Eichmüller Dario");
        list.add("Eichmüller Remo");
        list.add("Gebs Jessica");
        list.add("Giger Nino");
        list.add("Hollenstein Benedikt");
        list.add("Kalberer Simon");
        list.add("Knaus Katharina");
        list.add("Krieg Larissa");
        list.add("Lendi Simona");
        list.add("Lussy Yannic");
        list.add("Meier Matthias");
        list.add("Rickenbach Seppi");
        list.add("Schirmer Raphael");
        list.add("Schubiger Simon");
        list.add("Schwyter Julia");
        list.add("Spring Lisa");
        list.add("Steiner Adrian");
        list.add("Steiner Lea");
        list.add("Thoma Saskia");
        list.add("Widmer Miriam");
        list.add("Zahner Fabienne");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leiter.setAdapter(dataAdapter);
    }

    public String getMail(){
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            value_accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

        }
        addLeiter();
    }

    public long getTime(){
        long time= System.currentTimeMillis();
        return time;
    }

    public String getPoints1()
    {
        if(isOnline() == true) {
            value_leiter = leiter.getSelectedItem().toString();
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
        }

        //Toast.makeText(getBaseContext(), "Inserted Successfully",
        //        Toast.LENGTH_SHORT).show();
        return result;
    }

    public boolean isOnline() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    protected Dialog onCreateDialog( int id )
    {
        return
                new AlertDialog.Builder( this )
                        .setTitle( "Leiter" )
                        .setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
                        .setPositiveButton( "OK", new DialogButtonClickHandler() )
                        .create();
    }


    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
    {
        public void onClick( DialogInterface dialog, int clicked, boolean selected )
        {
            Log.i( "ME", _options[ clicked ] + " selected: " + selected );
        }
    }


    public class DialogButtonClickHandler implements DialogInterface.OnClickListener
    {
        public void onClick( DialogInterface dialog, int clicked )
        {
            switch( clicked )
            {
                case DialogInterface.BUTTON_POSITIVE:
                    printSelected();
                    break;
            }
        }
    }

    protected void printSelected(){
        value_grund = grund.getText().toString();
        value_punkte = punkte.getText().toString();
        value_time = String.valueOf(getTime());

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Lade Daten. Bitte warten...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        for( int i = 0; i < _options.length; i++ ){
            if(_selections[i] == true){
                MySql.insert(value_time,value_accountName,_options[i].toString(), value_punkte, value_grund);
            }
            Log.i( "ME", _options[ i ] + " selected: " + _selections[i] );
        }
        dialog.dismiss();
    }

    private void updateUI(String value)
    {
        String subed;
        subed = value.substring(17,result.length()-4);
        punkte.setText(subed);
    }

    private void download_update(){
        String PATH = Environment.getExternalStorageDirectory() + "/download/";
        file = new File(PATH);
        file.mkdirs();
        outputFile = new File(file,"app-debug.apk");

        DownloadAsync task = new DownloadAsync(MainActivity.this);
        task.execute();
    }

    public void compareVerison(){

        if( newversion > versionCode){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Möchten sie das Update herunterladen?");
            builder.setTitle("Update verfügbar!");
            builder.setPositiveButton("Herunterladen", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Intent intent = new Intent(Intent.ACTION_VIEW ,Uri.parse("http://www.old.jubla-uzna.ch/app-debug.apk"));
                    //startActivity(intent);

                    download_update();

                }

            });

            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {


                }

            });
            builder.show();
        }
    }
    public void punkteset(String i){
        punkte.setText( i);
    }
}


