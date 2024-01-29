package com.hehspace_host.components.otp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.register.RegisterView_Model;
import com.hehspace_host.databinding.ActivityOtpactivityBinding;
import com.hehspace_host.model.LoginModel;
import com.hehspace_host.model.VerifyOtpModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.GenericKeyEvent;
import com.hehspace_host.util.GenericTextWatcher;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.io.File;
import java.util.HashMap;

public class OTPActivity extends BaseBindingActivity {
    ActivityOtpactivityBinding activityOtpactivityBinding;
    RegisterView_Model view_model;
    String   verifyOTP ="";
    File imagefile = null;
    boolean issocial = false;


    @Override
    protected void setBinding() {
        activityOtpactivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_otpactivity);
        view_model = new RegisterView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.registerlivedata.observe(this, login_modelApiResponse -> handleResult(login_modelApiResponse));
        view_model.verifylivedata.observe(this, login_modelApiResponse -> resendhandleResult(login_modelApiResponse));
        activityOtpactivityBinding.otpEdit1.addTextChangedListener(new GenericTextWatcher(activityOtpactivityBinding.otpEdit1, activityOtpactivityBinding.otpEdit2));
        activityOtpactivityBinding.otpEdit2.addTextChangedListener(new GenericTextWatcher(activityOtpactivityBinding.otpEdit2, activityOtpactivityBinding.otpEdit3));
        activityOtpactivityBinding.otpEdit3.addTextChangedListener(new GenericTextWatcher(activityOtpactivityBinding.otpEdit3, activityOtpactivityBinding.otpEdit4));
        activityOtpactivityBinding.otpEdit4.addTextChangedListener(new GenericTextWatcher(activityOtpactivityBinding.otpEdit4, activityOtpactivityBinding.otpEdit5));
        activityOtpactivityBinding.otpEdit5.addTextChangedListener(new GenericTextWatcher(activityOtpactivityBinding.otpEdit5, activityOtpactivityBinding.otpEdit6));
        activityOtpactivityBinding.otpEdit6.addTextChangedListener(new GenericTextWatcher(activityOtpactivityBinding.otpEdit6, null));

        activityOtpactivityBinding.otpEdit2.setOnKeyListener(new GenericKeyEvent(activityOtpactivityBinding.otpEdit2, activityOtpactivityBinding.otpEdit1));
        activityOtpactivityBinding.otpEdit3.setOnKeyListener(new GenericKeyEvent(activityOtpactivityBinding.otpEdit3, activityOtpactivityBinding.otpEdit2));
        activityOtpactivityBinding.otpEdit4.setOnKeyListener(new GenericKeyEvent(activityOtpactivityBinding.otpEdit4, activityOtpactivityBinding.otpEdit3));
        activityOtpactivityBinding.otpEdit5.setOnKeyListener(new GenericKeyEvent(activityOtpactivityBinding.otpEdit5, activityOtpactivityBinding.otpEdit4));
        activityOtpactivityBinding.otpEdit6.setOnKeyListener(new GenericKeyEvent(activityOtpactivityBinding.otpEdit6, activityOtpactivityBinding.otpEdit5));
        verifyOTP = getIntent().getStringExtra("otp");
        activityOtpactivityBinding.tvOTP.setText(verifyOTP);
        imagefile = new File(getIntent().getStringExtra("imagefile"));
        issocial = !getIntent().getStringExtra("socialmediaID").equals("");

    }

    private void resendhandleResult(ApiResponse<VerifyOtpModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        verifyOTP = result.getData().data.verifactionOtp;
                        activityOtpactivityBinding.tvOTP.setText(verifyOTP);
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }


    @Override
    protected void setListeners() {
        activityOtpactivityBinding.btnVerify.setOnClickListener(this);
        activityOtpactivityBinding.layoutResend.setOnClickListener(this);
        activityOtpactivityBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.btnVerify) {
            String otp = activityOtpactivityBinding.otpEdit1.getText().toString() + "" + activityOtpactivityBinding.otpEdit2.getText().toString() + ""
                    + activityOtpactivityBinding.otpEdit3.getText().toString() + "" +
                    activityOtpactivityBinding.otpEdit4.getText().toString() + "" + activityOtpactivityBinding.otpEdit5.getText().toString() + "" +
                    activityOtpactivityBinding.otpEdit6.getText().toString();
            if (TextUtils.isEmpty(otp)) {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!otp.equals(verifyOTP)) {
                Toast.makeText(this, "OTP is Invalid.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Uitility.isOnline(this)) {
                if(getIntent().getStringExtra("imagefile").isEmpty()){
                    view_model.createAccount(getIntent().getStringExtra("fname"), getIntent().getStringExtra("lname")
                            , getIntent().getStringExtra("email"), getIntent().getStringExtra("phone"), getIntent().getStringExtra("genderId")
                            , getIntent().getStringExtra("password"), getIntent().getStringExtra("socialmediaID"), Constant.USERTYPE,
                            imagefile,getIntent().getStringExtra("imagefile"),  sessionManager.getetDEVICE_TOKEN(),issocial);
                }
                else {
                    view_model.createAccount(getIntent().getStringExtra("fname"), getIntent().getStringExtra("lname")
                            , getIntent().getStringExtra("email"), getIntent().getStringExtra("phone"), getIntent().getStringExtra("genderId")
                            , getIntent().getStringExtra("password"), getIntent().getStringExtra("socialmediaID"), Constant.USERTYPE,
                            imagefile,getIntent().getStringExtra("imagefile"),  sessionManager.getetDEVICE_TOKEN(),issocial);
                }

            }
            else {
                Uitility.nointernetDialog(this);
            }

        }

        if(view.getId() == R.id.layoutResend){
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("email_address",getIntent().getStringExtra("email"));
                view_model.verifyOTP(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }
        }
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<LoginModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        sessionManager.setLogin();
                        sessionManager.setUSER_ID(result.getData().data.profileData.userId);
                        sessionManager.setEMAIL(result.getData().data.profileData.emailAddress);
                        sessionManager.setNAME(result.getData().data.profileData.firstName +" "+ result.getData().data.profileData.lastName);
                        sessionManager.setPHONE(result.getData().data.profileData.mobileNumber);
                        sessionManager.setUSER_TYPE(Constant.USERTYPE);
                        sessionManager.setAuthToken(result.getData().data.accessToken);
                        sessionManager.setPROFILE_IMAGE(result.getData().data.profileData.userPicName);
                        startActivity(new Intent(OTPActivity.this, MainActivity.class));
                    }
                    Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


}