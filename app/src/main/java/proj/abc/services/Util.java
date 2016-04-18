package proj.abc.services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import proj.abc.MainActivity;

/**
 * Created by tonnyquintos on 10/24/15.
 */
public class Util {

    private static Util instance;

    private Util(){

    }

    public static Util getInstance(){
        if(instance == null){
            instance = new Util();
        }
        return instance;
    }


    public void showDialog(Context ctx, String message, String okButton, String cancelButton
      , DialogInterface.OnClickListener positiveListener
      , DialogInterface.OnClickListener negativeListener){
        new AlertDialog.Builder(ctx)
          .setTitle(MainActivity.APP_CODE)
          .setMessage(message)
          .setPositiveButton(okButton, positiveListener)
          .setNegativeButton(cancelButton, negativeListener)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    }

    public void showDialog(Context ctx,String title, String message, String okButton
      , DialogInterface.OnClickListener positiveListener ){
        new AlertDialog.Builder(ctx)
          .setTitle(title)
          .setMessage(message)
          .setPositiveButton(okButton, positiveListener)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    }

    public void showDialog(Context ctx, String message, String okButton
      , DialogInterface.OnClickListener positiveListener ){
      try {
        new AlertDialog.Builder(ctx)
          .setTitle(MainActivity.APP_CODE)
          .setMessage(message)
          .setPositiveButton(okButton, positiveListener)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
      }catch(Exception e){

      }
    }

    public void showSnackBarToast(Context context, String message){
      int currentapiVersion = android.os.Build.VERSION.SDK_INT;
      if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
        // Do something for lollipop and above versions
        Snackbar.make(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
      } else{
        // do something for phones running an SDK before lollipop
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
      }
    }



}
