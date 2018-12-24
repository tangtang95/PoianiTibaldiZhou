package com.trackme.trackmeapplication.request.individualRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.home.Settings;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkImp;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkIInterface;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.baseUtility.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Individual request fragment handles the individual request sent by the third party. It shows all the request
 * in a recyclerView.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class IndividualMessageFragment extends BaseFragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private CustomRecyclerView customRecyclerView;
    private List<RequestItem> requestItems = new ArrayList<>();

    private Handler handler;
    Runnable checkNewRequest;
    IndividualRequestNetworkIInterface individualrequestNetwork = IndividualRequestNetworkImp.getInstance();
    String username;

    /**
     * Custom recyclerView class for showing the individualRequestItem in the recycler.
     */
    private class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.MyViewHolder> {

        /**
         * The holder that searches the object in the layout and binds it.
         */
        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private RelativeLayout accept;
            private RelativeLayout refuse;

            /**
             * Constructor.
             *
             * @param view current view.
             */
            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textViewThirdPartyName);
                accept = view.findViewById(R.id.button_accept);
                refuse = view.findViewById(R.id.button_refuse);
            }
        }

        private Activity context;
        private List<RequestItem> items;

        /**
         * Constructor.
         *
         * @param requestItems list of item to show in the recyclerView.
         */
        CustomRecyclerView(@NonNull Activity context, List<RequestItem> requestItems) {
            this.context = context;
            this.items = requestItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_listview_layout, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.name.setText(items.get(position).getThirdPartyName());

            holder.accept.setOnClickListener(view -> {
                individualrequestNetwork.acceptIndividualRequest(items.get(position).getID());
                refreshList(individualrequestNetwork.getOwnIndividualRequest(username));
            });

            holder.refuse.setOnClickListener(view -> {

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            individualrequestNetwork.blockThirdPartyCustomer(items.get(position).getEmail());
                            individualrequestNetwork.refuseIndividualRequest(items.get(position).getID());
                            refreshList(individualrequestNetwork.getOwnIndividualRequest(username));
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            individualrequestNetwork.refuseIndividualRequest(items.get(position).getID());
                            refreshList(individualrequestNetwork.getOwnIndividualRequest(username));
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getString(R.string.block_third_party_message)).
                        setPositiveButton(android.R.string.yes, dialogClickListener)
                        .setNegativeButton(android.R.string.no, dialogClickListener).show();

            });

            holder.name.setOnClickListener(view -> {
                Intent intent = new Intent(context, RequestBodyActivity.class);
                intent.putExtra(Constant.SD_INDIVIDUAL_REQUEST_KEY, items.get(position));
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_user_message;
    }

    /**
     * This method setUp the layout and create a thread in order to periodically
     * refresh the list of items.
     */
    @Override
    protected void setUpFragment() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(Objects.requireNonNull(getActivity()), requestItems);
        recyclerView.setAdapter(customRecyclerView);

        SharedPreferences sp = getmContext().getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
        username = sp.getString(Constant.SD_USERNAME_DATA_KEY, null);

        handler = new Handler();
        checkNewRequest = new Runnable() {
            @Override
            public void run() {
                refreshList(individualrequestNetwork.getOwnIndividualRequest(username));
                handler.postDelayed(this, Settings.getRefreshItemTime());
            }
        };
        handler.post(checkNewRequest);
    }

    /**
     * Refresh the recyclerView when it changes.
     */
    private void refreshList(List<RequestItem> newItems) {
        requestItems.clear();
        requestItems.addAll(newItems);
        customRecyclerView.notifyDataSetChanged();
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(0));
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(checkNewRequest);
        super.onDestroy();
    }

}
