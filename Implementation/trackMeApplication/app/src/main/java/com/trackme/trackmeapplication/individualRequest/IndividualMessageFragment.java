package com.trackme.trackmeapplication.individualRequest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    protected ListView listView;

    private CustomListView customListView;
    private List<RequestItem> requestItems = new ArrayList<>();

    private class CustomListView extends ArrayAdapter<RequestItem> {

        private class ViewHolder extends RecyclerView.ViewHolder{
            private TextView name;
            private RelativeLayout accept;
            private RelativeLayout refuse;

            ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textViewThirdPartyName);
                accept = view.findViewById(R.id.button_accept);
                refuse = view.findViewById(R.id.button_refuse);
            }
        }

        private Activity context;

        public CustomListView(@NonNull Activity context, List<RequestItem> requestItems) {
            super(context, R.layout.request_listview_layout,requestItems);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View r = convertView;
            ViewHolder viewHolder;
            if (r == null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.request_listview_layout, parent, false);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) r.getTag();

            viewHolder.name.setText(requestItems.get(position).getThirdPartyName());

            viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*TODO*/
                    removeRequestItem(position);
                }
            });

            viewHolder.refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
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

            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RequestBodyActivity.class);
                    intent.putExtra("item", requestItems.get(position));
                    startActivity(intent);
                }
            });

            return r;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View individualMessageFragment = inflater.inflate(R.layout.fragment_user_message, container, false);
        ButterKnife.bind(this, individualMessageFragment);

        /*TODO*/

        requestItems.add(new RequestItem("TangTangEmperor", "12/12/2018", "01/01/3018",
                "Controlling that the slaves are doing something when i'm at university "));
        requestItems.add(new RequestItem("TangTangEmperor", "12/12/2018", "01/01/3018",
                "Controlling that the slaves are doing something when i'm at university "));

        customListView = new CustomListView(Objects.requireNonNull(getActivity()), requestItems);
        listView.setAdapter(customListView);


        return individualMessageFragment;
    }


    public void addRequestItem(RequestItem requestItem) {
        requestItems.add(0,requestItem);
        refreshList();
    }

    public void removeRequestItem(int j){
        requestItems.remove(j);
        refreshList();
    }

    private void refreshList() {
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
