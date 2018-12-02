package com.trackme.trackmeapplication.baseUtility;

import android.support.annotation.NonNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivityDelegate<
        V extends BaseView,
        P extends BasePresenterImpl<V>> {

    private Unbinder mUnBinder = null;

    protected P mPresenter;

    public void onCreate(P presenter) {
        mPresenter = presenter;
        mUnBinder = ButterKnife.bind(this, mPresenter.getView().getContentView());
    }

    public void onDestroy() {
        mUnBinder.unbind();
    }

    public Unbinder getmUnBinder() {
        return mUnBinder;
    }
}
