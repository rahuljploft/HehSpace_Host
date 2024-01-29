package com.hehspace_host.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class utilss {
    static String[] appPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int PERMISSION_REUEST_CODE = 1240;

    public static boolean checkAndRequestPermissions(Activity context) {
        List<String> listpermissionneeds = new ArrayList<>();
        //check with permission granted
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                listpermissionneeds.add(perm);
            }
        }
        //ask for non -granted permssions
        if (!listpermissionneeds.isEmpty()) {
            ActivityCompat.requestPermissions(context, listpermissionneeds.toArray(new String[listpermissionneeds.size()])
                    , PERMISSION_REUEST_CODE);
            return false;
        }
        return true;
    }
}
