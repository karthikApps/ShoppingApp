package com.android.shoppingapp.Global;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.ecommerce.R;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommonFunctions {

    public static Dialog mDialog, load_dialog;
    public static Dialog mProgressDialog;

    public static void showProgressDialog(Context context) {
        if (context != null) {
            mProgressDialog = new Dialog(context, R.style.ProgressDialog);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(R.layout.layout_progress);

            mProgressDialog.show();
        }
    }

    public static void removeProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    //Short Toast
    /*public static void shortToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }*/

    public static void shortToast(View view, String mMessage, int mDuration, Context activity) {
        Snackbar mSnackbar;
        mSnackbar = Snackbar.make(view, mMessage, mDuration);
        View mSnackbar_view = mSnackbar.getView();
        mSnackbar_view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) mSnackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(activity.getResources().getColor(R.color.white));
        mSnackbar.show();
    }

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    public static boolean checkLocationPermission(Context context)
    {
        String coarse = Manifest.permission.ACCESS_COARSE_LOCATION;
        String fine = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = context.checkCallingOrSelfPermission(coarse);
        int res1 = context.checkCallingOrSelfPermission(fine);
        return (res == PackageManager.PERMISSION_GRANTED && res1 == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isGPSEnabled(Context mContext){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void jsonValidation(Context context,int code,String message){

        switch (code){

            case 500:
                //shortToast(context,message);
                break;

            case 401:
               // shortToast(context,message);
                SessionManager sessionManager = new SessionManager(context);
                sessionManager.logoutUser();
                break;
        }

    }

}
