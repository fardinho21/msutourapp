package edu.msu.fardiho.msutourapp;

import android.util.Log;
import android.view.View;

public class MainClickListener implements View.OnClickListener{

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Log.i("MainClickListener", String.valueOf(viewId));
    }
}
