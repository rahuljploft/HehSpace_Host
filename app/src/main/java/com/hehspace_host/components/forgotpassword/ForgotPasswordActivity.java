package com.hehspace_host.components.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hehspace_host.R;
import com.hehspace_host.databinding.ActivityForgotPasswordBinding;
import com.hehspace_host.model.ForgotPasswordModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.ui.base.BaseBindingActivity;
import com.hehspace_host.util.Constant;
import com.hehspace_host.util.GenericKeyEvent;
import com.hehspace_host.util.GenericTextWatcher;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;
import com.hehspace_host.util.custom_snackbar.CookieBar;

import java.util.HashMap;

public class ForgotPasswordActivity extends BaseBindingActivity {

    ActivityForgotPasswordBinding activityForgotPasswordBinding;
    ForgotPasswordView_Model view_model;
    String verifactionOtp = "";
    String user_id = "";

    @Override
    protected void setBinding() {
        activityForgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        view_model = new ForgotPasswordView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.forgotpasswordlivedata.observe(this, forgotPasswordModelApiResponse -> handleResult(forgotPasswordModelApiResponse));
    }

    @Override
    protected void setListeners() {
        activityForgotPasswordBinding.ivBack.setOnClickListener(this);
        activityForgotPasswordBinding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
        if (view.getId() == R.id.btnSubmit) {

            if (TextUtils.isEmpty(activityForgotPasswordBinding.etEmail.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Email Cannot be Blank")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (!Uitility.isValidEmail(activityForgotPasswordBinding.etEmail.getText().toString())) {
                CookieBar.build(mActivity)
                        .setTitle(R.string.app_name)
                        .setTitleColor(R.color.black)
                        .setMessageColor(R.color.black)
                        .setMessage("Enter Valid Email")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setIconAnimation(R.animator.icon_anim)
                        .setIcon(R.drawable.logo_black)
                        .setDuration(5000) // 5 seconds
                        .show();
                return;
            }
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("email_address",activityForgotPasswordBinding.etEmail.getText().toString());
                reqData.put("user_type", Constant.USERTYPE);
                view_model.forgotPassword(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }

        }

    }

    private void handleResult(ApiResponse<ForgotPasswordModel> result) {
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
                        verifactionOtp = result.getData().data.verifactionOtp;
                        user_id = result.getData().data.userId;
                        showOTPPopUp();
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void showOTPPopUp(){
        View view1 = getLayoutInflater().inflate(R.layout.bottomsheet_verify_dailog, null);
        final BottomSheetDialog dialog1 = new BottomSheetDialog(this);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(view1);
        ImageView close = dialog1.findViewById(R.id.close);
        TextView btnVerify = dialog1.findViewById(R.id.btnVerify);
        EditText otpEdit1 = dialog1.findViewById(R.id.otp_edit_1);
        EditText otpEdit2 = dialog1.findViewById(R.id.otp_edit_2);
        EditText otpEdit3 = dialog1.findViewById(R.id.otp_edit_3);
        EditText otpEdit4 = dialog1.findViewById(R.id.otp_edit_4);
        EditText otpEdit5 = dialog1.findViewById(R.id.otp_edit_5);
        EditText otpEdit6 = dialog1.findViewById(R.id.otp_edit_6);
        LinearLayout layoutResend = dialog1.findViewById(R.id.layoutResend);

        layoutResend.setOnClickListener(v -> {
            dialog1.dismiss();
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("email_address",activityForgotPasswordBinding.etEmail.getText().toString());
                reqData.put("user_type", Constant.USERTYPE);
                view_model.forgotPassword(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }
        });

        assert otpEdit1 != null;
        otpEdit1.addTextChangedListener(new GenericTextWatcher(otpEdit1, otpEdit2));
        assert otpEdit2 != null;
        otpEdit2.addTextChangedListener(new GenericTextWatcher(otpEdit2, otpEdit3));
        assert otpEdit3 != null;
        otpEdit3.addTextChangedListener(new GenericTextWatcher(otpEdit3, otpEdit4));
        assert otpEdit4 != null;
        otpEdit4.addTextChangedListener(new GenericTextWatcher(otpEdit4, otpEdit5));
        assert otpEdit5 != null;
        otpEdit5.addTextChangedListener(new GenericTextWatcher(otpEdit5, otpEdit6));
        assert otpEdit6 != null;
        otpEdit6.addTextChangedListener(new GenericTextWatcher(otpEdit6, null));

        otpEdit2.setOnKeyListener(new GenericKeyEvent(otpEdit2, otpEdit1));
        otpEdit3.setOnKeyListener(new GenericKeyEvent(otpEdit3, otpEdit2));
        otpEdit4.setOnKeyListener(new GenericKeyEvent(otpEdit4, otpEdit3));
        otpEdit5.setOnKeyListener(new GenericKeyEvent(otpEdit5, otpEdit4));
        otpEdit6.setOnKeyListener(new GenericKeyEvent(otpEdit6, otpEdit5));

        assert close != null;
        close.setOnClickListener(v ->
                dialog1.dismiss());

        assert btnVerify != null;
        btnVerify.setOnClickListener(v -> {
                    String otp = otpEdit1.getText().toString() + "" + otpEdit2.getText().toString() + "" + otpEdit3.getText().toString() + "" +
                            otpEdit4.getText().toString() + "" + otpEdit5.getText().toString() + "" + otpEdit6.getText().toString();
                    if (TextUtils.isEmpty(otp)) {
                        Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!otp.equals(verifactionOtp)) {
                        Toast.makeText(this, "OTP is Invalid.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(new Intent(this,SetPasswordActivity.class).putExtra("user_id",user_id));
                }
        );
        dialog1.show();
    }

}