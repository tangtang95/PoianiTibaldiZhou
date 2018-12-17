package com.trackme.trackmeapplication.Request.groupRequest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Group request fragment handles the group request sent by the third party. It shows all the request
 * in a recyclerView and it allows to the third party to create some one new.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class GroupRequestFragment extends BaseFragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private GroupRequestFragment.CustomRecyclerView customRecyclerView;
    private List<GroupRequestItem> groupRequestItems = new ArrayList<>();

    /**
     * Custom recyclerView class for showing the groupRequestItem in the recycler.
     */
    private class CustomRecyclerView extends RecyclerView.Adapter<GroupRequestFragment.CustomRecyclerView.MyViewHolder> {

        /**
         * The holder that searches the object in the layout and binds it.
         */
        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView status;
            private TextView receiver;

            /**
             * Constructor.
             *
             * @param view current view.
             */
            MyViewHolder(View view) {
                super(view);
                status = view.findViewById(R.id.textViewStatus);
                receiver = view.findViewById(R.id.textViewSsn);
            }
        }

        private List<GroupRequestItem> items;

        /**
         * Constructor.
         *
         * @param requestItems list of item to show in the recyclerView.
         */
        CustomRecyclerView(List<GroupRequestItem> requestItems) {
            this.items = requestItems;
        }

        @NonNull
        @Override
        public GroupRequestFragment.CustomRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_request_listview_layout, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupRequestFragment.CustomRecyclerView.MyViewHolder holder, final int position) {
            holder.status.setText(items.get(position).getStatus().name());
            holder.status.setTextColor(items.get(position).getStatus().getColor());
            holder.receiver.setText(getText(R.string.app_name));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_business_message;
    }

    @Override
    protected void setUpFragment() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(groupRequestItems);
        recyclerView.setAdapter(customRecyclerView);
    }


    /**
     * Handle the add individual request click event.
     */
    @OnClick(R.id.add_individual_request)
    public void onAddIndividualRequestClick(){
        Intent intent = new Intent(getActivity(), GroupRequestFormActivity.class);
        startActivity(intent);
    }

    public void addRequestItem(GroupRequestItem item) {
        groupRequestItems.add(0, item);
        refreshList();
    }

    /**
     * Refresh the recyclerView when it changes.
     */
    private void refreshList() {
        customRecyclerView.notifyDataSetChanged();
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
