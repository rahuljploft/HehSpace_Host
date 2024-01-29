package com.hehspace_host.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hehspace_host.R;
import com.hehspace_host.util.SessionManager;


public abstract class BaseBindingActivity extends AppCompatActivity implements ToolbarCallback, OnClickListener {

    public AppCompatActivity mActivity = null;

    protected SessionManager sessionManager = null;

    protected Fragment fragment = null;

    protected abstract void setBinding();

    protected abstract void createActivityObject(Bundle savedInstanceState);

    protected abstract void initializeObject();

    protected abstract void setListeners();

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(this);
        createActivityObject(savedInstanceState);
        setBinding();
        initializeObject();
        setListeners();
    }


    @Override
    public void setToolbarCustomTitle(String titleKey) {

    }

    /*@Override
    public boolean  onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity == null) throw new NullPointerException("must create activity object");
        Env.currentActivity = mActivity;
    }

    public void changeFragment(Fragment fragment, Boolean isAddToBack) {
        this.fragment = fragment;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getName());
        if (isAddToBack) transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    public void changeFragment(Fragment fragment, Boolean isAddToBack, Bundle bundle) {
        this.fragment = fragment;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment.getClass(), bundle, fragment.getClass().getName());
        if (isAddToBack) transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = this.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            fragment.setArguments(bundle);
            ft.replace(R.id.container, fragment, fragmentTag);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void showUpIconVisibility(boolean isVisible) {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(isVisible);
    }

}
