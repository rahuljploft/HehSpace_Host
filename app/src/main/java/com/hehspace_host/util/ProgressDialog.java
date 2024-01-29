package com.hehspace_host.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hehspace_host.R;
import com.hehspace_host.util.custom_loader.AVLoadingIndicatorView;


public class ProgressDialog {
    private static Dialog dialog = null;
    static ShimmerFrameLayout shimmerFrameLayout;
    static LinearLayout rl_loading;
    static AVLoadingIndicatorView avLoadingIndicatorView;
    public static void showProgressDialog(Activity context) {
        if (dialog == null)
            dialog = new Dialog(context);
        dialog.setContentView(R.layout.loader);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        avLoadingIndicatorView = dialog.findViewById(R.id.loader);
        rl_loading = dialog.findViewById(R.id.rl_loading);
        shimmerFrameLayout = dialog.findViewById(R.id.shimmerFrameLayout);
        rl_loading.setVisibility(View.VISIBLE);
        shimmerFrameLayout.setVisibility(View.GONE);
        avLoadingIndicatorView.setIndicator("BallClipRotateMultipleIndicator");
        dialog.show();
    }

    public static void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    private static final String[] INDICATORS=new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",
            "com.wang.avi.sample.MyCustomIndicator"
    };

}