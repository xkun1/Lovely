package com.example.kun.lovelier.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kun.lovelier.R;

/**
 * Created by kun on 2016/4/21.
 */
public class ShowinfoDialog extends DialogFragment {

        private String sex;
        private String birthday;
        private String address;

        private TextView show;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sex= (String) getArguments().get("sex");
        birthday= (String) getArguments().get("birthday");
        address= (String) getArguments().get("address");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_dialog,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        show= (TextView) view.findViewById(R.id.show);
        show.setText(sex+"/n"+birthday+"/n"+address);
    }
}
