package com.trackme.trackmeapplication.baseUtility;

import android.view.View;

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showMessage(String message);

    View getContentView();
}
