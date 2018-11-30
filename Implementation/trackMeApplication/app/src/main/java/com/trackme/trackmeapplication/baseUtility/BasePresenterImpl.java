package com.trackme.trackmeapplication.baseUtility;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V>{

    protected V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    public V getView() {
        return mView;
    }
}
