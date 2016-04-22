package com.example.kun.lovelier.view;

import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.example.kun.lovelier.R;
import com.example.kun.lovelier.URL.HttpUrl;
import com.example.kun.lovelier.dialog.ShowinfoDialog;
import com.example.kun.lovelier.loadtools.Httploading;
import com.example.kun.lovelier.moudle.Card_Data;
import com.example.kun.lovelier.moudle.Ip_Data;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

/**
 * Created by kun on 2016/4/21.
 */
public class QueryAll extends BaseActivity implements View.OnClickListener {

    private EditText editText_card; //身份证查询

    private ExtractEditText editText_ip; //ip地址查询

    private Card_Data card_data;

    private Ip_Data ip_data;

    private Gson gson;

    private Button btn_card;

    private Button btn_ip;

    @Override
    protected void initContentView() {
        setContentView(R.layout.query_activity);
        gson=new Gson();
        initView();
        init();

    }

    private void init() {

    }

    private void initView() {
        editText_card = (EditText) findViewById(R.id.edit_query);
        editText_ip = (ExtractEditText) findViewById(R.id.ip_query);
        btn_card = (Button) findViewById(R.id.query_btn);
        btn_ip = (Button) findViewById(R.id.query_ip_btn);

        btn_card.setOnClickListener(this);
        btn_ip.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.query_btn:
                ShowinfoDialog dialog=new ShowinfoDialog();
                Bundle bundle=new Bundle();
                Card_Data loding = loding(String.valueOf(editText_card.getText()));
                bundle.putString("sex",loding.getRetData().getSex());
                bundle.putString("birthday",loding.getRetData().getBirthday());
                bundle.putString("address",loding.getRetData().getAddress());
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(),"");
                break;
            case R.id.query_ip_btn:
                lodingIp(String.valueOf(editText_card.getText()));
                break;
        }

    }

    public Card_Data loding(String str){
        String request = Httploading.request(HttpUrl.WEATH, str);
         card_data = gson.fromJson(request, Card_Data.class);
        return card_data;
    }

    public Ip_Data lodingIp(String str){
        String request = Httploading.request(HttpUrl.CARDURL, str);
        card_data = gson.fromJson(request, Card_Data.class);
        return ip_data;
    }

}
