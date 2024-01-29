package com.hehspace_host.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.hehspace_host.R;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Uitility {

    static String[] appPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE


    };
    public static final int PERMISSION_REUEST_CODE1 = 1240;

    public static boolean isValidEmail(String email) {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches();
    }
    public static String removeLastCharacter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static  String getRealPathFromURI(Context context,String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void ratingApp(Context context) {

        final Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {

            Uri uriUrl = Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName());
            context.startActivity(new Intent(Intent.ACTION_VIEW, uriUrl));
        }
    }

    public static boolean isOnline(Context context) {
        boolean isDeviceOnLine = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo != null && netInfo.isConnected();
        }
        return isDeviceOnLine;
    }


    @SuppressLint("ClickableViewAccessibility")
    public static void setOntouchLitener(Activity activity, TextInputEditText textInputEditText) {
        textInputEditText.setOnTouchListener((v, event) -> {
            @SuppressLint("ClickableViewAccessibility") Typeface typeface = ResourcesCompat.getFont(activity, R.font.muli_semi_bold);
            textInputEditText.setTypeface(typeface);
            return false;
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    public static void setOntouchLitener1(Activity activity, TextInputEditText textInputEditText) {
        @SuppressLint("ClickableViewAccessibility") Typeface typeface = ResourcesCompat.getFont(activity, R.font.muli_semi_bold);
        textInputEditText.setTypeface(typeface);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }
    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromUri(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void nointernetDialog(Activity activity){
        CookieBar.build(activity)
                .setTitle(R.string.app_name)
                .setTitleColor(R.color.black)
                .setMessageColor(R.color.black)
                .setMessage("Your Internet not Connect with device Please Connect it.")
                .setBackgroundColor(R.color.colorPrimary)
                .setIconAnimation(R.animator.icon_anim)
                .setIcon(R.drawable.no_internet)
                .setDuration(5000) // 5 seconds
                .show();
    }



    public static void Call(Activity activity,String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        activity.startActivity(intent);
    }
//    public static void nointernetDialog(Activity activity){
//        CookieBar.build(activity)
//                .setTitle(R.string.app_name)
//                .setTitleColor(R.color.black)
//                .setMessageColor(R.color.black)
//                .setMessage("Your Internet not Connect with device Please Connect it.")
//                .setBackgroundColor(R.color.color_00CFE2)
//                .setIconAnimation(R.animator.icon_anim)
//                .setIcon(R.drawable.no_internet)
//                .setDuration(5000) // 5 seconds
//                .show();
//    }
}