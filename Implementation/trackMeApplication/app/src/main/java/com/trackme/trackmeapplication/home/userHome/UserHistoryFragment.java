package com.trackme.trackmeapplication.home.userHome;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHistoryFragment extends Fragment {

    @BindView(R.id.listView)
    protected ListView listView;

    private CustomListView customListView;
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

    private class CustomListView extends ArrayAdapter<HistoryItem> {

        private class ViewHolder {
            private TextView date;
            private TextView info;

            ViewHolder(View view) {
                date = view.findViewById(R.id.textViewDate);
                info = view.findViewById(R.id.textViewInfo);
            }
        }

        private Activity context;

        public CustomListView(@NonNull Activity context, List<HistoryItem> historyItems) {
            super(context, R.layout.history_listview_layout, historyItems);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View r = convertView;
            ViewHolder viewHolder;
            if (r == null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.history_listview_layout, null, true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) r.getTag();

            viewHolder.date.setText(historyItems.get(position).getDate());
            viewHolder.info.setText(historyItems.get(position).getInfo());
            return r;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userHistoryFragment = inflater.inflate(R.layout.fragment_user_history, container, false);
        ButterKnife.bind(this, userHistoryFragment);

        /*TODO*/
        customListView = new CustomListView(Objects.requireNonNull(getActivity()), historyItems);
        listView.setAdapter(customListView);

        addHistoryItem(new HistoryItem("12/08/1222","Pulse 100 bpm - Pressure: 120/80"));

        return userHistoryFragment;
    }

    public void addHistoryItem(HistoryItem historyItem) {
        historyItems.add(0, historyItem);
        customListView.notifyDataSetChanged();
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onResume() {
        /*TODO*/
        super.onResume();
    }
}
