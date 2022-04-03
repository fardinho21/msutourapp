package edu.msu.fardiho.msutourapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LndMrkDescDlg extends DialogFragment {

    private String lndMrkName;
    private String desc;
    LndMrkDescDlg(String n, String d){
        lndMrkName = n; desc = d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.lndmrkdesc_dlg, null);
        builder.setView(view);


        //set the description for the dialog
        TextView descTxtVw = view.findViewById(R.id.lndMrkDescTxtVw);
        descTxtVw.setText(desc);
        builder.setTitle(lndMrkName);

        AlertDialog dlg = builder.create();
        return dlg;
    }
}
