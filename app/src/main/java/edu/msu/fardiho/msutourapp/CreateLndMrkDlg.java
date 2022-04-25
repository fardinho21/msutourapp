package edu.msu.fardiho.msutourapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.Objects;

public class CreateLndMrkDlg extends DialogFragment {

    private TourActivity tourActivity;
    private TextView longTv;
    private TextView latTv;
    private TextView descriptionTv;
    private TextView nameTv;

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

        descriptionTv = view.findViewById(R.id.enterDesc);
        nameTv = view.findViewById(R.id.namelm);
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

    public void onDlgCreate() {
        //TODO: Create landmark json object and send request to server #DONE
        Server server = new Server();
        String res = "";
        try {
            //create landmark object
            Landmark lm = new Landmark(
                    descriptionTv.getText().toString(),
                    Float.parseFloat(latTv.getText().toString()),
                    Float.parseFloat(longTv.getText().toString()),
                    nameTv.getText().toString());

            //send landmark to server
            server.RequestToServer(
                    tourActivity.getUsername(),
                    tourActivity.getUserId(),
                    "CREATE_LANDMARK",
                    lm.getJSONString());

            while (true) {
                res = server.getServerResponse();
                if (res != null && !res.equals("")) {
                    JSONObject obj = new JSONObject(res); //JSONObject from string
                    if (obj.getString("op").equals("LANDMARK_CREATED")) {
                        //TODO: Pin landmark on map #DONE
                        tourActivity.pinLandmark(lm);
                        break;
                    }
                } else {
                    tourActivity.notifyUser("Error");
                }
                Thread.currentThread().sleep(500);
            }
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
            Log.e("CreateLndMrkDlg", Objects.requireNonNull(e.getMessage()));
        }
    }

    //setters
    public void setTourActivity(TourActivity ta){
        tourActivity = ta;
    }
    //getters



}
