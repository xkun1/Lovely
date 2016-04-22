package com.example.kun.lovelier.view;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.kun.lovelier.R;
import com.rey.material.widget.ProgressView;

/**
 * Created by kun on 2016/4/19.
 */
public class DetailsActivity extends BaseActivity {
    private WebView webView;


    @Override
    protected void initContentView() {
        setContentView(R.layout.details_activity);
        init();
        initWebView();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.web);
        webView.loadUrl(getIntent().getStringExtra("weburl"));
    }

    //菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share) {
            Toast.makeText(DetailsActivity.this, "分享", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本


        webView.loadUrl(getIntent().getStringExtra("weburl"));

        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
        });
    }
}
