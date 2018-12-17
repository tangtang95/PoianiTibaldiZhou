package com.trackme.trackmeapplication.Request.individualRequest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndividualMessageBusinessFragment extends Fragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private IndividualMessageBusinessFragment.CustomRecyclerView customRecyclerView;
    private List<RequestItem> requestItems = new ArrayList<>();

    private class CustomRecyclerView extends RecyclerView.Adapter<IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView ssn;
            private TextView status;

            MyViewHolder(View view) {
                super(view);
                ssn = view.findViewById(R.id.textViewSsn);
                status = view.findViewById(R.id.textViewStatus);
            }
        }

        private Activity context;
        private List<RequestItem> items;

        public CustomRecyclerView(@NonNull Activity context, List<RequestItem> requestItems) {
            this.context = context;
            this.items = requestItems;
        }

        @NonNull
        @Override
        public IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_request_listview_layout, parent, false);
            return new IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder holder, final int position) {
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.YELLOW);
            holder.ssn.setText(items.get(position).getSsn());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View individualMessageBusinessFragment = inflater.inflate(R.layout.fragment_business_message, container, false);
        ButterKnife.bind(this, individualMessageBusinessFragment);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(Objects.requireNonNull(getActivity()), requestItems);
        recyclerView.setAdapter(customRecyclerView);

        return individualMessageBusinessFragment;
    }

    @OnClick(R.id.add_individual_request)
    public void onAddIndividualRequestClick(){
        Intent intent = new Intent(getActivity(), RequestFormActivity.class);
        startActivity(intent);
    }

    public void addRequestItem(RequestItem requestItem) {
        requestItems.add(0, requestItem);
        refreshList();
    }

    private void refreshList() {
        customRecyclerView.notifyDataSetChanged();
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
