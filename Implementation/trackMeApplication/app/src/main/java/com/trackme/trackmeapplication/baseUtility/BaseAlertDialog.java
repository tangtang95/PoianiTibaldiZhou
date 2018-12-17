package com.trackme.trackmeapplication.baseUtility;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;

public class BaseAlertDialog {

    private AlertDialog.Builder builder;

    public BaseAlertDialog(Context context, String message, String title) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    public void show() {
        builder.show();
    }

}
