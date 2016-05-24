package com.example.kun.lovelier.view;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.kun.lovelier.R;
import com.example.kun.lovelier.utils.URLAvailability;
import com.rey.material.widget.Button;

import java.net.URL;

/**
 * kun on 2016/5/23.
 * 二维码扫描结果类
 * com.example.kun.lovelier.view
 */
public class OrCodeResult extends BaseActivity {

    private Button code_btn;

    private TextView result_text;

    private String retult;

    @Override
    protected void initContentView() {
        retult = getIntent().getStringExtra("result");
        setContentView(R.layout.activity_orcode);
        initView();
    }

    private void initView() {
        code_btn = (Button) findViewById(R.id.code_btn);
        result_text = (TextView) findViewById(R.id.code_result);
        result_text.setText(retult);
        code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统浏览器
                Uri content_url = Uri.parse(retult);
                Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
                startActivity(intent);
                OrCodeResult.this.finish();
            }
        });
    }
}
