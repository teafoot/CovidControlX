package com.example.covidcontrolx.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;

import com.example.covidcontrolx.LoginActivity;

import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;

public class NetworkUtils {
    public static boolean getConnection(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    return true;
                else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        }
        return false;
    }
////    Doesn't work
//    public static class CheckConnection extends TimerTask {
//        private final Context context;
//        public CheckConnection(Context context){
//            this.context = context;
//        }
//        public void run() {
//            if(!NetworkUtils.getConnection(context)){
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("No Internet Connection");
//                builder.setMessage("Please connect to the internet to use the app.");
//                final AlertDialog dialog = builder.create();
//                dialog.setButton(Dialog.BUTTON_POSITIVE, "Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.setButton(Dialog.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialog.dismiss();
//                        Process.killProcess(android.os.Process.myPid());
//                    }
//                });
//                dialog.show();
//            }
//        }
//    }
}
