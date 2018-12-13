package com.trackme.trackmeapplication.groupRequest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trackme.trackmeapplication.R;

import butterknife.ButterKnife;

public class GroupRequestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View groupRequestFragment = inflater.inflate(R.layout.fragment_group_request, container, false);
        ButterKnife.bind(this, groupRequestFragment);
        return groupRequestFragment;
    }
}
