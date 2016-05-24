package com.example.kun.lovelier.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.kun.lovelier.R;
import com.example.kun.lovelier.URL.HttpUrl;
import com.example.kun.lovelier.adapter.BaseRecyclerAdapter;
import com.example.kun.lovelier.adapter.RecyclerHolder;
import com.example.kun.lovelier.adapter.SpacesItemDecoration;
import com.example.kun.lovelier.dialog.WeatherDialog;
import com.example.kun.lovelier.moudle.WeatherBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rey.material.widget.ListView;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 天气Activity
 */
public class WeatherActivity extends BaseActivity {

    private static final String TAG = "WeatherActivity";

    //百度定位
    public LocationClient mLocationClient = null;

    private Gson gson;


    private List<WeatherBean.WeatherBeanBean.DailyForecastBean> dailyForecastBean;//未来几天集合

    private RecyclerView mRecyclerView;

    private BaseRecyclerAdapter<WeatherBean.WeatherBeanBean.DailyForecastBean> recyclerAdapter;

    private List<WeatherBean.WeatherBeanBean> weatherList;


    private TextView now_tmp, now_cond, ip_aqi, cityText, locText;

    @Override
    protected void initContentView() {
        setContentView(R.layout.weather_activity);

        initbaidu();

        gson = new Gson();
        weatherList = new ArrayList<>();

        dailyForecastBean = new ArrayList<>();

        initView();

        recyclerAdapter = new BaseRecyclerAdapter<WeatherBean.WeatherBeanBean.DailyForecastBean>(mRecyclerView, dailyForecastBean, R.layout.weather_ry_item) {
            @Override
            public void convert(RecyclerHolder holder, WeatherBean.WeatherBeanBean.DailyForecastBean item, int position, boolean isScrolling) {
                holder.setText(R.id.item_date, item.getDate());
                holder.setText(R.id.item_tmp, item.getTmp().getMax() + "/" + item.getTmp().getMin());
                holder.setText(R.id.item_feng, item.getWind().getDir());
                holder.setText(R.id.item_txt_d_n, item.getCond().getTxt_d() + "/" + item.getCond().getTxt_n());
            }
        };

        initRecycler();
//
//        init();
//
//        refresh();


    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(recyclerAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        mRecyclerView.addItemDecoration(decoration);
    }


    private void initView() {

        now_tmp = (TextView) findViewById(R.id.now_tmp);
        now_cond = (TextView) findViewById(R.id.now_cond);
        ip_aqi = (TextView) findViewById(R.id.ip_qlty);
        cityText = (TextView) findViewById(R.id.city);
        locText = (TextView) findViewById(R.id.loc);
        mRecyclerView = (RecyclerView) findViewById(R.id.weather_recycler);
    }

    private void loading(String string, boolean isfalg) {
        if (isfalg == true) {
            RxVolley.get(HttpUrl.WEATH + string, new HttpCallback() {
                @Override
                public void onSuccess(String t) {

                    String weatherStr = t.replace("HeWeather data service 3.0", "weatherBean");

                    WeatherBean weatherBean = gson.fromJson(weatherStr, WeatherBean.class);
                    weatherList.add(weatherBean.getWeatherBean().get(0));


                    WeatherBean.WeatherBeanBean weatherBeanBean = weatherList.get(0);

                    dailyForecastBean.addAll(weatherBeanBean.getDaily_forecast());

                    recyclerAdapter.notifyDataSetChanged();

                    ip_aqi.setText(weatherBeanBean.getAqi().getCity().getAqi());  //空气质量类别
                    now_tmp.setText(weatherBeanBean.getNow().getTmp());  //当前温度
                    now_cond.setText(weatherBeanBean.getNow().getCond().getTxt()); // 天气状况
                    cityText.setText(weatherBeanBean.getBasic().getCity());//当前城市
                    locText.setText(weatherBeanBean.getBasic().getUpdate().getLoc()); //当前更新时间

                }
            });
        }

    }

    private void initbaidu() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String city = bdLocation.getCity();
                String s = city.substring(0, city.length() - 1);
                loading(s, true);
            }
        });    //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
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
        MenuItem item = menu.findItem(R.id.share);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.rotate) {
            dailyForecastBean.clear();
            initbaidu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
