package com.trackme.trackmeapplication.baseUtility;

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

}
