package com.trackme.trackmeapplication.baseUtility;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasePresenterImpl<?> that = (BasePresenterImpl<?>) o;
        return Objects.equals(mView, that.mView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mView);
    }
}
