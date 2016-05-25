package com.example.kun.lovelier;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;


import com.bigdata.zixinglibrary.ZXing.CaptureActivity;
import com.example.kun.lovelier.adapter.MyViewPagerAdapter;
import com.example.kun.lovelier.view.BaseActivity;
import com.example.kun.lovelier.view.MaterialActivity;
import com.example.kun.lovelier.view.OrCodeResult;
import com.example.kun.lovelier.view.QueryAll;
import com.example.kun.lovelier.view.WeatherActivity;
import com.makeramen.roundedimageview.RoundedImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;

    private long exitTime = 0;

    private CoordinatorLayout mCoordinatorLayout;  //增强的FragmeLayout
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView mNavigationView;

    private RoundedImageView imageView;//首页头像

    private TextView nametxt;//首页name

    private String[] mTitles;  //TabLayout标题

    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;

    @Override

    protected void initContentView() {

        setContentView(R.layout.activity_main);


        //初始化各种控件
        initView();

        SharedPreferences preferences = getSharedPreferences("imgUrl", MODE_PRIVATE);

        String imgUrl = preferences.getString("imgUrl", null);
        if (imgUrl != null) {
            imageView.setImageURI(Uri.parse(imgUrl));
        }
        // 初始化mTitles、mFragments等ViewPager需要的数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews();

    }


    private void configViews() {
        // 设置显示Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);

        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.setOnPageChangeListener(this);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

        // 设置FloatingActionButton的点击事件
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "小坤", Toast.LENGTH_SHORT).show();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * 重写返回按键
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            MyFragment mFragment = new MyFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);
        }

    }

    private void initView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.id_appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);


        View headerView = mNavigationView.getHeaderView(0);

        imageView = (RoundedImageView) headerView.findViewById(R.id.imageView);
        nametxt = (TextView) headerView.findViewById(R.id.name_txt);

        /**
         * 设置：
         */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.weather: //天气
                Intent intentWe = new Intent();
                intentWe.setClass(MainActivity.this, WeatherActivity.class);
                startActivity(intentWe);
                break;
            case R.id.two_code: //二维码
//                Toast.makeText(MainActivity.this, "正在开发。。。", Toast.LENGTH_SHORT).show();
                Intent intentcode = new Intent();
                intentcode.setClass(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intentcode, 1);
                break;
            case R.id.query: //查询
                Toast.makeText(MainActivity.this, "正在开发。。。", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, QueryAll.class);
//                startActivity(intent);
                break;
            case R.id.nav_share: //分享
                Toast.makeText(MainActivity.this, "正在开发。。。", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_manage: //设置
                Toast.makeText(MainActivity.this, "正在开发。。。", Toast.LENGTH_SHORT).show();
                break;
        }

        // Menu item点击后选中，并关闭Drawerlayout
        setTitle(menuItem.getTitle()); // 改变页面标题，标明导航状态
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String action = data.getExtras().getString("result");
                Intent intent = new Intent();
                intent.putExtra("result", action);
                intent.setClass(this, OrCodeResult.class);
                startActivity(intent);
            } else if (requestCode == 2) {
                Uri imgUrl = data.getData();
                SharedPreferences preferences = getSharedPreferences("imgUrl", MODE_PRIVATE);
                preferences.edit().putString("imgUrl", String.valueOf(imgUrl)).commit();
                imageView.setImageURI(imgUrl);
            }
        } else {
            return;
        }
    }

    /**
     * 隐藏ActionBar中的某一个menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item01 = menu.findItem(R.id.share);
        MenuItem item02 = menu.findItem(R.id.rotate);
        item01.setVisible(false);
        item02.setVisible(false);
        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 2);
    }


}

