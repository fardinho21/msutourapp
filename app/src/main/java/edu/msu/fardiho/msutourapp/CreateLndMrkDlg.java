package edu.msu.fardiho.msutourapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.HashMap;

public class CreateLndMrkDlg extends DialogFragment {

    private TourActivity tourActivity;
    private TextView longTv;
    private TextView latTv;

    /**
     * Create the dialog box
     * @param savedInstanceState The saved instance bundle
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.create_lm);
        //inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.create_dlg, null);
        builder.setView(view);

        //get location, format and display it
        longTv = view.findViewById(R.id.user_longit);
        latTv = view.findViewById(R.id.user_latit);
        double lon = tourActivity.getUserLongitude();
        double lat = tourActivity.getUserLatitude();

        DecimalFormat df = new DecimalFormat("##.#####");

        longTv.setText(df.format(lon));
        latTv.setText(df.format(lat));


        AlertDialog dlg = builder.create();

        return dlg;
    }

    public void setTourActivity(TourActivity ta){
        tourActivity = ta;
    }

    //gathers inputted data from dialog, returns hashmap for landmark creation
    public HashMap<String, String> getInfo() {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        //get coordinates
        hashMap.put("longitude", String.valueOf(tourActivity.getUserLongitude()));
        hashMap.put("latitude", String.valueOf(tourActivity.getUserLatitude()));

        Dialog dlg  = getDialog();

        EditText name = dlg.findViewById(R.id.namelm);
        EditText desc = dlg.findViewById(R.id.enterDesc);



        //get description and name
        hashMap.put("desc", desc.getText().toString());
        hashMap.put("name", name.getText().toString());

        return hashMap;
    }


}
