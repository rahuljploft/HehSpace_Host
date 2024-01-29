package com.hehspace_host.components.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.login.LoginActivity;
import com.hehspace_host.databinding.ActivitySetPasswordBinding;
import com.hehspace_host.model.CommonModel;
import com.hehspace_host.model.ForgotPasswordModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import java.util.HashMap;

public class SetPasswordActivity extends BaseBindingActivity {
    ActivitySetPasswordBinding activitySetPasswordBinding;
    SetPasswordView_Model view_model;

    @Override
    protected void setBinding() {
        activitySetPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_password);
        view_model = new SetPasswordView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.resetpasswordlivedata.observe(this, forgotPasswordModelApiResponse -> handleResult(forgotPasswordModelApiResponse));
    }

    @Override
    protected void setListeners() {
        activitySetPasswordBinding.ivBack.setOnClickListener(this);
        activitySetPasswordBinding.btnSubmit.setOnClickListener(this);
        activitySetPasswordBinding.password.setOnClickListener(this);
        activitySetPasswordBinding.password1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
        if(view.getId() == R.id.password){
            ShowHidePass(view);
        }
        if(view.getId() == R.id.password1){
            ShowHidePass(view);
        }
        if(view.getId() == R.id.btnSubmit){
            if (TextUtils.isEmpty(activitySetPasswordBinding.etNewpassword.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Enter new Password")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (TextUtils.isEmpty(activitySetPasswordBinding.etConfirmPassword.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Enter confirm Password")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (!activitySetPasswordBinding.etConfirmPassword.getText().toString().equals(activitySetPasswordBinding.etNewpassword.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Password doesn't match.")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }

            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                if(getIntent().getStringExtra("user_id") == null){
                    reqData.put("user_id", sessionManager.getUSER_ID());
                }
                else reqData.put("user_id",getIntent().getStringExtra("user_id"));
                reqData.put("user_password", activitySetPasswordBinding.etNewpassword.getText().toString());
                reqData.put("_method", "PUT");
                view_model.forgotPassword(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }
        }
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.password) {
            if (activitySetPasswordBinding.etNewpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eyehide);
                //Show Password
                activitySetPasswordBinding.etNewpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eyeshow);
                //Hide Password
                activitySetPasswordBinding.etNewpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        if (view.getId() == R.id.password1) {
            if (activitySetPasswordBinding.etConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eyehide);
                //Show Password
                activitySetPasswordBinding.etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eyeshow);
                //Hide Password
                activitySetPasswordBinding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }


    private void handleResult(ApiResponse<CommonModel> result) {
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
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                        if(getIntent().getStringExtra("user_id") == null){
                            startActivity(new Intent(SetPasswordActivity.this, MainActivity.class));
                        }
                        else startActivity(new Intent(SetPasswordActivity.this, LoginActivity.class));
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}