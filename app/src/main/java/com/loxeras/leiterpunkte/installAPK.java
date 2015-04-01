package com.loxeras.leiterpunkte;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by dario.duff on 25.03.2015.
 * Install an apc who is stored at a specific destination.
 * Used by file: DownloadAsync.apk
 *
 * BSP:
 * installAPK.installApp(com.loxeras.dalitools.IR_activity.context);
 */
public class installAPK {
    public static ProgressDialog dialog;

    //Defines the name of the APK to install
    private static final String APK_NAME = "app-debug.apk";

    //Forces to update the APK
    public static void installApp(Context mycontext) {
        Intent installer = new Intent();
        installer.setAction(Intent.ACTION_VIEW);
        String PATH = "file://" + Environment.getExternalStorageDirectory() + "/download/"+APK_NAME;
        installer.setDataAndType(Uri.parse(PATH), "application/vnd.android.package-archive");
        installer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mycontext.startActivity(installer);
    }
}


