package com.trackme.trackmeapplication.Request.individualRequest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndividualMessageFragment extends Fragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private CustomRecyclerView customRecyclerView;
    private List<RequestItem> requestItems = new ArrayList<>();

    private class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private RelativeLayout accept;
            private RelativeLayout refuse;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textViewThirdPartyName);
                accept = view.findViewById(R.id.button_accept);
                refuse = view.findViewById(R.id.button_refuse);
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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_listview_layout, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.name.setText(items.get(position).getThirdPartyName());

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*TODO*/
                    removeRequestItem(position);
                }
            });

            holder.refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    /*TODO*/
                                    removeRequestItem(position);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    /*TODO*/
                                    removeRequestItem(position);
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to block this third party customer?").
                            setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RequestBodyActivity.class);
                    intent.putExtra("item", items.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View individualMessageFragment = inflater.inflate(R.layout.fragment_user_message, container, false);
        ButterKnife.bind(this, individualMessageFragment);

        /*TODO*/

        requestItems.add(new RequestItem("","TangTangEmperor", "12/12/2018", "01/01/3018",
                "Controlling that the slaves are doing something when i'm at university "));
        requestItems.add(new RequestItem("","TangTangEmperor", "12/12/2018", "01/01/3018",
                "Controlling that the slaves are doing something when i'm at university "));
        requestItems.add(new RequestItem("","TangTangEmperor", "12/12/2018", "01/01/3018",
                "Controlling that the slaves are doing something when i'm at university "));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(Objects.requireNonNull(getActivity()), requestItems);
        recyclerView.setAdapter(customRecyclerView);

        return individualMessageFragment;
    }


    public void addRequestItem(RequestItem requestItem) {
        requestItems.add(0, requestItem);
        refreshList();
    }

    public void removeRequestItem(int j) {
        requestItems.remove(j);
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
        /*TODO*/
        super.onResume();
    }
}
