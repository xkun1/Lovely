package com.example.kun.lovelier.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kun.lovelier.R;

/**
 * Created by kun on 2016/4/22.
 */
public class WeatherDialog extends DialogFragment {

    private TextView api, pm, pm10, so, no;

    private Bundle bundle;

    private LinearLayout dialog_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_Dialog);
        bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog_layout = (LinearLayout) view.findViewById(R.id.dialog_layout);
        api = (TextView) view.findViewById(R.id.api);
        pm = (TextView) view.findViewById(R.id.pm2_5);
        pm10 = (TextView) view.findViewById(R.id.pm10);
        so = (TextView) view.findViewById(R.id.so2);
        no = (TextView) view.findViewById(R.id.no2);
        init();
        click();
    }

    private void click() {
        dialog_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void init() {
        api.setText(bundle.getString("api"));
        pm.setText(bundle.getString("pm25"));
        pm10.setText(bundle.getString("pm10"));
        so.setText(bundle.getString("so2"));
        no.setText(bundle.getString("no2"));
    }
}
