package com.example.kun.lovelier.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.kun.lovelier.R;
import com.rey.material.widget.ProgressView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

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
            PackageInfo pkg = null;
            try {
                pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            String appName = pkg.applicationInfo.loadLabel(getPackageManager()).toString();
            new ShareAction(this).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .withTitle(appName)
                    .withText("来自天知妹的分享")
                    .withMedia(new UMImage(this, myShot(this)))
                    .withTargetUrl(webView.getUrl())
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            Toast.makeText(DetailsActivity.this, share_media + "分享成功啦", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                            Toast.makeText(DetailsActivity.this, share_media + "分享失败啦", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            Toast.makeText(DetailsActivity.this, share_media + "分享取消了", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .open();

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
        final ProgressDialog mDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        webView.setWebViewClient(new WebViewClient() {
                                     //当点击链接时,希望覆盖而不是打开新窗口
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         view.loadUrl(url);  //加载新的url
                                         return true;    //返回true,代表事件已处理,事件流到此终止
                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         mDialog.dismiss();
                                         //页面加载完毕
                                         super.onPageFinished(view, url);
                                     }

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         //页面开始加载
                                         super.onPageStarted(view, url, favicon);
                                         mDialog.setMessage("加载中...");
                                         mDialog.setCancelable(true);
                                         mDialog.show();
                                     }

                                 }

        );
    }


    /**
     * 截取当前界面
     *
     * @return
     */

    public Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item02 = menu.findItem(R.id.rotate);
        item02.setVisible(false);
        return true;
    }
}
