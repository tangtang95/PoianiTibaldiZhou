package com.trackme.trackmeapplication.baseUtility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Context mContext = getContext();

    private Unbinder mUnBinder = null;

    protected abstract int getLayoutResID();

    protected abstract void setUpFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResID(), container, false);
        mUnBinder = ButterKnife.bind(this, v);
        setUpFragment();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mContext = null;
        mUnBinder.unbind();
        super.onDetach();
    }

    public void showMessage(String message) {
        Toast.makeText(getmContext(), message, Toast.LENGTH_LONG).show();
    }

    public Context getmContext(){
        return mContext;
    }

}
