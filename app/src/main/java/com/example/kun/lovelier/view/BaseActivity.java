package com.example.kun.lovelier.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.kun.lovelier.R;

/**
 * Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: >>>>>>>");
        initContentView();
    }

    //初始化UI布局
    protected abstract void initContentView();


    //设置全屏
    public void setFullScreen() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    /*
     * 界面切换方法
     */

    public void forward(Class<?> classObj) {

        Intent intent = new Intent();

        intent.setClass(this, classObj);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(intent);

        this.finish();
    }

    /*
     * 界面切换方法（带数据）
     */
    public void forward(Class<?> classObj, Bundle params) {

        Intent intent = new Intent();

        intent.setClass(this, classObj);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtras(intent);

        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * toast()提示信息
    */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: >>>>>>>");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: >>>>>>>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: >>>>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: >>>>>>>");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: >>>>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: >>>>>>>");
    }


    protected void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 从照相机获取图片
     */
    public   void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, 4);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 直接从图库获取
     */
    public void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 2);
    }


}
