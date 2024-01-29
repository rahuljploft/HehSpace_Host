package com.hehspace_host.util;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class GenericKeyEvent implements View.OnKeyListener {

    private final EditText currentView;
    private final EditText previousView;

    public GenericKeyEvent(EditText currentView, EditText previousView) {
        this.currentView = currentView;
        this.previousView = previousView;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getText().toString().isEmpty()) {
            if (previousView != null) {
                previousView.requestFocus();
            }
            return true;
        }
        return false;
    }
}