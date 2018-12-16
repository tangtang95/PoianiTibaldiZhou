package com.trackme.trackmeapplication.baseUtility;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView {

    private Unbinder mUnBinder = null;

    private ProgressBar progressBar = null;

    protected @NonNull abstract P getPresenterInstance();

    protected P mPresenter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnBinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mUnBinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showProgress(){
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(){
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public View getContentView() {
        return getWindow().getDecorView();
    }

    protected ProgressBar getProgressBar(){
        return progressBar;
    }

    protected Unbinder getmUnBinder() {
        return mUnBinder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !((Object) this).getClass().getSimpleName().equals(o.getClass().getSimpleName())) return false;
        BaseActivity<?> that = (BaseActivity<?>) o;
        return Objects.equals(mUnBinder, that.mUnBinder) &&
                Objects.equals(progressBar, that.progressBar) &&
                Objects.equals(mPresenter, that.mPresenter);
    }

}
