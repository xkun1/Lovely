package com.example.kun.lovelier.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.example.kun.lovelier.MainActivity;
import com.example.kun.lovelier.R;
import com.makeramen.roundedimageview.RoundedImageView;

import pl.droidsonroids.gif.GifImageView;

/**
 * kun on 2016/5/23.
 * com.example.kun.lovelier.view
 */
public class StartMainActivity extends Activity {

    private GifImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        imageView = (GifImageView) findViewById(R.id.home_img);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    Intent intent = new Intent();
                    intent.setClass(StartMainActivity.this, MainActivity.class);
                    startActivity(intent);
                    StartMainActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
