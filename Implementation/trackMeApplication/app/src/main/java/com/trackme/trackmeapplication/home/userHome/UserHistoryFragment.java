package com.trackme.trackmeapplication.home.userHome;

import android.app.Activity;
import android.content.Context;
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

public class UserHistoryFragment extends Fragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private CustomRecyclerView customRecyclerView;
    private List<HistoryItem> historyItems = new ArrayList<>();

    public class HistoryItem {
        private String date;
        private String info;

        public HistoryItem(String date, String info) {
            this.date = date;
            this.info = info;
        }

        public String getDate() {
            return date;
        }

        public String getInfo() {
            return info;
        }
    }

    private class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView date;
            private TextView info;

            MyViewHolder(View view) {
                super(view);
                date = view.findViewById(R.id.textViewDate);
                info = view.findViewById(R.id.textViewInfo);
            }
        }

        private Context context;
        private List<HistoryItem> items;

        public CustomRecyclerView(@NonNull Activity context, List<HistoryItem> historyItems) {
            this.context = context;
            this.items = historyItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_listview_layout, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.date.setText(historyItems.get(position).getDate());
            holder.info.setText(historyItems.get(position).getInfo());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userHistoryFragment = inflater.inflate(R.layout.fragment_user_history, container, false);
        ButterKnife.bind(this, userHistoryFragment);

        /*TODO*/

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(Objects.requireNonNull(getActivity()), historyItems);
        recyclerView.setAdapter(customRecyclerView);

        addHistoryItem(new HistoryItem("12/08/1222","Pulse 100 bpm - Pressure: 120/80"));

        return userHistoryFragment;
    }

    public void addHistoryItem(HistoryItem historyItem) {
        historyItems.add(0, historyItem);
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
        /*TODO*/
        super.onResume();
    }
}
