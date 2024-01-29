package com.hehspace_host.components.splash;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.addproperty.AddOnServiceActivity;
import com.hehspace_host.components.addproperty.PropertyDetailsActivity;
import com.hehspace_host.components.addproperty.PropertySummaryActivity;
import com.hehspace_host.components.addproperty.UploadPhotoActivity;
import com.hehspace_host.components.login.LoginActivity;
import com.hehspace_host.components.rating.RatingReviewActivity;
import com.hehspace_host.components.request.RequestActivity;
import com.hehspace_host.components.request.RequestDetailsActivity;
import com.hehspace_host.databinding.ActivitySplashBinding;
import com.hehspace_host.ui.base.BaseBindingActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseBindingActivity {
    ActivitySplashBinding activitySplashBinding;
    String token = "";
    @Override
    protected void setBinding() {
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        if (!token.isEmpty())
                            sessionManager.setDEVICE_TOKEN(token);
                        // Log and toast
                        //    Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        printHashKey(this);

    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            if(sessionManager.isLogin()){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        },2000);

    }

    public void printHashKey(Context context) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hehspace_host",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}