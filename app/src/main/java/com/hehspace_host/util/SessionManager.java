package com.hehspace_host.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.BaseObservable;

public class SessionManager extends BaseObservable {
    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;
    private static SessionManager session;
    private final String IS_LOGIN = "isLoggedIn";
    private final String AUTH_TOKEN = "auth_token";
    private final String USER_ID = "user_id";
    private final String NAME = "name";
    private final String PHONE = "phone";
    private final String EMAIL = "email";
    private final String USER_TYPE = "user_type";
    private final String LATITUDE = "lat";
    private final String LONGTITUDE = "lng";
    private final String PROFILE_PHOTO = "profile_photo";


    public static SessionManager getInstance(Context context) {
        if (session == null) {
            session = new SessionManager();
        }
        if (shared == null) {
            shared = context.getSharedPreferences("Hehspacehost", MODE_PRIVATE);
            editor = shared.edit();
        }
        return session;
    }


    public boolean isLogin() {
        return shared.getBoolean(IS_LOGIN, false);
    }

    public void setLogin() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public String getAuthToken() {
        return shared.getString(AUTH_TOKEN, "");
    }

    public void setAuthToken(String authToken) {
        editor.putString(AUTH_TOKEN, authToken);
        editor.commit();
    }
    public String getUSER_TYPE() {
        return shared.getString(USER_TYPE, "");
    }

    public void setUSER_TYPE(String usertype) {
        editor.putString(USER_TYPE, usertype);
        editor.commit();
    }
    public String getLATITUDE() {
        return shared.getString(LATITUDE, "");
    }

    public void setlATITUDE(String lat) {
        editor.putString(LATITUDE, lat);
        editor.commit();
    }
    public String getLONGTITUDE() {
        return shared.getString(LONGTITUDE, "");
    }

    public void setLONGTITUDE(String lng) {
        editor.putString(LONGTITUDE, lng);
        editor.commit();
    }
    public String getEMAIL() {
        return shared.getString(EMAIL, "");
    }

    public void setEMAIL(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getSStatus() {
        return shared.getString("sstaus", "");
    }
    public String getStripeUrl() {
        return shared.getString("stripeurl", "");
    }
    public void setStripeURL(String stripeurl) {
        editor.putString("stripeurl", stripeurl);
        editor.commit();
    }
    public void setSStatus(String email) {
        editor.putString("sstaus", email);
        editor.commit();
    }

    public String getetDEVICE_TOKEN() {
        return shared.getString("DEVICETOKEN", "");
    }

    public void setDEVICE_TOKEN(String email) {
        editor.putString("DEVICETOKEN", email);
        editor.commit();
    }



    public String getUSER_ID() {
        return shared.getString(USER_ID, "");
    }

    public void setUSER_ID(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getNAME() {
        return shared.getString(NAME, "");
    }

    public void setNAME(String fullName) {
        editor.putString(NAME, fullName);
        editor.commit();
    }

    public String getFNAME() {
        return shared.getString("FNAME", "");
    }

    public void setFNAME(String fullName) {
        editor.putString("FNAME", fullName);
        editor.commit();
    }

    public String getLNAME() {
        return shared.getString("LNAME", "");
    }

    public void setLNAME(String fullName) {
        editor.putString("LNAME", fullName);
        editor.commit();
    }


    public String getPHONE() {
        return shared.getString(PHONE, "");
    }

    public void setPHONE(String phone) {
        editor.putString(PHONE, phone);
        editor.commit();
    }

    public String getPROFILE_IMAGE() {
        return shared.getString(PROFILE_PHOTO, "");
    }

    public void setPROFILE_IMAGE(String profileImage) {
        editor.putString(PROFILE_PHOTO, profileImage);
        editor.commit();
    }
    public void logout() {
        editor.putString(USER_ID, "");
        editor.putString(NAME, "");
        editor.putString(PHONE, "");
        editor.putString(EMAIL, "");
        editor.putString(PROFILE_PHOTO, "");
        editor.putString(AUTH_TOKEN, "");
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();

    }
}